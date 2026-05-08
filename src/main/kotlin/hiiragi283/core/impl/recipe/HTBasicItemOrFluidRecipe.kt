package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.util.Ior
import hiiragi283.core.common.data.recipe.builder.HTItemOrFluidRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

open class HTBasicItemOrFluidRecipe(
    val ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
    val result: Ior<HTItemResult, HTFluidResult>,
    override val progressData: HTProgressData,
) : HTItemOrFluidRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val INGREDIENT_CODEC: MapCodec<Ior<HTItemIngredient, HTFluidIngredient>> = HTCodecs
            .ior(
                HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT),
                HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT),
            )

        @JvmField
        val RESULT_CODEC: MapCodec<Ior<HTItemResult, HTFluidResult>> = HTCodecs
            .ior(
                HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT),
                HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT),
            )

        @JvmStatic
        fun <T : HTBasicItemOrFluidRecipe> codec(factory: HTItemOrFluidRecipeBuilder.Factory<T>): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    INGREDIENT_CODEC.forGetter(HTBasicItemOrFluidRecipe::ingredient),
                    RESULT_CODEC.forGetter(HTBasicItemOrFluidRecipe::result),
                    HTProgressData.CODEC.forGetter(HTBasicItemOrFluidRecipe::progressData),
                ).apply(instance, factory::create)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTBasicItemOrFluidRecipe> = codec(::HTBasicItemOrFluidRecipe)
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean = ingredient.fold(
        { it.test(first) && second.isEmpty },
        { it.test(second) && first.isEmpty },
        { item: Predicate<ItemStack>, fluid: Predicate<FluidStack> -> item.test(first) && fluid.test(second) },
    )

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> {
        val (item: HTItemIngredient?, fluid: HTFluidIngredient?) = ingredient.toPair()
        return (item?.getRequiredAmount(first) ?: 0) to (fluid?.getRequiredAmount(second) ?: 0)
    }

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): HTItemAndFluidResult = result.mapLeft { it.getOrEmpty() }.mapRight { it.create() }.let(::HTItemAndFluidResult)
}
