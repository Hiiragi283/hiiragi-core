package hiiragi283.core.common.item

import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.recipe.HCColoringRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.storage.fluid.HTItemFluidTank
import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTPaintBrushItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val player: Player = context.player ?: return InteractionResult.PASS
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val state: BlockState = level.getBlockState(pos)
        val targetStack = ItemStack(state.block)
        val mainStack: ItemStack = context.itemInHand
        return when {
            mainStack.isEmpty || targetStack.isEmpty -> InteractionResult.PASS
            else -> handleRecipe(level, player, mainStack, targetStack) {
                (it.item as? BlockItem)
                    ?.block
                    ?.defaultBlockState()
                    ?.let { state1: BlockState -> level.setBlockAndUpdate(pos, state1) }
            }
        }
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (usedHand != InteractionHand.MAIN_HAND) return super.use(level, player, usedHand)
        val mainStack: ItemStack = player.getItemInHand(usedHand)
        val offStack: ItemStack = player.getItemInHand(InteractionHand.OFF_HAND)
        return when {
            mainStack.isEmpty || offStack.isEmpty -> super.use(level, player, usedHand)
            else -> InteractionResultHolder(
                handleRecipe(level, player, mainStack, offStack) {
                    HTItemDropHelper.giveStackTo(player, it)
                },
                mainStack,
            )
        }
    }

    private fun handleRecipe(
        level: Level,
        player: Player,
        mainStack: ItemStack,
        offStack: ItemStack,
        onSucceeded: (ItemStack) -> Unit,
    ): InteractionResult {
        val tank: HTItemFluidTank = HTFluidCapabilities.getFirstTank(mainStack) as? HTItemFluidTank ?: return InteractionResult.FAIL
        val fluidInput = HTFluidInputHandler(tank)
        val input = HTItemAndFluidRecipeInput(offStack, fluidInput.getFluidStack())
        val recipe: HCColoringRecipe = HCRecipeLookups.COLORING
            .findFirst(level) { it.test(input) }
            ?.recipe
            ?: return InteractionResult.PASS
        // output
        recipe.assemble(input, level.registryAccess()).let(onSucceeded)
        // input
        offStack.consume(1, player)
        recipe.getRequiredAmount(input).getRight()?.let(fluidInput::consume)
        player.setItemInHand(InteractionHand.MAIN_HAND, tank.container)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }
}
