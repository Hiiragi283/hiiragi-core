package hiiragi283.core.api.integration.emi.widget

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer

/**
 * @see mekanism.client.recipe_viewer.interfaces.IRecipeViewerGhostTarget
 */
interface HTGhostWidget {
    fun getGhostConsumer(): GhostIngredientConsumer?

    interface GhostIngredientConsumer : Consumer<Any> {
        fun supportedTarget(ingredient: Any): Any?
    }

    fun interface ItemConsumer : GhostIngredientConsumer {
        override fun supportedTarget(ingredient: Any): ItemStack? = when (ingredient) {
            is ItemStack if !ingredient.isEmpty -> ingredient
            else -> null
        }
    }

    fun interface FluidConsumer : GhostIngredientConsumer {
        override fun supportedTarget(ingredient: Any): FluidStack? = when (ingredient) {
            is FluidStack if !ingredient.isEmpty -> ingredient
            else -> null
        }
    }
}
