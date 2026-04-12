package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.extension.HTTankInteractionCategoryExtension
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.common.data.tank.HTOminousTankInteraction
import hiiragi283.core.setup.HCFluids
import mezz.jei.api.gui.builder.IIngredientAcceptor
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Items

data object HTOminousTankInteractionCategoryExtension : HTTankInteractionCategoryExtension<HTOminousTankInteraction> {
    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTOminousTankInteraction, accessor: T) {
        accessor.addItemLike(Items.GLASS_BOTTLE)
    }

    override fun <T : IIngredientAcceptor<T>> setFilledContainer(recipe: HTOminousTankInteraction, accessor: T) {
        accessor.addItemStack(createItemStack(Items.OMINOUS_BOTTLE, DataComponents.OMINOUS_BOTTLE_AMPLIFIER, recipe.amplifier))
    }

    override fun <T : IIngredientAcceptor<T>> setFluid(recipe: HTOminousTankInteraction, accessor: T) {
        accessor.addFluidStack(HCFluids.OMINOUS_FLUX.toStack(recipe.amount), false)
    }
}
