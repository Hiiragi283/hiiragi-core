package hiiragi283.core.api.render.area

import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos

/**
 * 有効範囲を描画するための値を提供するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see appeng.client.render.overlay.IOverlayDataSource
 */
interface HTAreaDefinition {
    /**
     * 描画する範囲を取得します。
     */
    fun getArea(): Set<BlockPos>

    /**
     * 提供元の座標を取得します。
     */
    fun getSource(): GlobalPos?

    /**
     * 描画時の色を取得します。
     */
    fun getColor(): Int
}
