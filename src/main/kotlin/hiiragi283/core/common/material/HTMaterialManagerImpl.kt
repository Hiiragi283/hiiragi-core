package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.common.HiiragiCoreAccessImpl
import net.neoforged.fml.ModLoader

class HTMaterialManagerImpl(private val propertyMapMap: Map<HTMaterialKey, HTPropertyMap>) : HTMaterialManager {
    companion object {
        @JvmStatic
        internal fun gatherAttributes() {
            val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = mutableMapOf()
            ModLoader.postEvent(
                HTMaterialPropertyEvent { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTBasicPropertyMap.Mutable() } },
            )
            HiiragiCoreAccessImpl.materialManagerCache =
                builderMap.filterValues(HTPropertyMap::isNotEmpty).let(::HTMaterialManagerImpl)
            HiiragiCoreAPI.LOGGER.info("Gathered Material Attributes!")
        }
    }

    override fun contains(material: HTMaterialLike): Boolean = material.asMaterialKey() in propertyMapMap

    override fun get(material: HTMaterialLike): HTPropertyMap? = propertyMapMap[material.asMaterialKey()]

    override val keys: Set<HTMaterialKey> get() = propertyMapMap.keys
    override val entries: Set<HTMaterialManager.Entry>
        get() = propertyMapMap.mapTo(mutableSetOf(), ::EntryImpl)

    private class EntryImpl(entry: Map.Entry<HTMaterialKey, HTPropertyMap>) :
        HTMaterialManager.Entry,
        HTMaterialLike by entry.key,
        HTPropertyMap by entry.value
}
