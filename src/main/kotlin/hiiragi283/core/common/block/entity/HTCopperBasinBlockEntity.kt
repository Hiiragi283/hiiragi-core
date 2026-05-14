package hiiragi283.core.common.block.entity

import hiiragi283.core.api.recipe.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.core.setup.HCRecipeLookups
import hiiragi283.lib.HTConstants
import hiiragi283.lib.block.entity.HTBlockEntity
import hiiragi283.lib.entity.serverLevel
import hiiragi283.lib.recipe.cache.HTRecipeCaches
import hiiragi283.lib.recipe.handler.HTFluidInputHandler
import hiiragi283.lib.recipe.handler.HTFluidOutputHandler
import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.getFilledLevel
import hiiragi283.lib.transfer.item.ItemResourceHandler
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
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler
import org.apache.commons.lang3.math.Fraction

class HTCopperBasinBlockEntity(worldPosition: BlockPos, blockState: BlockState) : HTBlockEntity(TODO(), worldPosition, blockState) {
    private val fluidHandler = FluidStacksResourceHandler(1, 4000)

    private val emptyingCache: HTRecipeCaches.SingleItem<HTTankEmptyingRecipe> = HTRecipeCaches.SingleItem(HCRecipeLookups.EMPTYING)
    private val fillingCache: HTRecipeCaches.ItemAndFluid<HTTankFillingRecipe> = HTRecipeCaches.ItemAndFluid(HCRecipeLookups.FILLING)
    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(fluidHandler, 0) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(fluidHandler, 0) }

    fun drainContainer(player: Player, hand: InteractionHand): Boolean {
        val stack: ItemStack = player.getItemInHand(hand)

        val level: ServerLevel = player.serverLevel() ?: return false
        val recipe: HTTankEmptyingRecipe = emptyingCache.findFirstRecipe(stack, level) ?: return false

        val (item: ItemStack, fluid: FluidStack) = recipe.assemble(stack)
        if (fluidOutputHandler.canInsert(fluid, null)) {
            HTItemDropHelper.giveStackTo(player, item)
            fluidOutputHandler.insert(fluid, null)
            stack.consume(1, player)
            return true
        } else {
            return false
        }
    }

    fun fillContainer(player: Player, hand: InteractionHand): Boolean {
        val itemStack: ItemStack = player.getItemInHand(hand)
        val fluidStack: FluidStack = fluidInputHandler.getStack()
        val level: ServerLevel = player.serverLevel() ?: return false
        val recipe: HTTankFillingRecipe = fillingCache.findFirstRecipe(itemStack, fluidStack, level) ?: return false

        HTItemDropHelper.giveStackTo(player, recipe.assemble(itemStack, fluidStack))
        recipe
            .getRequiredAmount(itemStack, fluidStack)
            .let { (first: Int, second: Int) ->
                itemStack.consume(first, player)
                fluidInputHandler.consume(second, null)
            }
        return true
    }

    //    Save & Load    //

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.FLUIDS, fluidHandler)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.FLUIDS, fluidHandler)
    }

    //    Sync    //

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        output.putChild(HTConstants.FLUIDS, fluidHandler)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        input.readChild(HTConstants.FLUIDS, fluidHandler)
    }

    //    Ticking    //

    private var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Fraction = fluidHandler.getFilledLevel(0)
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }

    override fun getItemHandler(direction: Direction?): ItemResourceHandler? = null

    override fun getFluidHandler(direction: Direction?): FluidResourceHandler = fluidHandler

    override fun getEnergyStorage(direction: Direction?): EnergyHandler? = null
}
