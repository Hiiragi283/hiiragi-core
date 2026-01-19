package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.tags.ItemTags
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
        HTMaterialContentsAccess.INSTANCE.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, _) ->
            if (key.getNamespace() != modId) return@forEach
            copy(prefix, key)
        }
        for (key: HTMaterialKey in HCBlockTagsProvider.VANILLA_STORAGE_BLOCKS.keys) {
            copy(CommonTagPrefixes.BLOCK, key)
        }
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        HTMaterialContentsAccess.INSTANCE.getItemTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: HTIdLike) ->
            if (key.getNamespace() != modId) return@forEach
            addMaterial(factory, prefix, key).add(item)
            if (prefix == CommonTagPrefixes.GEM || prefix == CommonTagPrefixes.INGOT) {
                factory.apply(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
            }
            if (prefix == CommonTagPrefixes.PLATE && key == VanillaMaterialKeys.WOOD) {
                factory.apply(ItemTags.PLANKS).add(item)
            }
            if (prefix == CommonTagPrefixes.WIRE && key == CommonMaterialKeys.PLASTIC) {
                factory.apply(Tags.Items.STRINGS).add(item)
            }
        }

        addMaterial(factory, CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL).addItem(Items.COAL)
        addMaterial(factory, CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL).addItem(Items.CHARCOAL)
        addMaterial(factory, CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO).addItem(Items.ECHO_SHARD)
        addMaterial(factory, CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER).addItem(Items.ENDER_PEARL)

        addMaterial(factory, CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE).addItem(Items.NETHERITE_SCRAP)

        factory.apply(ItemTags.COALS).add(HCItems.BAMBOO_CHARCOAL)
        factory.apply(HiiragiCoreTags.Items.COAL_COKE).addTag(CommonTagPrefixes.FUEL, CommonMaterialKeys.COAL_COKE)
    }

    //    Tool    //

    private fun tool(factory: BuilderFactory<Item>) {
        listOf(
            ItemTags.AXES,
            ItemTags.HOES,
            ItemTags.PICKAXES,
            ItemTags.SHOVELS,
            Tags.Items.MINING_TOOL_TOOLS,
        ).map(factory::apply).forEach { it.add(HCItems.ALMIGHTY_PICKAXE) }
    }

    //    Bucket    //

    private fun bucket(factory: BuilderFactory<Item>) {
        for (content: HTFluidContent<*, *, *> in HCFluids.REGISTER.entries) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
    }

    //    Misc    //

    private fun misc(factory: BuilderFactory<Item>) {
        // Foods
        factory.apply(Tags.Items.FOODS_GOLDEN).add(HCItems.AMBROSIA)
        // Others
        factory
            .apply(Tags.Items.SLIME_BALLS)
            .add(HCItems.RAW_RUBBER)

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
