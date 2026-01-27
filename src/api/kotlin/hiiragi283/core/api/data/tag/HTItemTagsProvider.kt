package hiiragi283.core.api.data.tag

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
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
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTItemTagsProvider(modId: String, private val blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTTagsProvider<Item>(modId, Registries.ITEM, context) {
    //    Extensions    //

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    /**
     * [HTTagPrefix.createCommonTagKey]と[HTTagPrefix.createTagKey]に基づいて，ブロックのタグの値をアイテムのタグにコピーします。
     */
    protected fun copy(prefix: HTTagPrefix, material: HTMaterialLike) {
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
        this.add(HTItemHolderLike.of(item), type)

    /**
     * 素材ブロックのタグをコピーします。
     * @since 0.8.0
     */
    fun copyMaterials() {
        contents.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, _) ->
            if (key.namespace != modId) return@forEach
            copy(prefix, key)
        }
    }

    /**
     * 素材アイテムのタグを登録します。
     * @since 0.8.0
     */
    @HTBuilderMarker
    fun addMaterials(factory: BuilderFactory<Item>, builderAction: (Triple<HTTagPrefix, HTMaterialKey, HTIdLike>) -> Unit) {
        contents.getItemTable().forEach { triple ->
            val (prefix: HTTagPrefix, key: HTMaterialKey, item: HTIdLike) = triple
            if (key.namespace != modId) return@forEach
            addMaterial(factory, prefix, key).add(item)
            builderAction(triple)
        }
    }

    /**
     * 素材ツールのタグを登録します。
     * @since 0.8.0
     */
    @HTBuilderMarker
    fun addTools(factory: BuilderFactory<Item>) {
        contents.getToolTable().forEach { (toolType: HTToolType, key: HTMaterialKey, item: HTIdLike) ->
            if (key.namespace != modId) return@forEach
            toolType.toolTags.map(factory::apply).forEach { it.add(item) }
        }
    }

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
