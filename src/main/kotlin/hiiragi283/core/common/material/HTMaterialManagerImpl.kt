package hiiragi283.core.common.material

import com.mojang.logging.LogUtils
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap
import net.neoforged.fml.ModLoader
import org.slf4j.Logger

private typealias RawPropertyMap = MutableMap<HTPropertyKey<*>, Any?>

class HTMaterialManagerImpl : HTMaterialManager {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        private var propertyMapMap: Map<HTMaterialKey, HTPropertyMap> = mapOf()

        @JvmStatic
        fun gatherAttributes(isDataGen: Boolean) {
            val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = hashMapOf()
            ModLoader.postEvent(
                HTMaterialPropertyEvent(isDataGen) { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { PropertyMapImpl() } },
            )
            propertyMapMap = builderMap.filterValues(HTPropertyMap::isEmpty)
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

    //    PropertyMapImpl    //

    @Suppress("UNCHECKED_CAST")
    private class PropertyMapImpl : HTPropertyMap.Mutable {
        private val map: RawPropertyMap = hashMapOf()

        override fun <T> put(key: HTPropertyKey<T>, value: T): T? {
            if (value == null) return remove(key)
            return map.put(key, value) as? T
        }

        override fun <T> remove(key: HTPropertyKey<T>): T? = map.remove(key) as? T

        override fun isEmpty(): Boolean = map.isEmpty()

        override fun contains(key: HTPropertyKey<*>): Boolean = key in map

        override fun <T> get(key: HTPropertyKey<T>): T? = map[key] as? T
    }
}
