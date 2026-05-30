package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class HCItemTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
) : HTItemTagsProvider(fileHelper, output, lookupProvider, HiiragiCoreAPI.MOD_ID, blockTags) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
        // Buckets
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            factory.addTags(Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
        // Foods
        factory.addTags(Tags.Items.FOODS_DOUGH, HiiragiCoreTags.Items.FOODS_DOUGH_WHEAT).add(HCItems.WHEAT_DOUGH)
        factory.addTags(HiiragiCoreTags.Items.FLOURS, HiiragiCoreTags.Items.FLOURS_WHEAT).add(HCItems.WHEAT_FLOUR)

        factory.apply(Tags.Items.FOODS_GOLDEN).add(HCItems.AMBROSIA)
        factory.apply(Tags.Items.DRINKS_MAGIC).add(HCItems.POTION_OF_INFINITY)
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
        factory.apply(Tags.Items.STRINGS).add(HCItems.SYNTHETIC_FIBER)

        factory.addTags(Tags.Items.CROPS, HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART)

        factory.apply(HiiragiCoreTags.Items.RUBBERS).add(HCItems.CURED_RUBBER)

        factory
            .apply(HiiragiCoreTags.Items.STICKY_BALLS)
            .add(HCItems.RAW_RUBBER)
            .addTag(Tags.Items.SLIME_BALLS)

        factory
            .apply(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)

        factory
            .apply(HiiragiCoreTags.Items.FORGING_HAMMERS)
            .addTag(HiiragiCoreTags.Items.HAMMERS, HTTagDependType.OPTIONAL)
            .addTag(HiiragiCoreTags.Items.TOOLS_HAMMER, HTTagDependType.OPTIONAL)
    }
}
