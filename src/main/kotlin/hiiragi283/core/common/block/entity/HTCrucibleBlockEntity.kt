package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.handler.assembleFluid
import hiiragi283.core.api.transfer.holder.HTResourceSlotHolder
import hiiragi283.core.api.transfer.item.stack
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.block.entity.HTBlockEntity
import hiiragi283.core.impl.transfer.fluid.HTBasicFluidTank
import hiiragi283.core.impl.transfer.item.HTBasicItemSlot
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

class HTCrucibleBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.CRUCIBLE, pos, state) {
    //    Transfer    //

    private lateinit var tank: HTBasicFluidTank

    override fun createFluidHandler(listener: HTContentListener): HTResourceSlotHolder<FluidResource> {
        tank = HTBasicFluidTank.output(listener, 8000)
        return object : HTResourceSlotHolder<FluidResource> {
            override fun getSlots(side: Direction?): List<HTBasicFluidTank> = listOf(tank)

            override fun canInsert(side: Direction?): Boolean = false

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    private lateinit var slot: HTBasicItemSlot

    override fun createItemHandler(listener: HTContentListener): HTResourceSlotHolder<ItemResource> {
        slot = HTBasicItemSlot.input(listener)
        return object : HTResourceSlotHolder<ItemResource> {
            override fun getSlots(side: Direction?): List<HTBasicItemSlot> = listOf(slot)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = false
        }
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
    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(slot) }
    private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(tank) }

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
