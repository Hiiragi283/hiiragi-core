package hiiragi283.core.api.material.prefix

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialLike

enum class HTOreVariant(private val enPattern: String, private val jaPattern: String) : HTLangPatternProvider {
    STONE("%s Ore", "%s鉱石"),
    DEEPSLATE("Deepslate %s Ore", "深層%s鉱石"),
    NETHER("Nether %s Ore", "ネザー%s鉱石"),
    END("End %s Ore", "エンド%s鉱石"),
    ;

    fun createPath(material: HTMaterialLike): String = when (this) {
        HTOreVariant.STONE -> "${material.asMaterialName()}_ore"
        HTOreVariant.DEEPSLATE -> "deepslate_${material.asMaterialName()}_ore"
        HTOreVariant.NETHER -> "nether_${material.asMaterialName()}_ore"
        HTOreVariant.END -> "end_${material.asMaterialName()}_ore"
    }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)
}
