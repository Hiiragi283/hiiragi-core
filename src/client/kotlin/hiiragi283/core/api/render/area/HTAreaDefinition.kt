package hiiragi283.core.api.render.area

import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos

/**
 * 有効範囲を描画するための値を提供するインターフェースです。
 *
 * 参照 : [AppliedEnergistics2 - IOverlayDataSource](https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/1.21.1/src/main/java/appeng/client/render/overlay/IOverlayDataSource.java)
 * @author Hiiragi Tsubasa
 * @since 0.5.0
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
