package hiiragi283.core.common.block.cauldron

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid

class HTLatexCauldronBlock(properties: Properties) :
    LayeredCauldronBlock(Biome.Precipitation.NONE, HCCauldronInteractions.LATEX, properties) {
    override fun canReceiveStalactiteDrip(fluid: Fluid): Boolean = false

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
    }

    override fun asItem(): Item = Items.CAULDRON
}
