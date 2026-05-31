package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMaterialContents
import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

class HCItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>) : HTItemTagsProvider(output, lookupProvider, contentsGetter, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        copy(CommonTagPrefixes.STORAGE_BLOCK.rawCommonTag)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HCMaterialContents.CHARCOAL)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HCMaterialContents.GLOWSTONE)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HCMaterialContents.QUARTZ)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HCMaterialContents.AMETHYST)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HCMaterialContents.ECHO)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HCMaterialContents.IRIDIUM)

        tags(CommonTagPrefixes.DUST, HCMaterialContents.ENDER_PEARL).addItem(HCItems.ENDER_PEARL_DUST)
        tags(CommonTagPrefixes.DUST, HCMaterialContents.OBSIDIAN).addItem(HCItems.OBSIDIAN_DUST)
        tags(CommonTagPrefixes.DUST, HCMaterialContents.IRIDIUM).addItem(HCItems.IRIDIUM_DUST)

        tags(CommonTagPrefixes.GEM, HCMaterialContents.ECHO).addItem(Items.ECHO_SHARD)

        tags(CommonTagPrefixes.INGOT, HCMaterialContents.IRIDIUM).addItem(HCItems.IRIDIUM_INGOT)

        tags(CommonTagPrefixes.NUGGET, HCMaterialContents.NETHERITE).add(HCItems.NETHERITE_NUGGET)
        tags(CommonTagPrefixes.NUGGET, HCMaterialContents.IRIDIUM).add(HCItems.IRIDIUM_NUGGET)

        tags(Tags.Items.CROPS, HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART)

        tag(HiiragiCoreTags.Items.STICKY_BALLS).addTag(Tags.Items.SLIME_BALLS)
        tag(Tags.Items.FEATHERS).add(HCItems.SYNTHETIC_FEATHER)
        tag(Tags.Items.STRINGS).add(HCItems.SYNTHETIC_FIBER)
        tag(Tags.Items.LEATHERS).add(HCItems.SYNTHETIC_LEATHER)
    }
}
