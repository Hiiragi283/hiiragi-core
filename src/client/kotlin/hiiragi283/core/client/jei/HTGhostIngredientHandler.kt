package hiiragi283.core.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.bounds
import hiiragi283.core.api.gui.toRec2i
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.integration.jei.widget.HTGhostWidget
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.client.gui.widget.HTGuiWidget
import mezz.jei.api.gui.handlers.IGhostIngredientHandler
import mezz.jei.api.ingredients.ITypedIngredient
import net.minecraft.client.renderer.Rect2i

object HTGhostIngredientHandler : IGhostIngredientHandler<HTWidgetContainerScreen> {
    override fun <I : Any> getTargetsTyped(
        gui: HTWidgetContainerScreen,
        ingredient: ITypedIngredient<I>,
        doStart: Boolean,
    ): List<IGhostIngredientHandler.Target<I>> = gui
        .children()
        .asSequence()
        .filterIsInstance<HTGuiWidget<*>>()
        .map { it.bounds to it.widget }
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
