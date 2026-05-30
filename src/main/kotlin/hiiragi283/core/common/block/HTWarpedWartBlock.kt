package hiiragi283.core.common.block

import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.block.state.BlockState

class HTWarpedWartBlock(properties: Properties) : NetherWartBlock(properties) {
    override fun getCloneItemStack(level: LevelReader, pos: BlockPos, state: BlockState, includeData: Boolean, player: Player): ItemStack = HCBlocks.WARPED_WART.toStack()
}
