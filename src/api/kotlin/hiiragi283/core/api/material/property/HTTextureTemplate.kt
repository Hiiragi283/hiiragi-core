package hiiragi283.core.api.material.property

import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

@JvmRecord
data class HTTextureTemplate(private val baseType: BaseType, private val overrideMap: Map<HTMaterialPrefix, String> = mapOf()) {
    companion object {
        @JvmStatic
        fun dull(vararg pairs: Pair<HTMaterialPrefix, String>): HTTextureTemplate = HTTextureTemplate(BaseType.DULL, mapOf(*pairs))

        @JvmStatic
        fun default(vararg pairs: Pair<HTMaterialPrefix, String>): HTTextureTemplate = HTTextureTemplate(BaseType.DEFAULT, mapOf(*pairs))

        @JvmStatic
        fun shine(vararg pairs: Pair<HTMaterialPrefix, String>): HTTextureTemplate = HTTextureTemplate(BaseType.SHINE, mapOf(*pairs))
    }

    operator fun get(prefix: HTPrefixLike): String {
        val prefix1: HTMaterialPrefix = prefix.asMaterialPrefix()
        return overrideMap[prefix1] ?: "${prefix.asPrefixName()}${baseType.suffix}"
    }

    enum class BaseType(val suffix: String) {
        DULL("_dull"),
        DEFAULT(""),
        SHINE("_shine"),
    }
}
