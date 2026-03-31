package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.handler.assembleFluid
import hiiragi283.core.api.transfer.FluidResourceHandler
import hiiragi283.core.api.transfer.HTResourceHandler
import hiiragi283.core.api.transfer.ItemResourceHandler
import hiiragi283.core.api.transfer.item.stack
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.block.entity.HTBlockEntity
import hiiragi283.core.impl.transfer.HTSlotInfo
import hiiragi283.core.impl.transfer.fluid.HTBasicFluidTank
import hiiragi283.core.impl.transfer.item.HTBasicItemSlot
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class HTCrucibleBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.CRUCIBLE, pos, state) {
    //    Transfer    //

    private val slot: HTBasicItemSlot = HTBasicItemSlot.input(::setOnlySave)
    private val tank: HTBasicFluidTank = HTBasicFluidTank.output(::setOnlySave, 8000)

    override fun getInternalItemHandler(): ItemResourceHandler = HTResourceHandler { listOf(slot) }

    override fun getItemInfoFrom(index: Int): HTSlotInfo = when (index) {
        0 -> HTSlotInfo.INPUT
        else -> HTSlotInfo.NONE
    }

    override fun getInternalFluidHandler(): FluidResourceHandler = HTResourceHandler { listOf(tank) }

    override fun getFluidInfoFrom(index: Int): HTSlotInfo = when (index) {
        0 -> HTSlotInfo.OUTPUT
        else -> HTSlotInfo.NONE
    }

    //    Sync    //

    override fun initReducedUpdateTag(output: ValueOutput) {
        super.initReducedUpdateTag(output)
        output.putChild(HTConst.ITEM, slot)
        output.putChild(HTConst.FLUID, tank)
    }

    override fun handleUpdateTag(input: ValueInput) {
        super.handleUpdateTag(input)
        input.readChild(HTConst.ITEM, slot)
        input.readChild(HTConst.FLUID, tank)
    }

    //    Processing    //

    private val cache: HTRecipeCache<SingleRecipeInput, HCMeltingRecipe> = HCRecipeTypes.MELTING.createCache()
    private val inputHandler = HTItemInputHandler(slot)
    private val outputHandler: HTFluidOutputHandler = HTFluidOutputHandler.single(tank)

    private val handler: HTRecipeHandler<SingleRecipeInput, HCMeltingRecipe> = HTProgressHandler.create {
        recipeFinder = finder@{ level: ServerLevel, _ ->
            val input = SingleRecipeInput(slot.stack)
            if (input.isEmpty) return@finder null
            cache.getFirstRecipe(input, level)?.let { HTHandledRecipe.create(input, it) }
        }
        maxProgressGetter = { it.recipe.time }
        progressGetter = { level, pos -> 1 }
        canComplete = { _, _, recipe: HTHandledRecipe<SingleRecipeInput, HCMeltingRecipe> ->
            outputHandler.canInsert(recipe.assembleFluid())
        }
        onComplete = { level, _, recipe: HTHandledRecipe<SingleRecipeInput, HCMeltingRecipe> ->
            // output
            outputHandler.insert(recipe.assembleFluid())
            // input
            inputHandler.consume(1)
            // sound
            playSound(SoundEvents.LAVA_POP)
        }
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = handler.tick(level, pos)
}
