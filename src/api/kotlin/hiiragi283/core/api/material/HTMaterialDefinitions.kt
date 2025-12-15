package hiiragi283.core.api.material

import hiiragi283.core.api.material.attribute.HTDefaultPrefixMaterialAttribute
import hiiragi283.core.api.material.attribute.HTMaterialAttribute
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

inline fun <reified T : HTMaterialAttribute> HTMaterialDefinition.get(): T? = get(T::class.java)

fun HTMaterialDefinition.getDefaultPrefix(): HTMaterialPrefix? = get<HTDefaultPrefixMaterialAttribute>()?.prefix

// Builder
fun HTMaterialDefinition.Builder.addDefaultPrefix(prefix: HTPrefixLike) {
    add(HTDefaultPrefixMaterialAttribute(prefix.asMaterialPrefix()))
}
