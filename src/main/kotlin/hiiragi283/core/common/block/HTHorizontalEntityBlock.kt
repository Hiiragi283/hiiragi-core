package hiiragi283.core.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState

open class HTHorizontalEntityBlock(private val type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HorizontalDirectionalBlock(properties),
    HTBlockWithEntity {
    override fun codec(): MapCodec<out HorizontalDirectionalBlock> = throw UnsupportedOperationException()

    final override fun triggerEvent(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        id: Int,
        param: Int,
    ): Boolean {
        super.triggerEvent(state, level, pos, id, param)
        return level.getBlockEntity(pos)?.triggerEvent(id, param) ?: false
    }

    final override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = type
}
