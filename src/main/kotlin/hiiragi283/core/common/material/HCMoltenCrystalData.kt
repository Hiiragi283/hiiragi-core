package hiiragi283.core.common.material

import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.setup.HCFluids
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import java.awt.Color

enum class HCMoltenCrystalData(val color: Color, private val enName: String, private val jaName: String) : HTMaterialLike.Translatable {
    CRIMSON(Color(0x990000), "Crimson Blood", "深紅の血液"),
    WARPED(Color(0x009999), "Dew of the Warp", "歪みの雫"),
    ELDRITCH(Color(0x990099), "Eldritch Flux", "異質な流動体"),
    ;

    val base: TagKey<Item>?
        get() = when (this) {
            CRIMSON -> ItemTags.CRIMSON_STEMS
            WARPED -> ItemTags.WARPED_STEMS
            ELDRITCH -> null
        }

    val sap: HTSimpleFluidContent?
        get() = when (this) {
            CRIMSON -> HCFluids.CRIMSON_SAP
            WARPED -> HCFluids.WARPED_SAP
            ELDRITCH -> null
        }

    val molten: HTSimpleFluidContent
        get() = when (this) {
            CRIMSON -> HCFluids.CRIMSON_BLOOD
            WARPED -> HCFluids.DEW_OF_THE_WARP
            ELDRITCH -> HCFluids.ELDRITCH_FLUX
        }

    val material: HCMaterial get() = when (this) {
        CRIMSON -> HCMaterial.Gems.CRIMSON_CRYSTAL
        WARPED -> HCMaterial.Gems.WARPED_CRYSTAL
        ELDRITCH -> HCMaterial.Pearls.ELDRITCH
    }

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }

    override fun asMaterialKey(): HTMaterialKey = material.asMaterialKey()
}
