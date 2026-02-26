package hiiragi283.core.common.block.cauldron

import hiiragi283.core.setup.HCItems
import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.phys.Vec3

class HTLatexCauldronBlock(properties: Properties) :
    LayeredCauldronBlock(Biome.Precipitation.NONE, HCCauldronInteractions.LATEX, properties) {
    override fun canReceiveStalactiteDrip(fluid: Fluid): Boolean = false

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        if (isFull(state) && random.nextInt(7) == 0) {
            HTItemDropHelper.dropStackAt(level, Vec3.atCenterOf(pos), HCItems.RAW_RUBBER.toStack(random.nextInt(1, 4)))
            level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState())
        }
    }

    override fun asItem(): Item = Items.CAULDRON
}
