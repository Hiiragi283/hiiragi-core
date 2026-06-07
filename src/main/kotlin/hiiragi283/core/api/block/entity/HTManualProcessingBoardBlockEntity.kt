package hiiragi283.core.api.block.entity

import hiiragi283.lib.HTConstants
import hiiragi283.lib.block.entity.HTBlockEntity
import hiiragi283.lib.recipe.base.HTItemToChancedItemsRecipe
import hiiragi283.lib.recipe.cache.HTRecipeCaches
import hiiragi283.lib.recipe.handler.HTItemInputHandler
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.world.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTManualProcessingBoardBlockEntity(lookup: HTRecipeLookup<HTItemToChancedItemsRecipe>, type: BlockEntityType<*>, worldPosition: BlockPos, blockState: BlockState) : HTBlockEntity(type, worldPosition, blockState) {
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

    //    Recipe    //

    protected val cache: HTRecipeCaches.SingleItem<HTItemToChancedItemsRecipe> = HTRecipeCaches.SingleItem(lookup)
    protected val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(slot) }

    fun processItem(player: Player, hand: InteractionHand): Boolean {
        val level: ServerLevel = this.getServerLevel() ?: return false

        val input: ItemStack = slot.getStack()
        val tool: ItemStack = player.getItemInHand(hand)
        if (!canProcessWithTool(tool)) return false
        val recipe: HTItemToChancedItemsRecipe = cache.findFirstRecipe(input, level) ?: return false
        // outputs
        for (stackIn: ItemStack in recipe.assemble(input)) {
            HTItemDropHelper.giveStackTo(player, stackIn)
        }
        // inputs
        tool.hurtAndBreak(1, player, hand.asEquipmentSlot())
        useTransaction { transaction: Transaction ->
            val amount: Int = recipe.getRequiredAmount(input)
            inputHandler.extract(amount, transaction)
            transaction.commit()
        }
        playCompletedSound()
        return true
    }

    protected abstract fun canProcessWithTool(tool: ItemStack): Boolean

    protected abstract fun playCompletedSound()

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

    private var hasItem: Boolean = false

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val hasItem: Boolean = slot.isEmpty()
        if (hasItem != this.hasItem) {
            this.hasItem = hasItem
            return true
        }
        return false
    }
}
