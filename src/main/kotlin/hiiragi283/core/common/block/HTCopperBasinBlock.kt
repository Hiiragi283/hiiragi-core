package hiiragi283.core.common.block

import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.FluidUtil

open class HTCopperBasinBlock(properties: Properties) : HTBasicEntityBlock(HCBlockEntityTypes.COPPER_BASIN, properties) {
    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val result: ItemInteractionResult = super.useItemOn(stack, state, level, pos, player, hand, hitResult)
        if (stack.isEmpty) return result
        if (!player.isShiftKeyDown) {
            if (!level.isClientSide) {
                val tankEntity: HTCopperBasinBlockEntity = level.getTypedBlockEntity(pos) ?: return ItemInteractionResult.FAIL
                val result: ItemInteractionResult = ItemInteractionResult.CONSUME
                when {
                    tankEntity.drainContainer(player, hand) -> return result
                    tankEntity.fillContainer(player, hand) -> return result
                    FluidUtil.interactWithFluidHandler(player, hand, tankEntity) -> return result
                }
            } else {
                return ItemInteractionResult.SUCCESS
            }
        }
        return result
    }
}
