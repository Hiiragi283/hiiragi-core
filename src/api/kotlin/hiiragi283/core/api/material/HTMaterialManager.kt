package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap

interface HTMaterialManager {
    companion object {
        @JvmField
        val INSTANCE: HTMaterialManager = HiiragiCoreAPI.getService()
    }

    operator fun contains(material: HTMaterialLike): Boolean

    operator fun get(material: HTMaterialLike): HTPropertyMap?

    val keys: Set<HTMaterialKey>

    val entries: Set<Map.Entry<HTMaterialKey, HTPropertyMap>>

    fun asSequence(): Sequence<Map.Entry<HTMaterialKey, HTPropertyMap>> = entries.asSequence()

    fun getOrEmpty(material: HTMaterialLike): HTPropertyMap = get(material) ?: EmptyMap

    private data object EmptyMap : HTPropertyMap {
        override fun isEmpty(): Boolean = true

        override fun contains(key: HTPropertyKey<*>): Boolean = false

        override fun <T> get(key: HTPropertyKey<T>): T? = null
    }
}
