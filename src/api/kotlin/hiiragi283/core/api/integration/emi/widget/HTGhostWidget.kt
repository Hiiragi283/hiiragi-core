package hiiragi283.core.api.integration.emi.widget

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer

/**
 * レシピビューワーからオブジェクトをドラッグ&ドロップ可能なウィジェットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.client.recipe_viewer.interfaces.IRecipeViewerGhostTarget
 */
interface HTGhostWidget {
    /**
     * 保持している[GhostIngredientConsumer]を取得します。
     */
    fun getGhostConsumer(): GhostIngredientConsumer?

    /**
     * ドラッグ&ドロップの処理を担うインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    interface GhostIngredientConsumer : Consumer<Any> {
        /**
         * 指定した[ingredient]から，対応するオブジェクトに変換します。
         * @return 対応していない場合は`null`
         */
        fun supportedTarget(ingredient: Any): Any?
    }

    /**
     * [ItemStack]向けの[GhostIngredientConsumer]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    fun interface ItemConsumer : GhostIngredientConsumer {
        override fun supportedTarget(ingredient: Any): ItemStack? = when (ingredient) {
            is ItemStack if !ingredient.isEmpty -> ingredient
            else -> null
        }
    }

    /**
     * [FluidStack]向けの[GhostIngredientConsumer]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    fun interface FluidConsumer : GhostIngredientConsumer {
        override fun supportedTarget(ingredient: Any): FluidStack? = when (ingredient) {
            is FluidStack if !ingredient.isEmpty -> ingredient
            else -> null
        }
    }
}
