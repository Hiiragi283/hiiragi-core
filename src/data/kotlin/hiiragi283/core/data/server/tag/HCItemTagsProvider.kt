package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
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
    @Suppress("DEPRECATION")
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
        // Buckets
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.getBucket())
        }
        // Foods
        addTags(factory, HiiragiCoreTags.Items.DOUGHS, HiiragiCoreTags.Items.DOUGHS_WHEAT).add(HCItems.WHEAT_DOUGH)
        addTags(factory, HiiragiCoreTags.Items.FLOURS, HiiragiCoreTags.Items.FLOURS_WHEAT).add(HCItems.WHEAT_FLOUR)

        factory.apply(Tags.Items.FOODS_GOLDEN).add(HCItems.AMBROSIA)
        // Materials
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
        factory.apply(Tags.Items.FEATHERS).add(HCItems.SYNTHETIC_FEATHER)
        factory.apply(Tags.Items.LEATHERS).add(HCItems.SYNTHETIC_LEATHER)
        factory.apply(Tags.Items.SLIME_BALLS).add(HCItems.RAW_RUBBER)
        factory.apply(Tags.Items.SLIMEBALLS).add(HCItems.RAW_RUBBER)
        factory.apply(Tags.Items.STRINGS).add(HCItems.SYNTHETIC_FIBER)

        addTags(factory, Tags.Items.CROPS, HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART)

        factory
            .apply(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)

        factory
            .apply(HiiragiCoreTags.Items.FORGING_HAMMERS)
            .addTag(HiiragiCoreTags.Items.TOOLS_HAMMER, HTTagDependType.OPTIONAL)
    }
}
