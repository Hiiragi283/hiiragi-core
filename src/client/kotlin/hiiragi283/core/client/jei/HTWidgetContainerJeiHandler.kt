package hiiragi283.core.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.bounds
import hiiragi283.core.api.gui.toRec2i
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.integration.jei.widget.HTIngredientWidget
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.client.gui.widget.HTGuiWidget
import mezz.jei.api.gui.builder.IClickableIngredientFactory
import mezz.jei.api.gui.handlers.IGuiClickableArea
import mezz.jei.api.gui.handlers.IGuiContainerHandler
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.runtime.IClickableIngredient
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

data object HTWidgetContainerJeiHandler : IGuiContainerHandler<HTWidgetContainerScreen> {
    override fun getClickableIngredientUnderMouse(
        builder: IClickableIngredientFactory,
        containerScreen: HTWidgetContainerScreen,
        mouseX: Double,
        mouseY: Double,
    ): Optional<out IClickableIngredient<*>> = containerScreen
        .children()
        .asSequence()
        .filterIsInstance<HTGuiWidget<*>>()
        .map { it.bounds to it.widget }
        .filter { (bounds: HTBounds, _) -> bounds.contains(mouseX.toInt(), mouseY.toInt()) }
        .mapNotNull { (bounds: HTBounds, widget: HTWidget) ->
            when (val ingredient = (widget as? HTIngredientWidget)?.getIngredient()) {
                is FluidStack if !ingredient.isEmpty -> builder.createBuilder(NeoForgeTypes.FLUID_STACK, ingredient)
                is ItemStack if !ingredient.isEmpty -> builder.createBuilder(ingredient)
                else -> return@mapNotNull null
            }.buildWithArea(bounds.toRec2i())
        }.firstOrNull()
        ?: Optional.empty()

    override fun getGuiClickableAreas(
        containerScreen: HTWidgetContainerScreen,
        guiMouseX: Double,
        guiMouseY: Double,
    ): Collection<IGuiClickableArea> = super.getGuiClickableAreas(containerScreen, guiMouseX, guiMouseY)
}
