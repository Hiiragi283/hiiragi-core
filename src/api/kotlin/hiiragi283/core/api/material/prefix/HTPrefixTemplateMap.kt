package hiiragi283.core.api.material.prefix

/**
 * テクスチャの生成で使用されるテンプレートを提供するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTPrefixTemplateMap private constructor(
    val prefixes: Set<HTMaterialPrefix>,
    private val customMap: Map<HTMaterialPrefix, String>,
) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTPrefixTemplateMap = Builder().apply(builderAction).build()
    }

    operator fun get(prefix: HTPrefixLike): String? {
        val prefix1: HTMaterialPrefix = prefix.asMaterialPrefix()
        if (prefix1 !in prefixes) return null
        return customMap[prefix1] ?: prefix1.name
    }

    //    Builder    //

    class Builder {
        private val prefixes: MutableSet<HTMaterialPrefix> = mutableSetOf()
        private val customMap: MutableMap<HTMaterialPrefix, String> = hashMapOf()

        fun add(prefix: HTPrefixLike): Builder = apply {
            prefixes += prefix.asMaterialPrefix()
        }

        fun addCustom(prefix: HTPrefixLike, name: String): Builder = apply {
            add(prefix)
            customMap[prefix.asMaterialPrefix()] = name
        }

        fun build(): HTPrefixTemplateMap = HTPrefixTemplateMap(prefixes, customMap)
    }
}
