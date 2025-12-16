package hiiragi283.core.common.block.entity

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.stack.toImmutable
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.core.common.storage.item.HTSingleItemHandler
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

class HTDryingLackBlockEntity(pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(HCBlockEntityTypes.DRYING_LACK, pos, state),
    HTHandlerProvider {
    companion object {
        @JvmStatic
        fun serverTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTDryingLackBlockEntity,
        ) {
            val result: Boolean = blockEntity.tick(level, pos)
            when (result) {
                true -> return
                false -> blockEntity.isDrying = false
            }
        }
    }

    private var progress: Int = 0
    private var maxProgress: Int = 0
    private var isDrying = false
    private var isComplete = false
    private var stack: ItemStack = ItemStack.EMPTY
    private val recipeCache: HTRecipeCache<HTRecipeInput, HTDryingRecipe> = HTFinderRecipeCache(HCRecipeTypes.DRYING)

    private fun tick(level: Level, pos: BlockPos): Boolean {
        if (isComplete) {
            if (stack.isEmpty) {
                return false
            }
            isComplete = false
        }
        val input: HTRecipeInput = HTRecipeInput.create(pos) { items += stack.toImmutable() } ?: run {
            isComplete = true
            return false
        }
        val recipe: HTDryingRecipe = recipeCache.getFirstRecipe(input, level) ?: run {
            isComplete = true
            return false
        }
        isDrying = true
        if (maxProgress != recipe.time) {
            maxProgress = recipe.time
        }
        if (progress < maxProgress) {
            progress++
            return true
        } else {
            val result: ItemStack = recipe.assembleItem(input, level.registryAccess())?.unwrap() ?: return false
            stack = result
            isDrying = false
            isComplete = true
            progress = 0
            return true
        }
    }

    //    HTHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandler = LackItemHandler(direction)

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = null

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null

    private inner class LackItemHandler(private val side: Direction?) : HTSingleItemHandler(this::stack) {
        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = when {
            side == Direction.UP -> super.insertItem(slot, stack, simulate)
            else -> stack
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = when {
            side == Direction.DOWN && !isDrying && isComplete -> super.extractItem(slot, amount, simulate)
            else -> ItemStack.EMPTY
        }

        override fun getSlotLimit(slot: Int): Int = when {
            side == Direction.UP -> 1
            else -> super.getSlotLimit(slot)
        }

        override fun onContentsChanged() {
            setOnlySave()
        }
    }
}
