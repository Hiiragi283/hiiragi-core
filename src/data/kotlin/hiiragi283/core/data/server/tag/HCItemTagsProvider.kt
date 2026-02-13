package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
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
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
        // Copy from Block
        for (key: HTMaterialKey in HCBlockTagsProvider.VANILLA_STORAGE_BLOCKS.keys) {
            copy(CommonTagPrefixes.BLOCK, key)
        }

        // Buckets
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.getBucketHolder())
        }
        // Foods
        factory.apply(Tags.Items.FOODS_GOLDEN).add(HCItems.AMBROSIA)
        // Materials
        addMaterial(factory, CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL).addItem(Items.CHARCOAL)
        addMaterial(factory, CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL).addItem(Items.COAL)
        addMaterial(factory, CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO).addItem(Items.ECHO_SHARD)
        addMaterial(factory, CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER).addItem(Items.ENDER_PEARL)
        addMaterial(factory, CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE).addItem(Items.NETHERITE_SCRAP)

        factory.apply(ItemTags.COALS).add(HCItems.BAMBOO_CHARCOAL)
        factory.apply(ItemTags.PLANKS).add(HCItems.PARTICLE_BOARD)
        // Tools
        listOf(
            ItemTags.AXES,
            ItemTags.HOES,
            ItemTags.PICKAXES,
            ItemTags.SHOVELS,
            Tags.Items.MINING_TOOL_TOOLS,
        ).map(factory::apply).forEach { it.add(HCItems.ALMIGHTY_PICKAXE) }
        // Others
        factory.apply(Tags.Items.LEATHERS).add(HCItems.SYNTHETIC_LEATHER)
        factory.apply(Tags.Items.SLIME_BALLS).add(HCItems.RAW_RUBBER)

        factory
            .apply(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)
    }
}
