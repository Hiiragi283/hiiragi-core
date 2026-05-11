package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTBiRecipeFactory
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.HTTriRecipeFactory
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

data object HTRecipeFactories {
    //    Single Input    //

    fun interface SingleFluidTo<OUTPUT : Any> : HTRecipeFactory<FluidStack, OUTPUT>

    fun interface SingleItemTo<OUTPUT : Any> : HTRecipeFactory<ItemStack, OUTPUT>

    //    Double Input    //

    fun interface ItemAndFluid<OUTPUT : Any> : HTBiRecipeFactory<ItemStack, FluidStack, OUTPUT>

    fun interface DoubleItem<OUTPUT : Any> : HTBiRecipeFactory<ItemStack, ItemStack, OUTPUT>

    //    Triple Input    //

    fun interface ItemAndDoubleFluid<OUTPUT : Any> : HTTriRecipeFactory<ItemStack, FluidStack, FluidStack, OUTPUT>

    fun interface DoubleItemAndFluid<OUTPUT : Any> : HTTriRecipeFactory<ItemStack, ItemStack, FluidStack, OUTPUT>

    fun interface TripleItem<OUTPUT : Any> : HTTriRecipeFactory<ItemStack, ItemStack, ItemStack, OUTPUT>
}
