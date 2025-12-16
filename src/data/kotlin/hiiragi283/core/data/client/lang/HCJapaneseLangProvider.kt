package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCItems
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) : HTLangProvider.Japanese(output, HiiragiCoreAPI.MOD_ID) {
    override fun addTranslations() {
        // Item
        for (material: HCMaterial in HCMaterial.entries) {
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in HCItems.MATERIALS.column(material)) {
                val name: String = HCMaterialTranslations.translate(langType, prefix, material) ?: continue
                add(item, name)
            }
        }
        
        // Translation
        add(HCTranslation.CREATIVE_TAB_MATERIAL, "Hiiragi Core - 素材")
    }
}
