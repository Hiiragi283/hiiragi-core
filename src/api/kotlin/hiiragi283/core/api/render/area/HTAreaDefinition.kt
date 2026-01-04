package hiiragi283.core.api.render.area

import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see appeng.client.render.overlay.IOverlayDataSource
 */
interface HTAreaDefinition {
    fun getArea(): Set<BlockPos>

    fun getSource(): GlobalPos?

    fun getColor(): Int
}
