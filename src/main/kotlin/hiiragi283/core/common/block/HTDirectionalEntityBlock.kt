package hiiragi283.core.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.state.BlockState

open class HTDirectionalEntityBlock(private val type: HTDeferredBlockEntityType<*>, properties: Properties) :
    DirectionalBlock(properties),
    HTBlockWithEntity {
    override fun codec(): MapCodec<out DirectionalBlock> = throw UnsupportedOperationException()

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
