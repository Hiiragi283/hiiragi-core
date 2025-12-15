package hiiragi283.core.common.material

import hiiragi283.core.HiiragiCore
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialDefinitionEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.attribute.HTMaterialAttribute
import net.neoforged.neoforge.common.NeoForge

private typealias MutableAttributeMap = MutableMap<Class<out HTMaterialAttribute>, HTMaterialAttribute>

class HTMaterialManagerImpl :
    HTMaterialManager,
    Map<HTMaterialKey, HTMaterialDefinition> by definitions {
    companion object {
        internal var definitions: Map<HTMaterialKey, HTMaterialDefinition> = mapOf()
            private set

        @JvmStatic
        fun gatherAttributes() {
            val builderMap: MutableMap<HTMaterialKey, MutableAttributeMap> = hashMapOf()
            NeoForge.EVENT_BUS.post(
                HTMaterialDefinitionEvent { key: HTMaterialKey ->
                    val attributeMap: MutableAttributeMap =
                        builderMap.computeIfAbsent(key) { hashMapOf() }
                    BuilderImpl(key, attributeMap)
                },
            )
            definitions = builderMap
                .filterValues { attributeMap: MutableAttributeMap -> attributeMap.isNotEmpty() }
                .mapValues { (_, map: MutableAttributeMap) -> DefinitionImpl(map) }
            HiiragiCore.LOGGER.info("Gathered Material Attributes!")
        }
    }

    //    DefinitionImpl    //

    private class DefinitionImpl(private val attributeMap: MutableAttributeMap) : HTMaterialDefinition {
        override fun contains(clazz: Class<out HTMaterialAttribute>): Boolean = clazz in attributeMap

        @Suppress("UNCHECKED_CAST")
        override fun <T : HTMaterialAttribute> get(clazz: Class<T>): T? = attributeMap[clazz] as? T

        override fun getAllAttributes(): Collection<HTMaterialAttribute> = attributeMap.values
    }

    //    BuilderImpl    //

    private class BuilderImpl(private val key: HTMaterialKey, private val attributeMap: MutableAttributeMap) :
        HTMaterialDefinition.Builder {
        @Suppress("UNCHECKED_CAST")
        override fun <T : HTMaterialAttribute> get(clazz: Class<T>): T? = attributeMap[clazz] as? T

        override fun add(vararg attributes: HTMaterialAttribute) {
            for (attribute: HTMaterialAttribute in attributes) {
                val clazz: Class<out HTMaterialAttribute> = attribute::class.java
                check(attributeMap.put(clazz, attribute) == null) {
                    "Material attribute ${clazz.simpleName} has already exist in ${key.name}"
                }
            }
        }

        override fun set(vararg attributes: HTMaterialAttribute) {
            for (attribute: HTMaterialAttribute in attributes) {
                attributeMap[attribute::class.java] = attribute
            }
        }

        override fun remove(vararg classes: Class<out HTMaterialAttribute>) {
            for (clazz: Class<out HTMaterialAttribute> in classes) {
                attributeMap.remove(clazz)
            }
        }
    }
}
