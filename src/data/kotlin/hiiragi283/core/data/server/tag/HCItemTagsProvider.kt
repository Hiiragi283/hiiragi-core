package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.item.HTToolType
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.tag.HCModTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class HCItemTagsProvider(blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTItemTagsProvider(HiiragiCoreAPI.MOD_ID, blockTags, context) {
    override fun addTagsInternal(factory: BuilderFactory<Item>) {
        copyTags()

        material(factory)

        tool(factory)
        bucket(factory)

        misc(factory)
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

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        HCItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            addMaterial(factory, prefix, key).add(item)
            if (prefix == HCMaterialPrefixes.GEM || prefix == HCMaterialPrefixes.INGOT) {
                factory.apply(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
            }
            if (prefix == HCMaterialPrefixes.PLATE && HCMaterial.Wood.isOf(key)) {
                factory.apply(ItemTags.PLANKS).add(item)
            }
            if (prefix == HCMaterialPrefixes.RAW_MATERIAL && HCMaterial.Plates.RUBBER.isOf(key)) {
                factory.apply(Tags.Items.SLIME_BALLS).add(item)
            }
        }

        addBaseMaterial(factory, HCMaterial.Fuels.COAL, Items.COAL)
        addBaseMaterial(factory, HCMaterial.Fuels.CHARCOAL, Items.CHARCOAL)
        addBaseMaterial(factory, HCMaterial.Gems.ECHO, Items.ECHO_SHARD)
        addBaseMaterial(factory, HCMaterial.Pearls.ENDER, Items.ENDER_PEARL)

        addMaterial(factory, HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE).addItem(Items.NETHERITE_SCRAP)
    }

    private fun addBaseMaterial(factory: BuilderFactory<Item>, material: HCMaterial, item: ItemLike) {
        addMaterial(factory, material.basePrefix, material).addItem(item)
    }

    //    Tool    //

    private fun tool(factory: BuilderFactory<Item>) {
        HCItems.TOOLS.forEach { (toolType: HTToolType<*>, _, item: HTIdLike) ->
            for (tagKey: TagKey<Item> in toolType.getToolTags()) {
                factory.apply(tagKey).add(item)
            }
        }
    }

    //    Bucket    //

    private fun bucket(factory: BuilderFactory<Item>) {
        for (content: HTFluidContent<*, *, *> in HCFluids.REGISTER.entries) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.bucket)
        }
    }

    //    Misc    //

    private fun misc(factory: BuilderFactory<Item>) {
        factory
            .apply(HCModTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)
    }
}
