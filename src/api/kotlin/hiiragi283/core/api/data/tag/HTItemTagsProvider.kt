package hiiragi283.core.api.data.tag

import hiiragi283.core.api.registry.toItemLike
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

/**
 * [アイテム][Item]向けの[HTTagsProvider.DataGen]の拡張クラスです。
 * @param blockTags 生成された[ブロック][Block]のタグの一覧
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTItemTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    private val blockTags: CompletableFuture<TagLookup<Block>>,
    modId: String,
) : HTTagsProvider.DataGen<Item>(output, Registries.ITEM, lookupProvider, modId) {
    //    Extensions    //

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    /**
     * [ブロックのタグ][blockTag]の値を[アイテムのタグ][itemTag]にコピーします。
     */
    protected fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    fun HTTagBuilder<Item>.addItem(item: ItemLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Item> =
        this.add(item.toItemLike(), type)

    //    HTTagsProvider    //

    override fun createContentsProvider(): CompletableFuture<HolderLookup.Provider> = super
        .createContentsProvider()
        .thenCombine(blockTags) { provider: HolderLookup.Provider, lookup: TagLookup<Block> ->
            for ((blockTag: TagKey<Block>, itemTag: TagKey<Item>) in tagsToCopy) {
                val builder: TagBuilder = getOrCreateRawBuilder(itemTag)
                lookup
                    .apply(blockTag)
                    .orElseThrow { error("Missing block tag ${itemTag.location}") }
                    .build()
                    .forEach(builder::add)
            }
            provider
        }
}
