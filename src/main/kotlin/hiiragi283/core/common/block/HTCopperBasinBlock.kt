package hiiragi283.core.common.block

import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.lib.block.HTBasicEntityBlock
import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.world.getTypedBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.transfer.fluid.FluidUtil

open class HTCopperBasinBlock(properties: Properties) : HTBasicEntityBlock(HCBlockEntityTypes.COPPER_BASIN, properties) {
    override fun useItemOn(itemStack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): InteractionResult {
        val result: InteractionResult = super.useItemOn(itemStack, state, level, pos, player, hand, hitResult)
        if (itemStack.isEmpty) return result
        if (!player.isShiftKeyDown) {
            if (!level.isClientSide) {
                val copperBasin: HTCopperBasinBlockEntity = level.getTypedBlockEntity(pos) ?: return InteractionResult.FAIL
                val basinHandler: FluidResourceHandler = copperBasin.getFluidHandler(hitResult.direction) ?: return InteractionResult.FAIL
                val result: InteractionResult.Success = InteractionResult.CONSUME
                when {
                    copperBasin.drainContainer(player, hand) -> return result
                    copperBasin.fillContainer(player, hand) -> return result
                    FluidUtil.interactWithFluidHandler(player, hand, pos, basinHandler) -> return result
                }
            } else {
                return InteractionResult.SUCCESS
            }
        }
        return result
    }
}
