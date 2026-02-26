package hiiragi283.core.common.block.cauldron

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.getBucket
import hiiragi283.core.api.registry.getBucketHolder
import hiiragi283.core.api.registry.getDefaultState
import hiiragi283.core.api.registry.getFluidType
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.BlockPos
import net.minecraft.core.cauldron.CauldronInteraction
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.SoundActions
import java.util.function.Predicate

object HCCauldronInteractions {
    @JvmField
    val LATEX: CauldronInteraction.InteractionMap = CauldronInteraction.newInteractionMap(HiiragiCoreAPI.id("latex").toString())

    @JvmStatic
    fun init() {
        // Latex
        val latexMap: MutableMap<Item, CauldronInteraction> = LATEX.map
        latexMap[Items.BUCKET] = fillBucket(HCFluids.LATEX) { state: BlockState ->
            state.getValue(LayeredCauldronBlock.LEVEL) == LayeredCauldronBlock.MAX_FILL_LEVEL
        }
        emptyBucket(
            HCFluids.LATEX,
            HCBlocks.LATEX_CAULDRON
                .getDefaultState()
                .setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL),
        )
    }

    @JvmStatic
    private fun emptyBucket(fluid: HTFluidContent, cauldron: BlockState) {
        CauldronInteraction.EMPTY.map[fluid.getBucket()] =
            CauldronInteraction { _: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
                CauldronInteraction.emptyBucket(
                    level,
                    pos,
                    player,
                    hand,
                    stack,
                    cauldron,
                    fluid.getFluidType().getSound(SoundActions.BUCKET_EMPTY) ?: SoundEvents.BUCKET_EMPTY,
                )
            }
    }

    @JvmStatic
    private fun fillBucket(fluid: HTFluidContent, predicate: Predicate<BlockState>): CauldronInteraction =
        CauldronInteraction { state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
            val fillSound: SoundEvent = fluid.getFluidType().getSound(SoundActions.BUCKET_FILL) ?: SoundEvents.BUCKET_FILL
            CauldronInteraction.fillBucket(state, level, pos, player, hand, stack, fluid.getBucketHolder().toStack(), predicate, fillSound)
        }
}
