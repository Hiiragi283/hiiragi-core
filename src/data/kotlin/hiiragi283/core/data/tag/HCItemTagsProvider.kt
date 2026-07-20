package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.theillusivec4.curios.api.CuriosTags

class HCItemTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
) : HTItemTagsProvider(fileHelper, output, lookupProvider, HiiragiCoreAPI.MOD_ID, blockTags) {
    override fun appendTags(registries: HolderLookup.Provider) {
        // Buckets
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            tags(Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
        // Foods
        tags(Tags.Items.FOODS_DOUGH, HiiragiCoreTags.Items.FOODS_DOUGH_WHEAT).add(HCItems.WHEAT_DOUGH)
        tags(HiiragiCoreTags.Items.FLOURS, HiiragiCoreTags.Items.FLOURS_WHEAT).add(HCItems.WHEAT_FLOUR)

        builder(Tags.Items.FOODS_GOLDEN).add(HCItems.AMBROSIA)
        builder(Tags.Items.DRINKS_MAGIC).add(HCItems.POTION_OF_INFINITY)
        // Materials
        builder(ItemTags.COALS).add(HCItems.BAMBOO_CHARCOAL)
        builder(ItemTags.PLANKS).add(HCItems.PARTICLE_BOARD)
        // Tools
        listOf(
            ItemTags.AXES,
            ItemTags.HOES,
            ItemTags.PICKAXES,
            ItemTags.SHOVELS,
            Tags.Items.MINING_TOOL_TOOLS,
        ).map(::builder).forEach { it.add(HCItems.ALMIGHTY_PICKAXE) }
        // Others
        builder(Tags.Items.FEATHERS).add(HCItems.SYNTHETIC_FEATHER)
        builder(Tags.Items.LEATHERS).add(HCItems.SYNTHETIC_LEATHER)
        builder(Tags.Items.STRINGS).add(HCItems.SYNTHETIC_FIBER)

        tags(Tags.Items.CROPS, HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART)

        builder(HiiragiCoreTags.Items.RUBBERS).add(HCItems.CURED_RUBBER)

        builder(HiiragiCoreTags.Items.STICKY_BALLS).add(HCItems.RAW_RUBBER).addTag(Tags.Items.SLIME_BALLS)

        builder(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            .add(Items.GHAST_TEAR.toLike())
            .add(Items.PHANTOM_MEMBRANE.toLike())
            .add(Items.WIND_CHARGE.toLike())

        builder(HiiragiCoreTags.Items.FORGING_HAMMERS)
            .addTag(HiiragiCoreTags.Items.HAMMERS, HTTagDependType.OPTIONAL)
            .addTag(HiiragiCoreTags.Items.TOOLS_HAMMER, HTTagDependType.OPTIONAL)
        // Curios
        builder(CuriosTags.RING).add(HCItems.RING_OF_HYPERION)
    }
}
