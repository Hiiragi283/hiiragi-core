package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.HCForgingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.util.HTItemDropHelper
import hiiragi283.core.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
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

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(slot)

    //    Processing    //

    private val cache: HTLookupRecipeCache<HTDoubleRecipeInput, HCForgingRecipe> = HTLookupRecipeCache.forRecipe(HCRecipeLookups.FORGING)
    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(slot) }
    
    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    fun process(player: Player): Boolean {
        val stack: ItemStack = inputHandler.getItemStack()
        val stack1: ItemStack = player.getItemInHand(InteractionHand.OFF_HAND)
        val input = HTDoubleRecipeInput(stack, stack1)
        if (input.isEmpty) return false
        val level: Level = player.level()
        val recipe: HCForgingRecipe = cache.getFirstRecipe(input, level) ?: return false
        // outputs
        recipe.assembleItems(input, level.registryAccess()).forEach(HTItemDropHelper::giveStackTo.partially1(player))
        // inputs
        input.let(recipe::getBaseAmount).let(inputHandler::consume)
        input.let(recipe::getAdditionAmount).let(stack1::shrink)
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
