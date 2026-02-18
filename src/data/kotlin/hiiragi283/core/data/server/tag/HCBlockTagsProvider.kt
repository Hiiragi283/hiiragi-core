package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block

class HCBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider.DataGen<Block>(HiiragiCoreAPI.MOD_ID, Registries.BLOCK, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
        // Mineable
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)

        factory
            .apply(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(HCBlocks.OIL_SHALE)

        factory
            .apply(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(HCBlocks.OIL_SAND)

        factory
            .apply(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)
    }
}
