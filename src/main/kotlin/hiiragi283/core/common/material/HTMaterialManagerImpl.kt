package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.common.HiiragiCoreAccessImpl
import net.neoforged.fml.ModLoader

internal class HTMaterialManagerImpl(propertyMapMap: Map<HTMaterialKey, HTPropertyMap>) :
    HTMaterialManager,
    Map<HTMaterialKey, HTPropertyMap> by propertyMapMap {
    companion object {
        @JvmStatic
        internal fun gatherAttributes() {
            val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = mutableMapOf()
            ModLoader.postEvent(
                HTMaterialPropertyEvent { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTBasicPropertyMap.Mutable() } },
            )
            (HiiragiCoreAccess.INSTANCE as HiiragiCoreAccessImpl).materialManager =
                builderMap.filterValues(HTPropertyMap::isNotEmpty).let(::HTMaterialManagerImpl)
            HiiragiCoreAPI.LOGGER.info("Gathered Material Attributes!")
        }
    }
}
