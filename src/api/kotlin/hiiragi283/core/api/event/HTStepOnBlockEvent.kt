package hiiragi283.core.api.event

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.Event

/**
 * アイテムがブロックの上に乗る時に呼び出されるイベントクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
data class HTStepOnBlockEvent(
    val level: Level,
    val pos: BlockPos,
    val state: BlockState,
    val entity: ItemEntity,
) : Event()
