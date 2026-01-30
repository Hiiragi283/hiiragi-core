package hiiragi283.core.common.block

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.block.entity.HTTestBlockEntity
import hiiragi283.core.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTTestBlock(properties: Properties) :
    HTBasicEntityBlock(HCBlockEntityTypes.TEST, properties),
    HTBlockWidgetHolderContext.Factory {
    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (player is ServerPlayer) {
            HTBlockWidgetHolderContext.openMenu(player, pos)
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun setup(context: HTBlockWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        (context.blockEntity as? HTTestBlockEntity)?.setup(widgetHolder)
    }
}
