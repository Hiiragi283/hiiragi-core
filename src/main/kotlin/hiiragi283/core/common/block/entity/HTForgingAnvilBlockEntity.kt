package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeCaches
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.util.HTItemDropHelper
import hiiragi283.core.util.HTStorageHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTForgingAnvilBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.FORGING_ANVIL, pos, state) {
    lateinit var slot: HTBasicItemSlot
        private set

    override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder {
        slot = HTBasicItemSlot.create(listener)
        return object : HTItemSlotHolder {
            override fun getItemSlot(side: Direction?): List<HTItemSlot> = listOf(slot)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = false
        }
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStorageHelper.calculateRedstoneLevel(slot)

    //    Processing    //

    private val cache: HTRecipeCaches.DoubleItem<HTDoubleItemToItemRecipe> =
        HTRecipeCaches.DoubleItem(HCRecipeLookups.FORGING)
    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(slot) }

    fun process(player: Player): Boolean {
        val stack: ItemStack = inputHandler.getItemStack()
        val stack1: ItemStack = player.getItemInHand(InteractionHand.OFF_HAND)
        val level: Level = player.level()
        val recipe: HTDoubleItemToItemRecipe = cache.findFirstRecipe(stack, stack1, level) ?: return false
        // outputs
        recipe.assemble(stack, stack1).let(HTItemDropHelper::giveStackTo.partially1(player))
        // inputs
        val (primaryAmount: Int, secondaryAmount: Int) = recipe.getRequiredAmount(stack, stack1) // TODO
        inputHandler.consume(primaryAmount)
        stack1.shrink(secondaryAmount)
        // sound
        playSound(SoundEvents.ANVIL_LAND)
        return true
    }

    //    Sync    //

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        slot.serialize(output)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        slot.deserialize(input)
    }

    //    Tick    //

    private var oldResource: HTItemResourceType? = null

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val resource: HTItemResourceType? = slot.getResource()
        if (resource != oldResource) {
            this.oldResource = resource
            return true
        }
        return false
    }
}
