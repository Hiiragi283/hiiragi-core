package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.data.tag.HTIdLikeTagsProvider
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.tag.HTCommonTags
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class HCMaterialContentsTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTIdLikeTagsProvider<HTMaterialContents>(output, HTRegistries.Keys.MATERIAL_CONTENTS, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addElementsTags()
        addMineralTags()

        tag(HiiragiCoreTags.MaterialContents.COALS)
            .add(VanillaMaterialKeys.COAL)
            .add(VanillaMaterialKeys.CHARCOAL)
    }

    private fun addElementsTags() {
        tags(HTCommonTags.MaterialContents.ELEMENTS, HTCommonTags.MaterialContents.ELEMENTS_METAL)
            // 4rd period
            .add(VanillaMaterialKeys.IRON)
            .add(VanillaMaterialKeys.COPPER)
            // 5th period
            .add(CommonMaterialKeys.TIN)
            // 6th period
            .add(CommonMaterialKeys.IRIDIUM)
            .add(CommonMaterialKeys.PLATINUM)
            .add(VanillaMaterialKeys.GOLD)
            .add(CommonMaterialKeys.LEAD)
        // tags(HTCommonTags.MaterialContents.ELEMENTS_METAL, HTCommonTags.MaterialContents.ELEMENTS_ALKALI_METAL)
        // tags(HTCommonTags.MaterialContents.ELEMENTS_METAL, HTCommonTags.MaterialContents.ELEMENTS_ALKALI_EARTH_METAL)
        tags(HTCommonTags.MaterialContents.ELEMENTS, HTCommonTags.MaterialContents.ELEMENTS_PLATINUM_GROUP)
            .add(CommonMaterialKeys.IRIDIUM)
            .add(CommonMaterialKeys.PLATINUM)
    }

    private fun addMineralTags() {
        tags(HTCommonTags.MaterialContents.MINERALS, HTCommonTags.MaterialContents.MINERALS_BERYL)
            .add(VanillaMaterialKeys.EMERALD)
        // tags(HTCommonTags.MaterialContents.MINERALS, HTCommonTags.MaterialContents.MINERALS_ALUMINA)

        // tags(HTCommonTags.MaterialContents.MINERALS_ALUMINA, HTCommonTags.MaterialContents.MINERALS_CORUNDUM)
    }

    private fun IdAppender.add(material: HTMaterialKey): IdAppender = this.add { material.identifier() }
}
