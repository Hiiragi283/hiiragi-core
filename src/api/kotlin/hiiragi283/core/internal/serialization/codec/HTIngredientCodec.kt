package hiiragi283.core.internal.serialization.codec

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.fluids.crafting.EmptyFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient
import net.neoforged.neoforge.registries.NeoForgeRegistries

private typealias TagOrHolder<T> = Either<TagKey<T>, Holder<T>>

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
internal object HTIngredientCodec {
    @JvmStatic
    private fun <T : Any> tagOrHolderCodec(registryKey: RegistryKey<T>): Codec<TagOrHolder<T>> = HTCodecs.either(HTCodecs.tagKey(registryKey, true), HTCodecs.holder(registryKey))

    //    Item    //

    @JvmStatic
    private val ITEM_VALUE_CODEC: Codec<Ingredient.Value> = tagOrHolderCodec(Registries.ITEM).flatXmap(
        { tagOrHolder: TagOrHolder<Item> ->
            tagOrHolder.fold(
                { tagKey: TagKey<Item> -> DataResult.success(Ingredient.TagValue(tagKey)) },
                { holder: Holder<Item> -> DataResult.success(Ingredient.ItemValue(ItemStack(holder))) },
            )
        },
        { value: Ingredient.Value ->
            when (value) {
                is Ingredient.ItemValue -> DataResult.success(Either.Right(value.item().itemHolder))
                is Ingredient.TagValue -> DataResult.success(Either.Left(value.tag()))
                else -> DataResult.error { "Cannot serialize ingredient value $value" }
            }
        },
    )

    @JvmField
    val ITEM: Codec<Ingredient> = HTCodecs.dispatchOrElse(
        NeoForgeRegistries.INGREDIENT_TYPES.byNameCodec(),
        ICustomIngredient::getType,
        IngredientType<*>::codec,
        ITEM_VALUE_CODEC.listOrElement(),
    ).xmap(
        { either: Either<ICustomIngredient, List<Ingredient.Value>> ->
            either.fold(ICustomIngredient::toVanilla) { values: List<Ingredient.Value> -> Ingredient.fromValues(values.stream()) }
        },
        { ingredient: Ingredient ->
            val custom: ICustomIngredient? = ingredient.customIngredient
            if (custom != null) {
                Either.Left(custom)
            } else {
                Either.Right(ingredient.values.toList())
            }
        },
    )

    //    Fluid    //

    @JvmStatic
    private val FLUID_HOLDER_CODEC: Codec<FluidIngredient> = tagOrHolderCodec(Registries.FLUID).flatComapMap(
        { tagOrHolder: TagOrHolder<Fluid> -> tagOrHolder.fold(FluidIngredient::tag, ::SingleFluidIngredient) },
        { ingredient: FluidIngredient ->
            when (ingredient) {
                is EmptyFluidIngredient -> DataResult.error { "Cannot serialize empty fluid ingredient" }
                is TagFluidIngredient -> DataResult.success(Either.Left(ingredient.tag()))
                is SingleFluidIngredient -> DataResult.success(Either.Right(ingredient.fluid()))
                else -> DataResult.error { "Cannot serialize fluid ingredient $ingredient into tag or key" }
            }
        },
    )

    @JvmField
    val FLUID: Codec<FluidIngredient> = HTCodecs.dispatchOrElse(
        NeoForgeRegistries.FLUID_INGREDIENT_TYPES.byNameCodec(),
        FluidIngredient::getType,
        FluidIngredientType<*>::codec,
        FLUID_HOLDER_CODEC,
    ).xmap(
        { either: Either<FluidIngredient, FluidIngredient> -> either.unwrap() },
        { ingredient: FluidIngredient ->
            when (ingredient) {
                is TagFluidIngredient -> Either.Right(ingredient)
                is SingleFluidIngredient -> Either.Right(ingredient)
                else -> Either.Left(ingredient)
            }
        },
    )
}
