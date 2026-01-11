package hiiragi283.core.api.material.property

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.data.texture.HTArrayColorPalette
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.data.texture.HTGradientColorPalette
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.property.HTPropertyMap
import java.awt.Color

fun HTPropertyMap.getDefaultPart(): HTMaterialPrefix? = this[HTMaterialPropertyKeys.DEFAULT_PART]

// Mutable

fun HTPropertyMap.Mutable.addDefaultPart(prefix: HTPrefixLike) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = prefix.asMaterialPrefix()
}

fun HTPropertyMap.Mutable.addName(enName: String, jaName: String) {
    this.addName { type: HTLanguageType ->
        when (type) {
            HTLanguageType.EN_US -> enName
            HTLanguageType.JA_JP -> jaName
        }
    }
}

fun HTPropertyMap.Mutable.addName(value: HTLangName) {
    this[HTMaterialPropertyKeys.LANG_NAME] = value
}

fun HTPropertyMap.Mutable.addGradientColor(vararg colors: Color) {
    this.addColor(HTArrayColorPalette(arrayOf(*colors)))
}

fun HTPropertyMap.Mutable.addGradientColor(from: Color, to: Color) {
    this.addColor(HTGradientColorPalette(from, to))
}

fun HTPropertyMap.Mutable.addColor(value: HTColorPalette) {
    this[HTMaterialPropertyKeys.TEXTURE_COLOR] = value
}

inline fun HTPropertyMap.Mutable.addTemplate(builderAction: HTTextureTemplate.Builder.() -> Unit) {
    this.addTemplate(HTTextureTemplate.create(builderAction))
}

fun HTPropertyMap.Mutable.addTemplate(value: HTTextureTemplate) {
    this[HTMaterialPropertyKeys.TEXTURE_TEMPLATE] = value
}
