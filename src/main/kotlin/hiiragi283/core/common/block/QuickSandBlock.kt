package hiiragi283.core.common.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.FallingBlockEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

open class QuickSandBlock(properties: Properties) : Block(properties) {
    companion object {
        @JvmStatic
        private val FALLING_COLLISION_SHAPE: VoxelShape = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9, 1.0)
    }

    override fun skipRendering(state: BlockState, neighborState: BlockState, direction: Direction): Boolean = neighborState.`is`(this) || super.skipRendering(state, neighborState, direction)

    override fun getEntityInsideCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, entity: Entity): VoxelShape {
        val collisionShape: VoxelShape = this.getCollisionShape(state, level, pos, CollisionContext.of(entity))
        return if (collisionShape.isEmpty) super.getEntityInsideCollisionShape(state, level, pos, entity) else collisionShape
    }

    override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        if (context.isPlacement && context is EntityCollisionContext) {
            val entity: Entity? = context.entity
            if (entity != null) {
                if (entity.fallDistance > 2.5) {
                    return FALLING_COLLISION_SHAPE
                }
                if (entity is FallingBlockEntity && context.isAbove(Shapes.block(), pos, false) && !context.isDescending) {
                    return super.getCollisionShape(state, level, pos, context)
                }
            }
        }
        return Shapes.empty()
    }

    override fun getVisualShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = Shapes.empty()
}
