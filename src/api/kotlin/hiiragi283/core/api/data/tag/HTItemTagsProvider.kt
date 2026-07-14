package hiiragi283.core.api.data.tag

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.resource.BlockItemSupplierWithKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

/**
 * [アイテム][Item]向けの[HTTagsProvider.DataGen]の拡張クラスです。
 * @param blockTags 生成された[ブロック][Block]のタグの一覧
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTItemTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    modId: String,
    private val blockTags: CompletableFuture<TagLookup<Block>>,
) : HTTagsProvider.DataGen<Item>(fileHelper, output, Registries.ITEM, lookupProvider, modId) {
    //    Extensions    //

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    /**
     * [HTTagPrefix.rawCommonTag]と[HTTagPrefix.materialTag]に基づいて，ブロックのタグの値をアイテムのタグにコピーします。
     */
    protected fun copy(prefix: HTTagPrefix, material: HTMaterialLike) {
        copy(prefix.rawCommonTag)
        copy(prefix.materialTag(material))
    }

    /**
     * @since 0.16.0
     */
    protected fun copy(rawTagKey: RawTagKey) {
        tagsToCopy[rawTagKey.create(Registries.BLOCK)] = rawTagKey.create(Registries.ITEM)
    }

    /**
     * [ブロックのタグ][blockTag]の値を[アイテムのタグ][itemTag]にコピーします。
     */
    protected fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    fun HTTagBuilder<Item>.add(value: BlockItemSupplierWithKey<*, *>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Item> = this.add(value.getItemSupplier(), type)

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
