package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.attribute.HTMaterialAttribute

interface HTMaterialManager {
    companion object {
        @JvmField
        val INSTANCE: HTMaterialManager = HiiragiCoreAPI.getService()
    }

    operator fun contains(material: HTMaterialLike): Boolean

    operator fun get(material: HTMaterialLike): HTMaterialDefinition?

    val keys: Set<HTMaterialKey>

    val entries: Set<Map.Entry<HTMaterialKey, HTMaterialDefinition>>

    fun asSequence(): Sequence<Map.Entry<HTMaterialKey, HTMaterialDefinition>> = entries.asSequence()

    fun getOrEmpty(material: HTMaterialLike): HTMaterialDefinition = get(material) ?: Empty

    private data object Empty : HTMaterialDefinition {
        override fun contains(clazz: Class<out HTMaterialAttribute>): Boolean = false

        override fun <T : HTMaterialAttribute> get(clazz: Class<T>): T? = null

        override fun getAllAttributes(): Collection<HTMaterialAttribute> = listOf()
    }
}
