package hiiragi283.core.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.integration.jei.widget.HTGhostWidget
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import mezz.jei.api.gui.handlers.IGhostIngredientHandler
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.ingredients.ITypedIngredient
import net.minecraft.client.renderer.Rect2i

object HTGhostIngredientHandler : IGhostIngredientHandler<HTWidgetContainerScreen> {
    override fun <I : Any> getTargetsTyped(
        gui: HTWidgetContainerScreen,
        ingredient: ITypedIngredient<I>,
        doStart: Boolean,
    ): List<IGhostIngredientHandler.Target<I>> = gui
        .children()
        .filterIsInstance<HTWidgetContainerScreen.WidgetWrapper<*>>()
        .mapNotNull { wrapper: HTWidgetContainerScreen.WidgetWrapper<*> ->
            val bounds: HTBounds = wrapper.bounds
            val widget: HTWidget = wrapper.widget
            if (widget !is HTGhostWidget) return@mapNotNull null
            val ghostConsumer: HTGhostWidget.GhostIngredientConsumer = widget.getGhostConsumer() ?: return@mapNotNull null
            val type: IIngredientType<I> = ingredient.type
            val matchType: Boolean = ghostConsumer.supportedTarget(ingredient.ingredient)?.let(type::getCastIngredient) != null
            if (!matchType) return@mapNotNull null
            object : IGhostIngredientHandler.Target<I> {
                override fun getArea(): Rect2i {
                    val (x: Int, y: Int, width: Int, height: Int) = bounds
                    return Rect2i(x, y, width, height)
                }

                override fun accept(ingredient: I) {
                    ghostConsumer.accept(ingredient)
                }
            }
        }

    override fun onComplete() {}
}
