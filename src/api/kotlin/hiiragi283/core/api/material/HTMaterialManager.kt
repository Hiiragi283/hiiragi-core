package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI

interface HTMaterialManager : Map<HTMaterialKey, HTMaterialDefinition> {
    companion object {
        @JvmField
        val INSTANCE: HTMaterialManager = HiiragiCoreAPI.getService()
    }
}
