package hiiragi283.core.common.material

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.property.HTPropertyMap

class HTMaterialManagerImpl(private val propertyMapMap: Map<HTMaterialKey, HTPropertyMap>) : HTMaterialManager {
    override fun contains(material: HTMaterialLike): Boolean = material.asMaterialKey() in propertyMapMap

    override fun get(material: HTMaterialLike): HTPropertyMap? = propertyMapMap[material.asMaterialKey()]

    override val keys: Set<HTMaterialKey> = propertyMapMap.keys
    override val entries: Set<HTMaterialManager.Entry> = propertyMapMap.mapTo(mutableSetOf(), ::EntryImpl)

    private class EntryImpl(entry: Map.Entry<HTMaterialKey, HTPropertyMap>) :
        HTMaterialManager.Entry,
        HTMaterialLike by entry.key,
        HTPropertyMap by entry.value
}
