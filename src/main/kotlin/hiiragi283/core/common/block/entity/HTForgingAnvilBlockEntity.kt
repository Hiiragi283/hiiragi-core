package hiiragi283.core.common.block.entity

import hiiragi283.core.api.block.entity.HTManualProcessingBoardBlockEntity
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCRecipeLookups
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTForgingAnvilBlockEntity(worldPosition: BlockPos, blockState: BlockState) : HTManualProcessingBoardBlockEntity(HCRecipeLookups.CRUSHING, HCBlockEntityTypes.FORGING_ANVIL.get(), worldPosition, blockState) {
    override fun canProcessWithTool(tool: ItemStack): Boolean = tool.`is`(ItemTags.PICKAXES)

    override fun playCompletedSound() {
        playSound(SoundEvents.STONE_BREAK)
    }
}
