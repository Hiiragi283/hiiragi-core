package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

interface HCForgingRecipe : HTProcessingRecipe<HTDoubleRecipeInput> {
    companion object {
        @JvmField
        val RESULT_RANGE: IntRange = 1 until 9
    }

    fun testBase(stack: ItemStack): Boolean

    fun testAddition(stack: ItemStack): Boolean

    fun assembleItems(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): List<ItemStack>

    override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return testBase(first) && testAddition(second)
    }

    @Deprecated("Use 'assembles(HolderLookup.Provider)' instead")
    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        assembleItems(input, registries).firstOrNull() ?: ItemStack.EMPTY

    //    Serializable    //

    interface Serializable :
        HCForgingRecipe,
        HTProcessingRecipe.Serializable<HTDoubleRecipeInput> {
        override fun test(input: HTDoubleRecipeInput): Boolean = super.test(input)

        @Suppress("DEPRECATION")
        override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack = super.assemble(input, registries)
    }
}
