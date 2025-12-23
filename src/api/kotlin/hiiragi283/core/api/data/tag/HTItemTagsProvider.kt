package hiiragi283.core.api.data.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.toHolderLike
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

/**
 * [アイテム][Item]向けの[HTTagsProvider]の拡張クラスです。
 * @param blockTags 生成された[ブロック][Block]のタグの一覧
 */
abstract class HTItemTagsProvider(modId: String, private val blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTTagsProvider<Item>(modId, Registries.ITEM, context) {
    //    Extensions    //

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    /**
     * [HTPrefixLike.createCommonTagKey]と[HTPrefixLike.createTagKey]に基づいて，ブロックのタグの値をアイテムのタグにコピーします。
     */
    protected fun copy(prefix: HTPrefixLike, material: HTMaterialLike) {
        copy(prefix.createCommonTagKey(Registries.BLOCK), prefix.createCommonTagKey(Registries.ITEM))
        copy(prefix.createTagKey(Registries.BLOCK, material), prefix.itemTagKey(material))
    }

    /**
     * [ブロックのタグ][blockTag]の値を[アイテムのタグ][itemTag]にコピーします。
     */
    protected fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    fun HTTagBuilder<Item>.addItem(item: ItemLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Item> =
        this.add(item.toHolderLike(), type)

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
