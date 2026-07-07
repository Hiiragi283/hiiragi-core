package hiiragi283.core.api.block.entity

import hiiragi283.lib.HTConstants
import hiiragi283.lib.block.entity.HTBlockEntity
import hiiragi283.lib.recipe.base.HTItemToChancedItemsRecipe
import hiiragi283.lib.recipe.cache.HTRecipeCaches
import hiiragi283.lib.recipe.handler.HTItemInputHandler
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.serialization.getItemOrEmpty
import hiiragi283.lib.serialization.putItem
import hiiragi283.lib.transfer.HTHandlerProvider
import hiiragi283.lib.transfer.HTStrictResourceHandler
import hiiragi283.lib.transfer.HTTransferIO
import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.item.ItemResourceHandler
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.item.set
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
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTManualProcessingBoardBlockEntity(lookup: HTRecipeLookup<HTItemToChancedItemsRecipe>, type: BlockEntityType<*>, pos: BlockPos, blockState: BlockState) :
    HTBlockEntity(type, pos, blockState),
    HTHandlerProvider {
    val itemHandler: ItemResourceHandler
        field = object : ItemStacksResourceHandler(1) {
            override fun onContentsChanged(index: Int, previousContents: ItemStack) {
                this@HTManualProcessingBoardBlockEntity.setOnlySave()
            }
        }

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.ITEMS, itemHandler)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.ITEMS, itemHandler)
    }

    //    HTHandlerProvider    //

    override fun getFluidHandler(direction: Direction?): FluidResourceHandler? = null

    override fun getItemHandler(direction: Direction?): ItemResourceHandler = HTStrictResourceHandler(itemHandler) { _: Int ->
        when (direction) {
            Direction.DOWN -> HTTransferIO.NONE
            else -> HTTransferIO.INSERT_ONLY
        }
    }

    //    Sync    //

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        output.putItem(itemHandler.getItemStack(0))
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        itemHandler.set(0, input.getItemOrEmpty())
    }

    //    Ticking    //

    private var hasItem: Boolean = false

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val hasItem: Boolean = ResourceHandlerUtil.isEmpty(itemHandler)
        if (hasItem != this.hasItem) {
            this.hasItem = hasItem
            return true
        }
        return false
    }

    //    Recipe    //

    protected val cache: HTRecipeCaches.SingleItem<HTItemToChancedItemsRecipe> = HTRecipeCaches.SingleItem(lookup)
    protected val inputHandler: HTItemInputHandler = HTItemInputHandler(itemHandler, 0)

    fun processItem(player: Player, hand: InteractionHand): Boolean {
        val input: ItemStack = itemHandler.getItemStack(0)
        val tool: ItemStack = player.getItemInHand(hand)
        if (!canProcessWithTool(tool)) return false
        val recipe: HTItemToChancedItemsRecipe = this.getServerLevel().map { cache.findFirstRecipe(input, it) }.getOrNull() ?: return false
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
}
