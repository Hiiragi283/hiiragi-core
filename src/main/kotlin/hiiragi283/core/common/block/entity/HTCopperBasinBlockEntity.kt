package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeCaches
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.util.Ior
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.util.HTItemDropHelper
import hiiragi283.core.util.HTStorageHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

class HTCopperBasinBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.COPPER_BASIN, pos, state) {
    lateinit var tank: HTBasicFluidTank
        private set

    override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTBasicFluidTank.create(listener, 4000)
        return object : HTFluidTankHolder {
            override fun getFluidTank(side: Direction?): List<HTFluidTank> = listOf(tank)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    private val emptyingCache: HTRecipeCaches.SingleItem<HTTankEmptyingRecipe> = HTRecipeCaches.SingleItem(HCRecipeLookups.EMPTYING)
    private val fillingCache: HTRecipeCaches.ItemAndFluid<HTTankFillingRecipe> = HTRecipeCaches.ItemAndFluid(HCRecipeLookups.FILLING)
    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(tank) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(tank) }

    fun drainContainer(player: Player, hand: InteractionHand): Boolean {
        val stack: ItemStack = player.getItemInHand(hand)
        if (stack.isEmpty) return false

        val level: Level = player.level()
        val recipe: HTTankEmptyingRecipe = emptyingCache.findFirstRecipe(stack, level) ?: return false

        val rawResult: Ior<ItemStack, FluidStack> = recipe.apply(stack)
        val fluidStack: FluidStack = rawResult.getRight() ?: FluidStack.EMPTY
        if (!fluidStack.isEmpty && fluidOutputHandler.canInsert(fluidStack)) {
            rawResult.getLeft()?.let { HTItemDropHelper.giveStackTo(player, it) }
            fluidOutputHandler.insert(fluidStack)
            stack.consume(1, player)
            return true
        } else {
            return false
        }
    }

    fun fillContainer(player: Player, hand: InteractionHand): Boolean {
        val itemStack: ItemStack = player.getItemInHand(hand)
        val fluidStack: FluidStack = fluidInputHandler.getFluidStack()
        val level: Level = player.level()
        val recipe: HTTankFillingRecipe = fillingCache.findFirstRecipe(itemStack, fluidStack, level) ?: return false

        val filledContainer: ItemStack = recipe.apply(itemStack, fluidStack)
        HTItemDropHelper.giveStackTo(player, filledContainer)
        itemStack.consume(1, player)
        recipe
            .getRequiredAmount(itemStack, fluidStack)
            .second
            .let(fluidInputHandler::consume)
        return true
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStorageHelper.calculateRedstoneLevel(tank)

    //    Sync    //

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        tank.serialize(output)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        tank.deserialize(input)
    }

    //    Tick    //

    private var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Fraction = tank.getLevelAsFraction()
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }
}
