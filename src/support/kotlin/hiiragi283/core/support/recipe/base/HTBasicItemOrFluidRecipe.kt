package hiiragi283.core.support.recipe.base

import com.mojang.serialization.MapCodec
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
import hiiragi283.core.support.data.recipe.HTItemOrFluidRecipeBuilder
import java.util.function.Predicate
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

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
        fun <T : HTBasicItemOrFluidRecipe> codec(factory: HTItemOrFluidRecipeBuilder.Factory<T>): MapCodec<T> = HTCodecs.recordMap { instance ->
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

    override fun getMatchingStacks(first: ItemStack, second: FluidStack): Pair<ItemStack, FluidStack> {
        val (item: HTItemIngredient?, fluid: HTFluidIngredient?) = ingredient.toPair()
        return (item?.getMatchingStack(first) ?: ItemStack.EMPTY) to (fluid?.getMatchingStack(second) ?: FluidStack.EMPTY)
    }

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): HTItemAndFluidResult = result.mapLeft { it.createOrEmpty() }.mapRight { it.create() }.let(::HTItemAndFluidResult)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.merge(HTItemIngredient::isIncomplete, HTFluidIngredient::isIncomplete) { item: Boolean, fluid: Boolean -> item || fluid }
        val bool2: Boolean = result.getLeft()?.isIncomplete() ?: false
        return bool1 || bool2
    }
}
