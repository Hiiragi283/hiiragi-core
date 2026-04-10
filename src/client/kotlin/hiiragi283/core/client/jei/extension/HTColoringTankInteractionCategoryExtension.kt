package hiiragi283.core.client.jei.extension

import hiiragi283.core.common.data.tank.HTColoringTankInteraction
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.world.item.crafting.Ingredient

data object HTColoringTankInteractionCategoryExtension : HTTankInteractionCategoryExtension<HTColoringTankInteraction> {
    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTColoringTankInteraction, accessor: T) {
        accessor.addIngredients(Ingredient.of(recipe.inputTag))
    }

    override fun <T : IIngredientAcceptor<T>> setFilledContainer(recipe: HTColoringTankInteraction, accessor: T) {
        accessor.addItemStack(recipe.colored)
    }

    override fun <T : IIngredientAcceptor<T>> setFluid(recipe: HTColoringTankInteraction, accessor: T) {
        accessor.addIngredient(NeoForgeTypes.FLUID_STACK, recipe.color.toStack(recipe.amount))
    }
}
