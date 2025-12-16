package hiiragi283.core.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.entity.HTDryingLackBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState

class HTDryingLackBlock(properties: Properties) :
    HorizontalDirectionalBlock(properties),
    HTBlockWithEntity {
    override fun codec(): MapCodec<out HorizontalDirectionalBlock> = throw UnsupportedOperationException()

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        if (!state.`is`(newState.block)) {
            val dryingLack: HTDryingLackBlockEntity? = level.getTypedBlockEntity(pos)
            if (level is ServerLevel && dryingLack != null) {
                HTItemDropHelper.dropStackAt(level, pos, dryingLack.slot.getStack())
                state.block.popExperience(level, pos, dryingLack.storedExp.toInt())
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = HCBlockEntityTypes.DRYING_LACK
}
