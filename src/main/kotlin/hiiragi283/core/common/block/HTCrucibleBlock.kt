package hiiragi283.core.common.block

import hiiragi283.core.impl.block.HTBasicEntityBlock
import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.transfer.fluid.FluidUtil

class HTCrucibleBlock(properties: Properties) : HTBasicEntityBlock(HCBlockEntityTypes.CRUCIBLE, properties) {
    override fun useItemOn(
        itemStack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (!level.isClientSide) {
            if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction)) {
                return InteractionResult.SUCCESS_SERVER
            }
        } else {
            return InteractionResult.CONSUME
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult)
    }
}
