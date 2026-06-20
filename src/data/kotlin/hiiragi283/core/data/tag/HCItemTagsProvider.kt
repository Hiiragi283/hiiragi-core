package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTPartTagManager
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.resource.SimpleSupplierWithKey
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

class HCItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>) : HTItemTagsProvider(output, lookupProvider, contentsGetter, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        copy(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.GLOWSTONE)
        copy(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ)
        copy(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST)

        HCBlocks.RESOURCES.forEach { (part: HTMaterialPartKey, material: HTMaterialKey, _) ->
            val prefix: HTTagPrefix = HTPartTagManager[part] ?: return@forEach
            copy(prefix.rawCommonTag)
            copy(prefix, material)
        }

        HCItems.RESOURCES.forEach { (part: HTMaterialPartKey, material: HTMaterialKey, item: SimpleSupplierWithKey<Item>) ->
            val prefix: HTTagPrefix = HTPartTagManager[part] ?: return@forEach
            tags(prefix, material).add(item)
        }

        tags(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO).addItem(Items.ECHO_SHARD)

        tags(Tags.Items.CROPS, HiiragiCoreTags.Items.CROPS_WARPED_WART).add(HCBlocks.WARPED_WART.itemHolder)

        tag(HiiragiCoreTags.Items.STICKY_BALLS).addTag(Tags.Items.SLIME_BALLS)
        tag(Tags.Items.FEATHERS).add(HCItems.SYNTHETIC_FEATHER)
        tag(Tags.Items.STRINGS).add(HCItems.SYNTHETIC_FIBER)
        tag(Tags.Items.LEATHERS).add(HCItems.SYNTHETIC_LEATHER)
    }
}
