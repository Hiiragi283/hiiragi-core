package hiiragi283.core.api.material.property

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.property.HTPropertyMap

fun HTPropertyMap.getDefaultPart(): HTMaterialPrefix? = this[HTMaterialPropertyKeys.DEFAULT_PART]

fun HTPropertyMap.getStorageBlock(): HTStorageBlockProperty = this.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)

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

fun HTPropertyMap.Mutable.addTemplate(value: HTTextureTemplate) {
    this[HTMaterialPropertyKeys.TEXTURE_TEMPLATE] = value
}
