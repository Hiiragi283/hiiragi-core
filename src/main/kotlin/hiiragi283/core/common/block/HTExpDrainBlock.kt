package hiiragi283.core.common.block

import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class HTExpDrainBlock(properties: Properties) : HTBasicEntityBlock(HCBlockEntityTypes.EXP_DRAIN, properties) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(
            0.0,
            0.0,
            0.0,
            16.0,
            4.0,
            16.0,
        )
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE
}
