package hiiragi283.core.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.bounds
import hiiragi283.core.api.gui.toRec2i
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.recipe.viewer.widget.HTGhostWidget
import hiiragi283.core.api.recipe.viewer.widget.HTIngredientWidget
import hiiragi283.core.api.util.emptyOptional
import hiiragi283.core.impl.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.impl.gui.widget.HTGuiWidget
import mezz.jei.api.gui.builder.IClickableIngredientFactory
import mezz.jei.api.gui.handlers.IGhostIngredientHandler
import mezz.jei.api.gui.handlers.IGuiContainerHandler
import mezz.jei.api.ingredients.ITypedIngredient
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.runtime.IClickableIngredient
import net.minecraft.client.renderer.Rect2i
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

data object HTWidgetContainerJeiHandler : IGuiContainerHandler<HTWidgetContainerScreen>, IGhostIngredientHandler<HTWidgetContainerScreen> {
    @JvmStatic
    private fun getWidgets(screen: HTWidgetContainerScreen): Sequence<Pair<HTBounds, HTWidget>> = screen
        .children()
        .asSequence()
        .filterIsInstance<HTGuiWidget<*>>()
        .map { it.bounds to it.widget }

    override fun getClickableIngredientUnderMouse(
        builder: IClickableIngredientFactory,
        containerScreen: HTWidgetContainerScreen,
        mouseX: Double,
        mouseY: Double,
    ): Optional<out IClickableIngredient<*>> = getWidgets(containerScreen)
        .filter { (bounds: HTBounds, _) -> bounds.contains(mouseX.toInt(), mouseY.toInt()) }
        .mapNotNull { (bounds: HTBounds, widget: HTWidget) ->
            when (val ingredient = (widget as? HTIngredientWidget)?.getIngredient()) {
                is FluidStack if !ingredient.isEmpty -> builder.createBuilder(NeoForgeTypes.FLUID_STACK, ingredient)
                is ItemStack if !ingredient.isEmpty -> builder.createBuilder(ingredient)
                else -> return@mapNotNull null
            }.buildWithArea(bounds.toRec2i())
        }.firstOrNull()
        ?: emptyOptional()

    //    IGhostIngredientHandler    //

    override fun <I : Any> getTargetsTyped(
        gui: HTWidgetContainerScreen,
        ingredient: ITypedIngredient<I>,
        doStart: Boolean,
    ): List<IGhostIngredientHandler.Target<I>> = getWidgets(gui)
        .mapNotNull { (bounds: HTBounds, widget: HTWidget) ->
            val consumer: HTGhostWidget.GhostIngredientConsumer = (widget as? HTGhostWidget)?.getGhostConsumer() ?: return@mapNotNull null
            bounds to consumer
        }.filter { (_, consumer: HTGhostWidget.GhostIngredientConsumer) ->
            consumer.supportedTarget(ingredient.ingredient)?.let(ingredient.type::getCastIngredient) != null
        }.map { (bounds: HTBounds, consumer: HTGhostWidget.GhostIngredientConsumer) ->
            object : IGhostIngredientHandler.Target<I> {
                override fun getArea(): Rect2i = bounds.toRec2i()

                override fun accept(ingredient: I) {
                    consumer.accept(ingredient)
                }
            }
        }.toList()

    override fun onComplete() {}
}
