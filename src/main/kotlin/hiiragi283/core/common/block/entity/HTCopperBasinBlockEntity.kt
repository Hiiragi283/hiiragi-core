package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.common.recipe.handler.HTFluidInputHandler
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeTypes
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

    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(tank) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(tank) }

    fun drainContainer(player: Player, hand: InteractionHand): Boolean {
        val stack: ItemStack = player.getItemInHand(hand)
        val filledContainer: HTItemResourceType = stack.toResource() ?: return false
        val recipe: HTTankInteraction = HCRecipeTypes.TANK_INTERACTION
            .findFirst(level) { recipe: HTTankInteraction -> recipe.canEmptyContainer(filledContainer) }
            ?.second
            ?: return false

        val (emptyContainer: ItemStack, fluidStack: FluidStack) = recipe.emptyContainer(filledContainer)
        if (fluidOutputHandler.canInsert(fluidStack)) {
            HTItemDropHelper.giveStackTo(player, emptyContainer)
            fluidOutputHandler.insert(fluidStack)
            stack.consume(1, player)
            return true
        } else {
            return false
        }
    }

    fun fillContainer(player: Player, hand: InteractionHand): Boolean {
        val stack: ItemStack = player.getItemInHand(hand)
        val emptyContainer: HTItemResourceType = stack.toResource() ?: return false
        val fluid: HTFluidResourceType = fluidInputHandler.getResource() ?: return false
        val recipe: HTTankInteraction = HCRecipeTypes.TANK_INTERACTION
            .findFirst(level) { recipe: HTTankInteraction -> recipe.canFillContainer(emptyContainer, fluid) }
            ?.second
            ?: return false

        val filledContainer: ItemStack = recipe.fillContainer(emptyContainer, fluid)
        HTItemDropHelper.giveStackTo(player, filledContainer)
        stack.consume(1, player)
        fluidInputHandler.consume(recipe.amount)
        return true
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(tank)

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
