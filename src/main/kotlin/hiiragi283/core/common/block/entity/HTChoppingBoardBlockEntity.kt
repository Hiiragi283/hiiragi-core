package hiiragi283.core.common.block.entity

import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeLookups
import hiiragi283.lib.HTConstants
import hiiragi283.lib.block.entity.HTBlockEntity
import hiiragi283.lib.recipe.base.HTItemToChancedItemsRecipe
import hiiragi283.lib.recipe.cache.HTRecipeCaches
import hiiragi283.lib.recipe.handler.HTItemInputHandler
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.world.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import org.apache.commons.lang3.math.Fraction

class HTChoppingBoardBlockEntity(worldPosition: BlockPos, blockState: BlockState) : HTBlockEntity(HCBlockEntityTypes.CHOPPING_BOARD.get(), worldPosition, blockState) {
    lateinit var slot: HTBasicItemSlot
        private set

    override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot> {
        slot = HTBasicItemSlot.input(listener, canInsert = { resource: ItemResource ->
            val level: ServerLevel = this.getServerLevel() ?: return@input false
            cache.findFirstRecipe(resource, level) != null
        })
        return object : HTResourceSlotHolder<HTItemSlot> {
            override fun getSlots(side: Direction?): List<HTItemSlot> = listOf(slot)

            override fun canInsert(side: Direction?): Boolean = side != Direction.DOWN

            override fun canExtract(side: Direction?): Boolean = false
        }
    }

    private val cache: HTRecipeCaches.SingleItem<HTItemToChancedItemsRecipe> = HTRecipeCaches.SingleItem(HCRecipeLookups.CHOPPING)
    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(slot) }

    fun chopItem(player: Player, hand: InteractionHand): Boolean {
        val level: ServerLevel = this.getServerLevel() ?: return false

        val stack: ItemStack = inputHandler.getItemStack()
        val axeStack: ItemStack = player.getItemInHand(hand)
        if (axeStack.`is`(ItemTags.AXES)) {
            val recipe: HTItemToChancedItemsRecipe = cache.findFirstRecipe(stack, level) ?: return false
            // outputs
            for (stackIn: ItemStack in recipe.assemble(stack)) {
                HTItemDropHelper.giveStackTo(player, stackIn)
            }
            // inputs
            axeStack.hurtAndBreak(1, player, hand.asEquipmentSlot())
            useTransaction { transaction: Transaction ->
                val amount: Int = recipe.getRequiredAmount(stack)
                inputHandler.extract(amount, transaction)
                transaction.commit()
            }

            playSound(SoundEvents.AXE_STRIP)
            return true
        }
        return false
    }

    //    Sync    //

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        output.putChild(HTConstants.ITEM, slot)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        input.readChild(HTConstants.ITEM, slot)
    }

    //    Ticking    //

    private var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Fraction = slot.getFilledLevel(slot.resource)
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }
}
