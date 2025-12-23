package hiiragi283.core.common.block

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.item.toStack
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.block.state.BlockState

/**
 * @see net.minecraft.world.level.block.NetherWartBlock
 */
class HTWarpedWartBlock(properties: Properties) :
    NetherWartBlock(properties),
    HTBlockWithDescription {
    override fun getDescription(): HTTranslation = HCTranslation.WARPED_WART

    override fun getCloneItemStack(level: LevelReader, pos: BlockPos, state: BlockState): ItemStack = HCBlocks.WARPED_WART.toStack()
}
