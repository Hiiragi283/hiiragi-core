package hiiragi283.core.api.event

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.Event

data class HTStepOnBlockEvent(
    val level: Level,
    val pos: BlockPos,
    val state: BlockState,
    val entity: Entity,
) : Event()
