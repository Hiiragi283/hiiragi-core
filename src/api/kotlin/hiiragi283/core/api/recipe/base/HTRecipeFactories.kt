package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTBiRecipeFactory
import hiiragi283.core.api.recipe.HTRecipeFactory
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

data object HTRecipeFactories {
    //    Single Input    //

    fun interface SingleFluidTo<OUTPUT : Any> : HTRecipeFactory<FluidStack, OUTPUT>

    fun interface SingleItemTo<OUTPUT : Any> : HTRecipeFactory<ItemStack, OUTPUT>

    //    Double Input    //

    fun interface ItemAndFluid<OUTPUT : Any> : HTBiRecipeFactory<ItemStack, FluidStack, OUTPUT>

    fun interface DoubleItem<OUTPUT : Any> : HTBiRecipeFactory<ItemStack, ItemStack, OUTPUT>
}
