package hiiragi283.core.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.unwrapEither
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.core.Holder
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
    private fun <T : Any> tagOrKeyListCodec(registryKey: RegistryKey<T>): Codec<Either<TagKey<T>, List<ResourceKey<T>>>> = Codec
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
                    either.map(Ingredient::TagValue) { keys: List<ResourceKey<Item>> ->
                        val items: List<Item> = keys.mapNotNull(BuiltInRegistries.ITEM::get)
                        when {
                            items.size == 1 -> items[0].let(::ItemStack).let(Ingredient::ItemValue)
                            else -> items.map(::ItemStack).let(::StacksValue)
                        }
                    }
                },
                { value: Ingredient.Value ->
                    when (value) {
                        is Ingredient.TagValue -> Either.left(value.tag)
                        else ->
                            value.items
                                .map(ItemStack::getItemHolder)
                                .map(Holder<Item>::toLike)
                                .map(HTKeyLike<Item>::getResourceKey)
                                .let { Either.right(it) }
                    }
                },
            )

    @JvmField
    val ITEM: Codec<Ingredient> = Codec
        .either(VALUE_CODEC.listOrElement(), CUSTOM_ITEM_CODEC)
        .xmap(
            { either: Either<List<Ingredient.Value>, ICustomIngredient> ->
                either.map(
                    { values: List<Ingredient.Value> -> Ingredient.fromValues(values.stream()) },
                    ::Ingredient,
                )
            },
            { ingredient: Ingredient ->
                val custom: ICustomIngredient? = ingredient.customIngredient
                if (custom != null) {
                    Either.right(custom)
                } else {
                    Either.left(ingredient.values.toList())
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
                    either.map(FluidIngredient::tag) { keys: List<ResourceKey<Fluid>> ->
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
                        is TagFluidIngredient -> Either.left(ingredient.tag())
                        else ->
                            ingredient.stacks
                                .map(FluidStack::getFluidHolder)
                                .map(Holder<Fluid>::toLike)
                                .map(HTKeyLike<Fluid>::getResourceKey)
                                .let { Either.right(it) }
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
        ).xmap(::unwrapEither) { ingredient: FluidIngredient ->
            when (ingredient) {
                is TagFluidIngredient -> Either.right(ingredient)
                is SingleFluidIngredient -> Either.right(ingredient)
                is CompoundFluidIngredient -> {
                    val children: List<FluidIngredient> = ingredient.children()
                    when {
                        children.all { it is SingleFluidIngredient } -> Either.right(ingredient)
                        else -> Either.left(ingredient)
                    }
                }
                else -> Either.left(ingredient)
            }
        }
}
