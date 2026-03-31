package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.handler.assembleFluid
import hiiragi283.core.api.transfer.FluidResourceHandler
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.ItemResourceHandler
import hiiragi283.core.api.transfer.fluid.StrictFluidStacksResourceHandler
import hiiragi283.core.api.transfer.getStack
import hiiragi283.core.api.transfer.item.StrictItemStacksResourceHandler
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.block.entity.HTBlockEntity
import hiiragi283.core.impl.transfer.HTSlotInfo
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack

class HTCrucibleBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.CRUCIBLE, pos, state) {
    //    Transfer    //

    private val itemHandler: StrictItemStacksResourceHandler = object : StrictItemStacksResourceHandler(1) {
        override fun canInsert(index: Int, access: HTHandlerAccess): Boolean = index == 0

        override fun canExtract(index: Int, access: HTHandlerAccess): Boolean = when (index) {
            0 -> HTHandlerAccess.NOT_EXTERNAL.test(access)
            else -> false
        }

        override fun onContentsChanged(index: Int, previousContents: ItemStack) {
            super.onContentsChanged(index, previousContents)
            setOnlySave()
        }
    }
    private val fluidHandler: StrictFluidStacksResourceHandler = object : StrictFluidStacksResourceHandler(1, 8000) {
        override fun canInsert(index: Int, access: HTHandlerAccess): Boolean = when (index) {
            0 -> HTHandlerAccess.INTERNAL_ONLY.test(access)
            else -> false
        }

        override fun canExtract(index: Int, access: HTHandlerAccess): Boolean = index == 0

        override fun onContentsChanged(index: Int, previousContents: FluidStack) {
            super.onContentsChanged(index, previousContents)
            setOnlySave()
        }
    }

    override fun getInternalItemHandler(): ItemResourceHandler = itemHandler

    override fun getItemInfoFrom(index: Int): HTSlotInfo = when (index) {
        0 -> HTSlotInfo.INPUT
        else -> HTSlotInfo.NONE
    }

    override fun getInternalFluidHandler(): FluidResourceHandler = fluidHandler

    override fun getFluidInfoFrom(index: Int): HTSlotInfo = when (index) {
        0 -> HTSlotInfo.OUTPUT
        else -> HTSlotInfo.NONE
    }

    //    Sync    //

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConst.ITEM, itemHandler)
        output.putChild(HTConst.FLUID, fluidHandler)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConst.ITEM, itemHandler)
        input.readChild(HTConst.FLUID, fluidHandler)
    }

    override fun initReducedUpdateTag(output: ValueOutput) {
        super.initReducedUpdateTag(output)
        output.putChild(HTConst.ITEM, itemHandler)
        output.putChild(HTConst.FLUID, fluidHandler)
    }

    override fun handleUpdateTag(input: ValueInput) {
        super.handleUpdateTag(input)
        input.readChild(HTConst.ITEM, itemHandler)
        input.readChild(HTConst.FLUID, fluidHandler)
    }

    //    Processing    //

    private val cache: HTRecipeCache<SingleRecipeInput, HCMeltingRecipe> = HCRecipeTypes.MELTING.createCache()
    private val inputHandler = HTItemInputHandler(itemHandler, 0, itemHandler::set)
    private val outputHandler: HTFluidOutputHandler = HTFluidOutputHandler.single(fluidHandler, 0)

    private val handler: HTRecipeHandler<SingleRecipeInput, HCMeltingRecipe> = HTProgressHandler.create {
        recipeFinder = finder@{ level: ServerLevel, _ ->
            val input = SingleRecipeInput(itemHandler.getStack(0))
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
