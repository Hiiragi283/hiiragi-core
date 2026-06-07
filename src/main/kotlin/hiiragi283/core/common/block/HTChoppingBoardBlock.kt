package hiiragi283.core.common.block

import hiiragi283.core.common.block.entity.HTChoppingBoardBlockEntity
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.lib.block.HTBasicEntityBlock
import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.world.HTItemDropHelper
import hiiragi283.lib.world.getTypedBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTChoppingBoardBlock(properties: Properties) : HTBasicEntityBlock(HCBlockEntityTypes.CHOPPING_BOARD, properties) {
    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        val choppingBoard: HTChoppingBoardBlockEntity = level.getTypedBlockEntity(pos) ?: return InteractionResult.FAIL
        val slot: HTBasicItemSlot = choppingBoard.slot
        if (!slot.isEmpty()) {
            val resourceIn: ItemResource = slot.resource
            useTransaction { transaction: Transaction ->
                val extracted: Int = slot.extract(resourceIn, slot.amountAsInt, transaction, HTHandlerAccess.MANUAL)
                if (extracted > 0) {
                    transaction.commit()
                    HTItemDropHelper.giveStackTo(player, resourceIn.toStack(extracted))
                    return InteractionResult.SUCCESS
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult)
    }

    override fun useItemOn(itemStack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): InteractionResult {
        if (itemStack.isEmpty) return InteractionResult.TRY_WITH_EMPTY_HAND
        if (!player.isShiftKeyDown) {
            val choppingBoard: HTChoppingBoardBlockEntity = level.getTypedBlockEntity(pos) ?: return InteractionResult.FAIL
            val slot: HTBasicItemSlot = choppingBoard.slot
            when {
                !slot.isEmpty() && choppingBoard.chopItem(player, hand) -> return InteractionResult.SUCCESS
                else -> {
                    val handAccess: ItemAccess = ItemAccess.forPlayerInteraction(player, hand)
                    val handResource: ItemResource = handAccess.resource
                    useTransaction { transaction: Transaction ->
                        val extracted: Int = handAccess.extract(handResource, handAccess.amount, transaction)
                        val inserted: Int = useTransaction(transaction) { transaction1: Transaction ->
                            val inserted: Int = slot.insert(handResource, extracted, transaction1, HTHandlerAccess.MANUAL)
                            if (inserted > 0) {
                                transaction1.commit()
                            }
                            inserted
                        }
                        if (inserted > 0) {
                            transaction.commit()
                            return InteractionResult.SUCCESS
                        }
                    }
                }
            }
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult)
    }

    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = SHAPE
}
