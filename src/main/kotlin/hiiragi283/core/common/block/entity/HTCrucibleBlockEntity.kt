package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.handler.assembleFluid
import hiiragi283.core.api.transfer.useTransaction
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.block.entity.HTBlockEntity
import hiiragi283.core.impl.transfer.LimitingResourceHandler
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler

class HTCrucibleBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(HCBlockEntityTypes.CRUCIBLE, pos, state) {
    //    Transfer    //

    private val fluids: FluidStacksResourceHandler = object : FluidStacksResourceHandler(1, 8000) {
        override fun onContentsChanged(index: Int, previousContents: FluidStack) {
            setOnlySave()
        }
    }
    private val items: ItemStacksResourceHandler = object : ItemStacksResourceHandler(1) {
        override fun onContentsChanged(index: Int, previousContents: ItemStack) {
            setOnlySave()
        }
    }

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConst.FLUIDS, fluids)
        output.putChild(HTConst.ITEMS, items)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConst.FLUIDS, fluids)
        input.readChild(HTConst.ITEMS, items)
    }

    override fun getItemHandler(direction: Direction?): LimitingResourceHandler<ItemResource> =
        LimitingResourceHandler(items, intArrayOf(0), intArrayOf())

    override fun getFluidHandler(direction: Direction?): LimitingResourceHandler<FluidResource> =
        LimitingResourceHandler(fluids, intArrayOf(), intArrayOf(0))

    override fun getEnergyStorage(direction: Direction?): EnergyHandler? = null

    //    Processing    //

    private val cache: HTRecipeCache<HCMeltingRecipe.Input, HCMeltingRecipe> = HCRecipeTypes.MELTING.createCache()
    private val inputHandler = HTItemInputHandler(items, 0)
    private val outputHandler: HTFluidOutputHandler = HTFluidOutputHandler.single(fluids, 0)

    val handler: HTRecipeHandler<HCMeltingRecipe.Input, HCMeltingRecipe> = HTProgressHandler.create {
        recipeFinder = finder@{ level: ServerLevel, _ ->
            val input = HCMeltingRecipe.Input(inputHandler.stack, 10000)
            if (input.isEmpty) return@finder null
            cache.getFirstRecipe(input, level)?.let { HTHandledRecipe.create(input, it) }
        }
        maxProgressGetter = { it.recipe.time }
        progressGetter = { level, pos -> 1 }
        canComplete = { _, _, recipe: HTHandledRecipe<HCMeltingRecipe.Input, HCMeltingRecipe> ->
            outputHandler.canInsert(recipe.assembleFluid())
        }
        onComplete = { level, _, recipe: HTHandledRecipe<HCMeltingRecipe.Input, HCMeltingRecipe> ->
            useTransaction {
                // output
                outputHandler.insert(recipe.assembleFluid(), it)
                // input
                inputHandler.consume(1, it)
                it.commit()
            }
            // sound
            playSound(SoundEvents.LAVA_POP)
        }
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = handler.tick(level, pos)
}
