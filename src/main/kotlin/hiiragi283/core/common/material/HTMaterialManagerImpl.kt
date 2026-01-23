package hiiragi283.core.common.material

import com.mojang.logging.LogUtils
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import net.neoforged.fml.ModLoader
import org.slf4j.Logger

class HTMaterialManagerImpl :
    HTMaterialManager,
    Map<HTMaterialKey, HTPropertyMap> by propertyMapMap {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        private lateinit var propertyMapMap: Map<HTMaterialKey, HTPropertyMap>

        @JvmStatic
        internal fun gatherAttributes() {
            val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = mutableMapOf()
            ModLoader.postEvent(
                HTMaterialPropertyEvent { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTBasicPropertyMap.Mutable() } },
            )
            propertyMapMap = builderMap.filterValues(HTPropertyMap::isNotEmpty)
            LOGGER.info("Gathered Material Attributes!")
        }
    }
}
