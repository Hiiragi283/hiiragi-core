package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI

interface HTMaterialManager {
    companion object {
        @JvmField
        val INSTANCE: HTMaterialManager = HiiragiCoreAPI.getService()
    }

    operator fun get(key: HTMaterialKey): HTMaterialDefinition?

    fun getOrEmpty(key: HTMaterialKey): HTMaterialDefinition = get(key) ?: HTMaterialDefinition.Empty

    fun getAllKeys(): Set<HTMaterialKey>

    fun unwrap(): Map<HTMaterialKey, HTMaterialDefinition>
}
