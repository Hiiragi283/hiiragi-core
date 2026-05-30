package hiiragi283.core.common.block.entity

import hiiragi283.core.api.recipe.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeLookups
import hiiragi283.lib.HTConstants
import hiiragi283.lib.block.entity.HTBlockEntity
import hiiragi283.lib.entity.serverLevel
import hiiragi283.lib.recipe.cache.HTRecipeCaches
import hiiragi283.lib.recipe.handler.HTFluidInputHandler
import hiiragi283.lib.recipe.handler.HTFluidOutputHandler
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.world.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction
import org.apache.commons.lang3.math.Fraction

class HTCopperBasinBlockEntity(worldPosition: BlockPos, blockState: BlockState) : HTBlockEntity(HCBlockEntityTypes.COPPER_BASIN.get(), worldPosition, blockState) {
    lateinit var tank: HTBasicFluidTank
        private set

    override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank> {
        tank = HTBasicFluidTank.create(4000, listener)
        return object : HTResourceSlotHolder<HTFluidTank> {
            override fun getSlots(side: Direction?): List<HTFluidTank> = listOf(tank)

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

        val level: ServerLevel = player.serverLevel() ?: return false
        val recipe: HTTankEmptyingRecipe = emptyingCache.findFirstRecipe(stack, level) ?: return false

        val (item: ItemStack, fluid: FluidStack) = recipe.assemble(stack)
        useTransaction { transaction: Transaction ->
            if (fluidOutputHandler.insert(fluid, transaction).isSuccess) {
                transaction.commit()
                HTItemDropHelper.giveStackTo(player, item)
                stack.consume(1, player)
                return true
            }
        }
        return false
    }

    fun fillContainer(player: Player, hand: InteractionHand): Boolean {
        val itemStack: ItemStack = player.getItemInHand(hand)
        val fluidStack: FluidStack = fluidInputHandler.getFluidStack()
        val level: ServerLevel = player.serverLevel() ?: return false
        val recipe: HTTankFillingRecipe = fillingCache.findFirstRecipe(itemStack, fluidStack, level) ?: return false

        HTItemDropHelper.giveStackTo(player, recipe.assemble(itemStack, fluidStack))
        recipe
            .getRequiredAmount(itemStack, fluidStack)
            .let { (first: Int, second: Int) ->
                useTransaction { transaction: Transaction ->
                    if (fluidInputHandler.extract(second, transaction).isSuccess) {
                        transaction.commit()
                        itemStack.consume(first, player)
                    }
                }
            }
        return true
    }

    //    Save & Load    //

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.FLUIDS, tank) // TODO
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.FLUIDS, tank) // TODO
    }

    //    Sync    //

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        output.putChild(HTConstants.FLUIDS, tank)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        input.readChild(HTConstants.FLUIDS, tank)
    }

    //    Ticking    //

    private var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Fraction = tank.getFilledLevel(tank.resource)
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }
}
