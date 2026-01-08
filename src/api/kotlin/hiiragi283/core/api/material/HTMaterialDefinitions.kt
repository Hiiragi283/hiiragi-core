package hiiragi283.core.api.material

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.texture.HTArrayColorPalette
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.data.texture.HTGradientColorPalette
import hiiragi283.core.api.material.attribute.HTColorPaletteMaterialAttribute
import hiiragi283.core.api.material.attribute.HTDefaultPrefixMaterialAttribute
import hiiragi283.core.api.material.attribute.HTLangNameMaterialAttribute
import hiiragi283.core.api.material.attribute.HTMaterialAttribute
import hiiragi283.core.api.material.attribute.HTStorageBlockMaterialAttribute
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import java.awt.Color

inline fun <reified T : HTMaterialAttribute> HTMaterialDefinition.get(): T? = get(T::class.java)

fun HTMaterialDefinition.getDefaultPrefix(): HTMaterialPrefix? = get<HTDefaultPrefixMaterialAttribute>()?.prefix

fun HTMaterialDefinition.getStorageAttribute(): HTStorageBlockMaterialAttribute =
    get<HTStorageBlockMaterialAttribute>() ?: HTStorageBlockMaterialAttribute.THREE_BY_THREE

// Builder
fun HTMaterialDefinition.Builder.addDefaultPrefix(prefix: HTPrefixLike) {
    add(HTDefaultPrefixMaterialAttribute(prefix.asMaterialPrefix()))
}

fun HTMaterialDefinition.Builder.addName(enName: String, jaName: String) {
    add(HTLangNameMaterialAttribute(enName, jaName))
}

fun HTMaterialDefinition.Builder.addName(value: HTLangName) {
    add(HTLangNameMaterialAttribute(value))
}

fun HTMaterialDefinition.Builder.addColor(vararg colors: Color) {
    addColor(HTArrayColorPalette(arrayOf(*colors)))
}

fun HTMaterialDefinition.Builder.addGradientColor(from: Color, to: Color) {
    addColor(HTGradientColorPalette(from, to))
}

fun HTMaterialDefinition.Builder.addColor(value: HTColorPalette) {
    add(HTColorPaletteMaterialAttribute(value))
}
