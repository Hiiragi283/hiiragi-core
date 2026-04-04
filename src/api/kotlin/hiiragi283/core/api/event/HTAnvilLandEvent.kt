package hiiragi283.core.api.event

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.FallingBlockEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.Event

/**
 * 金床が着地した時に呼び出されるイベントクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
data class HTAnvilLandEvent(
    val level: Level,
    val pos: BlockPos,
    val oldState: BlockState,
    val newState: BlockState,
    val entity: FallingBlockEntity,
) : Event()
