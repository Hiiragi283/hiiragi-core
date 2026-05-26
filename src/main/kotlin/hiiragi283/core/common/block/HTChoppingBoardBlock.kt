package hiiragi283.core.common.block

import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.lib.recipe.base.HTItemToChancedItemsRecipe
import hiiragi283.lib.world.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTChoppingBoardBlock(properties: Properties) : Block(properties) {
    override fun useItemOn(itemStack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): InteractionResult {
        if (level is ServerLevel && hand == InteractionHand.MAIN_HAND) {
            val mainStack: ItemStack = player.getItemInHand(hand)
            val offStack: ItemStack = player.getItemInHand(InteractionHand.OFF_HAND)
            if (mainStack.`is`(ItemTags.AXES)) {
                val recipe: HTItemToChancedItemsRecipe? = level.getData(HCAttachmentTypes.IN_WORLD_RECIPE_CACHES)
                    .chopping
                    .findFirstRecipe(offStack, level)
                if (recipe != null) {
                    // outputs
                    for (stack: ItemStack in recipe.assemble(offStack)) {
                        HTItemDropHelper.giveStackTo(player, stack)
                    }
                    // inputs
                    mainStack.hurtAndBreak(1, player, hand.asEquipmentSlot())
                    recipe.getRequiredAmount(offStack).let { offStack.consume(it, player) }

                    level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS)
                    return InteractionResult.SUCCESS
                }
            }
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult)
    }
}
