package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

class HCItemTagsProvider(private val blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTTagsProvider<Item>(HiiragiCoreAPI.MOD_ID, Registries.ITEM, context) {
    override fun addTagsInternal(factory: BuilderFactory<Item>) {
        copyTags()

        material(factory)
    }

    //    Copy    //

    private fun copyTags() {
        // Material
        HCBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, _) ->
            copy(prefix, key)
        }
        for ((material: HCMaterial, _) in HCBlockTagsProvider.VANILLA_STORAGE_BLOCKS) {
            copy(HCMaterialPrefixes.STORAGE_BLOCK, material)
        }
    }

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    private fun copy(prefix: HTPrefixLike, material: HTMaterialLike) {
        copy(prefix.createCommonTagKey(Registries.BLOCK), prefix.createCommonTagKey(Registries.ITEM))
        copy(prefix.createTagKey(Registries.BLOCK, material), prefix.itemTagKey(material))
    }

    private fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        HCItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            addMaterial(factory, prefix, key).add(item)
            if (prefix == HCMaterialPrefixes.GEM || prefix == HCMaterialPrefixes.INGOT) {
                factory.apply(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
            }
        }

        addBaseMaterial(factory, HCMaterial.Fuels.COAL, Items.COAL)
        addBaseMaterial(factory, HCMaterial.Fuels.CHARCOAL, Items.CHARCOAL)
        addBaseMaterial(factory, HCMaterial.Gems.ECHO, Items.ECHO_SHARD)
        addBaseMaterial(factory, HCMaterial.Pearls.ENDER, Items.ENDER_PEARL)

        addMaterial(factory, HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE).addItem(Items.NETHERITE_SCRAP)

        factory.apply(ItemTags.PLANKS).addTag(HCMaterialPrefixes.PLATE, HCMaterial.Dusts.WOOD)
    }

    private fun addBaseMaterial(factory: BuilderFactory<Item>, material: HCMaterial, item: ItemLike) {
        addMaterial(factory, material.basePrefix, material).addItem(item)
    }

    //    Extensions    //

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
