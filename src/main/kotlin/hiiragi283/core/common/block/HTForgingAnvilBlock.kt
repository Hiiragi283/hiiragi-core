package hiiragi283.core.common.block

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.item.insert
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.entity.HTForgingAnvilBlockEntity
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTForgingAnvilBlock(properties: Properties) : HTBasicEntityBlock(HCBlockEntityTypes.FORGING_ANVIL, properties) {
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
        if (hand != InteractionHand.MAIN_HAND) return result
        val anvilBlockEntity: HTForgingAnvilBlockEntity = level.getTypedBlockEntity(pos) ?: return ItemInteractionResult.FAIL
        val anvilSlot: HTBasicItemSlot = anvilBlockEntity.slot
        val anvilStack: ItemStack = anvilSlot.getStack()
        // 手持ちのアイテムが空の場合
        if (stack.isEmpty) {
            // 金床のアイテムが空でない場合，中身をプレイヤーに渡す
            if (!anvilStack.isEmpty) {
                HTItemDropHelper.giveStackTo(player, anvilStack)
                anvilSlot.setStack(ItemStack.EMPTY)
                return ItemInteractionResult.sidedSuccess(!level.isClientSide)
            }
        } else {
            // 手持ちのアイテムがハンマーの場合，レシピを実行する
            if (stack.`is`(HiiragiCoreTags.Items.FORGING_HAMMERS)) {
                // レシピを実行
                if (anvilBlockEntity.process(player)) {
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
                    return ItemInteractionResult.sidedSuccess(!level.isClientSide)
                }
            } else {
                // ハンマーでない場合，手持ちのアイテムをセットする
                val remainder: ItemStack = anvilSlot.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                stack.shrink(stack.count - remainder.count)
                return ItemInteractionResult.sidedSuccess(!level.isClientSide)
            }
        }
        return result
    }
}
