package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.item.HTToolType
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
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
        for (key: HTMaterialKey in HCBlockTagsProvider.VANILLA_STORAGE_BLOCKS.keys) {
            copy(HCMaterialPrefixes.STORAGE_BLOCK, key)
        }
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        HCItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            addMaterial(factory, prefix, key).add(item)
            if (prefix == HCMaterialPrefixes.GEM || prefix == HCMaterialPrefixes.INGOT) {
                factory.apply(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
            }
            if (prefix == HCMaterialPrefixes.PLATE && key == VanillaMaterialKeys.WOOD) {
                factory.apply(ItemTags.PLANKS).add(item)
            }
            if (prefix == HCMaterialPrefixes.RAW_MATERIAL && key == CommonMaterialKeys.RUBBER) {
                factory.apply(Tags.Items.SLIME_BALLS).add(item)
            }
        }

        addMaterial(factory, HCMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL).addItem(Items.COAL)
        addMaterial(factory, HCMaterialPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL).addItem(Items.CHARCOAL)
        addMaterial(factory, HCMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO).addItem(Items.ECHO_SHARD)
        addMaterial(factory, HCMaterialPrefixes.PEARL, VanillaMaterialKeys.ENDER).addItem(Items.ENDER_PEARL)

        addMaterial(factory, HCMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE).addItem(Items.NETHERITE_SCRAP)

        factory.apply(ItemTags.COALS).add(HCItems.BAMBOO_CHARCOAL)
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
        // Foods
        factory.apply(Tags.Items.FOODS_GOLDEN).add(HCItems.AMBROSIA)

        addTags(factory, HiiragiCoreTags.Items.DOUGHS, HiiragiCoreTags.Items.DOUGHS_WHEAT)
            .add(HCItems.WHEAT_DOUGH)
        addTags(factory, HiiragiCoreTags.Items.FLOURS, HiiragiCoreTags.Items.FLOURS_WHEAT)
            .add(HCItems.WHEAT_FLOUR)
        // Others
        factory
            .apply(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)
        factory
            .apply(HiiragiCoreTags.Items.IGNORED_IN_RECIPE_INPUTS)
            .add(HCItems.SLOT_COVER)
        factory
            .apply(HiiragiCoreTags.Items.ORGANIC_OILS)
            .add(HCItems.ANIMAL_FAT)
            .add(HCItems.PULPED_FISH)
            .add(HCItems.PULPED_SEED)
    }
}
