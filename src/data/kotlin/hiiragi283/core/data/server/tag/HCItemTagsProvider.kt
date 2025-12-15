package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.material.CommonMaterialPrefixes
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.setup.HCItems
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

class HCItemTagsProvider(context: HTDataGenContext) : HTTagsProvider<Item>(HiiragiCoreAPI.MOD_ID, Registries.ITEM, context) {
    override fun addTagsInternal(factory: BuilderFactory<Item>) {
        material(factory)
    }

    private fun material(factory: BuilderFactory<Item>) {
        HCItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            addMaterial(factory, prefix, key).add(item)
            if (prefix.isOf(CommonMaterialPrefixes.GEM) || prefix.isOf(CommonMaterialPrefixes.INGOT)) {
                factory.apply(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
            }
        }

        addBaseMaterial(factory, HCMaterial.Fuels.COAL, Items.COAL)
        addBaseMaterial(factory, HCMaterial.Fuels.CHARCOAL, Items.CHARCOAL)
        addBaseMaterial(factory, HCMaterial.Gems.ECHO, Items.ECHO_SHARD)
        addBaseMaterial(factory, HCMaterial.Pearls.ENDER, Items.ENDER_PEARL)

        addMaterial(factory, CommonMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE).addItem(Items.NETHERITE_SCRAP)

        factory.apply(ItemTags.PLANKS).addTag(CommonMaterialPrefixes.PLATE, HCMaterial.Dusts.WOOD)
    }

    private fun addBaseMaterial(factory: BuilderFactory<Item>, material: HCMaterial, item: ItemLike) {
        addMaterial(factory, material.basePrefix, material).addItem(item)
    }

    //    Extensions    //

    fun HTTagBuilder<Item>.addItem(item: ItemLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Item> =
        this.add(item.toHolderLike(), type)
}
