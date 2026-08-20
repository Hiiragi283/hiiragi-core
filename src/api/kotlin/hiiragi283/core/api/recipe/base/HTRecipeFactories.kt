package hiiragi283.core.api.recipe.base

import com.mojang.datafixers.util.Function3
import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.input.getItemOrEmpty
import java.util.function.BiFunction
import java.util.function.Function
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

data object HTRecipeFactories {
    //    Single Input    //

    interface SingleInput<INPUT : RecipeInput, INPUT_A : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        Function<INPUT_A, OUTPUT> {
        override fun apply(input: INPUT_A): OUTPUT
    }

    /**
     * 1種類の液体から完成品を作る[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleFluidTo<OUTPUT : Any> : SingleInput<HTSingleFluidRecipeInput, FluidStack, OUTPUT> {
        override fun assemble(input: HTSingleFluidRecipeInput): OUTPUT = apply(input.fluid)
    }

    /**
     * 1種類のアイテムから完成品を作る[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleItemTo<OUTPUT : Any> : SingleInput<SingleRecipeInput, ItemStack, OUTPUT> {
        override fun assemble(input: SingleRecipeInput): OUTPUT = apply(input.item())
    }

    //    Double Input    //

    interface DoubleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        BiFunction<INPUT_A, INPUT_B, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B): OUTPUT
    }

    /**
     * 1種類のアイテムと液体から完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    fun interface ItemAndFluid<OUTPUT : Any> : DoubleInput<HTItemAndFluidRecipeInput, ItemStack, FluidStack, OUTPUT> {
        override fun assemble(input: HTItemAndFluidRecipeInput): OUTPUT = apply(input.item, input.fluid)
    }

    /**
     * 2種類のアイテムから完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    fun interface DoubleItem<OUTPUT : Any> : DoubleInput<RecipeInput, ItemStack, ItemStack, OUTPUT> {
        override fun assemble(input: RecipeInput): OUTPUT = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1))
    }

    //    Triple Input    //

    interface TripleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        Function3<INPUT_A, INPUT_B, INPUT_C, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B, third: INPUT_C): OUTPUT
    }

    fun interface ItemAndDoubleFluid<INPUT : HTFluidRecipeInput, OUTPUT : Any> : TripleInput<INPUT, ItemStack, FluidStack, FluidStack, OUTPUT> {
        override fun assemble(input: INPUT): OUTPUT = TODO("Not yet implemented")
    }

    fun interface DoubleItemAndFluid<INPUT : HTFluidRecipeInput, OUTPUT : Any> : TripleInput<INPUT, ItemStack, ItemStack, FluidStack, OUTPUT> {
        override fun assemble(input: INPUT): OUTPUT = TODO("Not yet implemented")
    }

    /**
     * 3種類のアイテムから完成品を作る[TripleInput]の拡張インターフェースです。
     */
    fun interface TripleItem<OUTPUT : Any> : TripleInput<RecipeInput, ItemStack, ItemStack, ItemStack, OUTPUT> {
        override fun assemble(input: RecipeInput): OUTPUT = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getItemOrEmpty(2))
    }
}
