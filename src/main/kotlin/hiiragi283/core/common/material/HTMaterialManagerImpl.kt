package hiiragi283.core.common.material

import com.mojang.logging.LogUtils
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import net.neoforged.fml.ModLoader
import org.slf4j.Logger

class HTMaterialManagerImpl : HTMaterialManager {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        private var propertyMapMap: Map<HTMaterialKey, HTPropertyMap> = mapOf()

        @JvmStatic
        fun gatherAttributes(isDataGen: Boolean) {
            val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = mutableMapOf()
            ModLoader.postEvent(
                HTMaterialPropertyEvent(
                    isDataGen,
                ) { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTBasicPropertyMap.Mutable() } },
            )
            propertyMapMap = builderMap.filterValues(HTPropertyMap::isNotEmpty)
            LOGGER.info("Gathered Material Attributes!")
        }
    }

    //    HTMaterialManager    //

    override fun contains(material: HTMaterialLike): Boolean = propertyMapMap.contains(material.asMaterialKey())

    override fun get(material: HTMaterialLike): HTPropertyMap? = propertyMapMap[material.asMaterialKey()]

    override val keys: Set<HTMaterialKey>
        get() = propertyMapMap.keys
    override val entries: Set<Map.Entry<HTMaterialKey, HTPropertyMap>>
        get() = propertyMapMap.entries
}
