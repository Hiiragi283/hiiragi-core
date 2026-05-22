package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTBiRecipeFactory
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.HTTriRecipeFactory
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance

data object HTRecipeFactories {
    //    Single Input    //

    fun interface SingleFluidTo<OUTPUT : Any> : HTRecipeFactory<FluidInstance, OUTPUT>

    fun interface SingleItemTo<OUTPUT : Any> : HTRecipeFactory<ItemInstance, OUTPUT>

    //    Double Input    //

    fun interface ItemAndFluid<OUTPUT : Any> : HTBiRecipeFactory<ItemInstance, FluidInstance, OUTPUT>

    fun interface DoubleItem<OUTPUT : Any> : HTBiRecipeFactory<ItemInstance, ItemInstance, OUTPUT>

    //    Triple Input    //

    fun interface ItemAndDoubleFluid<OUTPUT : Any> : HTTriRecipeFactory<ItemInstance, FluidInstance, FluidInstance, OUTPUT>

    fun interface DoubleItemAndFluid<OUTPUT : Any> : HTTriRecipeFactory<ItemInstance, ItemInstance, FluidInstance, OUTPUT>

    fun interface TripleItem<OUTPUT : Any> : HTTriRecipeFactory<ItemInstance, ItemInstance, ItemInstance, OUTPUT>
}
