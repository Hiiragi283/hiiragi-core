package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.data.tag.HTItemTagsProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

class HCItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>) : HTItemTagsProvider(output, lookupProvider, contentsGetter, HiiragiCoreAPI.MOD_ID) {
    override fun addTags(registries: HolderLookup.Provider) {
        tag(Tags.Items.CROPS).addTag(HiiragiCoreTags.Items.CROPS_WARPED_WART)
        tag(HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART)

        tag(HiiragiCoreTags.Items.STICKY_BALLS).addTag(Tags.Items.SLIME_BALLS)
    }
}
