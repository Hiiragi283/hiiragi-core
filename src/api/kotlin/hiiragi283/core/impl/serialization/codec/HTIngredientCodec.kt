package hiiragi283.core.impl.serialization.codec

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.getHolderLike
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.util.DFUEither
import hiiragi283.core.api.util.Either
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * @suppress
 */
internal object HTIngredientCodec {
    @JvmStatic
    private fun <T : Any> tagOrKeyListCodec(registryKey: RegistryKey<T>): Codec<Either<TagKey<T>, List<ResourceKey<T>>>> = HTCodecs
        .either(
            TagKey.hashedCodec(registryKey),
            ResourceKey.codec(registryKey).listOrElement(),
        )

    //    Item    //

    @JvmStatic
    private val CUSTOM_ITEM_CODEC: Codec<ICustomIngredient> = NeoForgeRegistries.INGREDIENT_TYPES
        .byNameCodec()
        .dispatch(ICustomIngredient::getType, IngredientType<*>::codec)

    @JvmStatic
    private val VALUE_CODEC: Codec<Ingredient.Value> =
        tagOrKeyListCodec(Registries.ITEM)
            .xmap(
                { either: Either<TagKey<Item>, List<ResourceKey<Item>>> ->
                    either.fold(Ingredient::TagValue) { keys: List<ResourceKey<Item>> ->
                        val items: List<Item> = keys.mapNotNull(BuiltInRegistries.ITEM::get)
                        when {
                            items.size == 1 -> items[0].let(::ItemStack).let(Ingredient::ItemValue)
                            else -> items.map(::ItemStack).let(::StacksValue)
                        }
                    }
                },
                { value: Ingredient.Value ->
                    when (value) {
                        is Ingredient.TagValue -> Either.Left(value.tag)
                        else ->
                            value.items
                                .map(ItemStack::getHolderLike)
                                .map(HTKeyLike<Item>::getResourceKey)
                                .let { Either.Right(it) }
                    }
                },
            )

    @JvmField
    val ITEM: Codec<Ingredient> = HTCodecs
        .either(VALUE_CODEC.listOrElement(), CUSTOM_ITEM_CODEC)
        .xmap(
            { either: Either<List<Ingredient.Value>, ICustomIngredient> ->
                either.fold(
                    { values: List<Ingredient.Value> -> Ingredient.fromValues(values.stream()) },
                    ::Ingredient,
                )
            },
            { ingredient: Ingredient ->
                val custom: ICustomIngredient? = ingredient.customIngredient
                if (custom != null) {
                    Either.Right(custom)
                } else {
                    Either.Left(ingredient.values.toList())
                }
            },
        )

    @JvmRecord
    private data class StacksValue(private val stacks: List<ItemStack>) : Ingredient.Value {
        override fun getItems(): Collection<ItemStack> = stacks
    }

    //    Fluid    //

    @JvmStatic
    private val FLUID_HOLDER_CODEC: Codec<FluidIngredient> =
        tagOrKeyListCodec(Registries.FLUID)
            .xmap(
                { either: Either<TagKey<Fluid>, List<ResourceKey<Fluid>>> ->
                    either.fold(FluidIngredient::tag) { keys: List<ResourceKey<Fluid>> ->
                        val fluids: List<Fluid> = keys.mapNotNull(BuiltInRegistries.FLUID::get)
                        when (fluids.size) {
                            0 -> FluidIngredient.empty()
                            1 -> FluidIngredient.single(fluids[0])
                            else -> CompoundFluidIngredient(fluids.map(FluidIngredient::single))
                        }
                    }
                },
                { ingredient: FluidIngredient ->
                    when (ingredient) {
                        is TagFluidIngredient -> Either.Left(ingredient.tag())
                        else ->
                            ingredient.stacks
                                .map(FluidStack::getHolderLike)
                                .map(HTKeyLike<Fluid>::getResourceKey)
                                .let { Either.Right(it) }
                    }
                },
            )

    @JvmField
    val FLUID: MapCodec<FluidIngredient> = NeoForgeExtraCodecs
        .dispatchMapOrElse(
            NeoForgeRegistries.FLUID_INGREDIENT_TYPES.byNameCodec(),
            FluidIngredient::getType,
            FluidIngredientType<*>::codec,
            FLUID_HOLDER_CODEC.fieldOf(HTConst.FLUIDS),
        ).xmap({ DFUEither.unwrap(it) }) { ingredient: FluidIngredient ->
            when (ingredient) {
                is TagFluidIngredient -> DFUEither.right(ingredient)
                is SingleFluidIngredient -> DFUEither.right(ingredient)
                is CompoundFluidIngredient -> {
                    val children: List<FluidIngredient> = ingredient.children()
                    when {
                        children.all { it is SingleFluidIngredient } -> DFUEither.right(ingredient)
                        else -> DFUEither.left(ingredient)
                    }
                }
                else -> DFUEither.left(ingredient)
            }
        }
}
