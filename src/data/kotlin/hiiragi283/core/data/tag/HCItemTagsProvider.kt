package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.tag.HTCommonTags
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

class HCItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>) : HTItemTagsProvider(output, lookupProvider, contentsGetter, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
        copy(HTCommonTags.Blocks.STORAGE_BLOCKS_CHARCOAL, HTCommonTags.Items.STORAGE_BLOCKS_CHARCOAL)
        copy(HTCommonTags.Blocks.STORAGE_BLOCKS_GLOWSTONE, HTCommonTags.Items.STORAGE_BLOCKS_GLOWSTONE)
        copy(HTCommonTags.Blocks.STORAGE_BLOCKS_QUARTZ, HTCommonTags.Items.STORAGE_BLOCKS_QUARTZ)
        copy(HTCommonTags.Blocks.STORAGE_BLOCKS_AMETHYST, HTCommonTags.Items.STORAGE_BLOCKS_AMETHYST)
        copy(HTCommonTags.Blocks.STORAGE_BLOCKS_ECHO, HTCommonTags.Items.STORAGE_BLOCKS_ECHO)

        tags(Tags.Items.GEMS, HTCommonTags.Items.GEMS_ECHO).addItem(Items.ECHO_SHARD)

        tags(Tags.Items.NUGGETS, HTCommonTags.Items.NUGGETS_NETHERITE).add(HCItems.NETHERITE_NUGGET)

        tags(Tags.Items.CROPS, HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART)

        tag(HiiragiCoreTags.Items.STICKY_BALLS).addTag(Tags.Items.SLIME_BALLS)
        tag(Tags.Items.FEATHERS).add(HCItems.SYNTHETIC_FEATHER)
        tag(Tags.Items.STRINGS).add(HCItems.SYNTHETIC_FIBER)
        tag(Tags.Items.LEATHERS).add(HCItems.SYNTHETIC_LEATHER)
    }
}
