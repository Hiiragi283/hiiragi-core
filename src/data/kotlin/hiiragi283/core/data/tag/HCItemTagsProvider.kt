package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.common.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class HCItemTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
) : HTItemTagsProvider(output, lookupProvider, blockTags, HiiragiCoreAPI.MOD_ID) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
        addTags(factory, Tags.Items.CROPS, HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART)

        factory
            .apply(HiiragiCoreTags.Items.ALMIGHTY_PICKAXE_MATERIALS)
            .addTag(Tags.Items.NETHER_STARS)
    }
}
