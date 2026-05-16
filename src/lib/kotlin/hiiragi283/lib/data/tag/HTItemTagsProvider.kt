package hiiragi283.lib.data.tag

import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * @see net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider
 */
abstract class HTItemTagsProvider : HTIdLikeTagsProvider<Item> {
    private val blockTags: CompletableFuture<TagLookup<Block>>
    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    constructor(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, parentProvider: CompletableFuture<TagLookup<Item>>, contentsGetter: CompletableFuture<TagLookup<Block>>, modId: String) : super(output, Registries.ITEM, lookupProvider, parentProvider, modId) {
        this.blockTags = contentsGetter
    }

    constructor(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>, modId: String) : super(output, Registries.ITEM, lookupProvider, modId) {
        this.blockTags = contentsGetter
    }

    protected fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    final override fun createContentsProvider(): CompletableFuture<HolderLookup.Provider> = super.createContentsProvider().thenCombine(blockTags) { provider: HolderLookup.Provider, blockTags1: TagLookup<Block> ->
        for ((blockTag: TagKey<Block>, itemTag: TagKey<Item>) in this.tagsToCopy) {
            val builder: TagBuilder = this.getOrCreateRawBuilder(itemTag)
            blockTags1.apply(blockTag)
                .orElseThrow { error("Missing block tag ${itemTag.location()}") }
                .let { blockBuilder ->
                    blockBuilder.build().forEach(builder::add)
                    blockBuilder.removeEntries.forEach(builder::remove)
                }
        }
        provider
    }
}
