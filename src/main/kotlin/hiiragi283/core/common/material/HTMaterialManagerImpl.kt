package hiiragi283.core.common.material

import com.mojang.logging.LogUtils
import hiiragi283.core.api.event.HTMaterialDefinitionEvent
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.attribute.HTMaterialAttribute
import net.neoforged.fml.ModLoader
import org.slf4j.Logger

private typealias AttributeMap = Map<Class<out HTMaterialAttribute>, HTMaterialAttribute>
private typealias MutableAttributeMap = MutableMap<Class<out HTMaterialAttribute>, HTMaterialAttribute>

class HTMaterialManagerImpl : HTMaterialManager {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        private var definitionMap: Map<HTMaterialKey, HTMaterialDefinition> = mapOf()

        @JvmStatic
        fun gatherAttributes(isDataGen: Boolean) {
            val builderMap: MutableMap<HTMaterialKey, MutableAttributeMap> = hashMapOf()
            ModLoader.postEvent(
                HTMaterialDefinitionEvent(isDataGen) { key: HTMaterialKey ->
                    val attributeMap: MutableAttributeMap = builderMap.computeIfAbsent(key) { hashMapOf() }
                    BuilderImpl(key, attributeMap)
                },
            )
            definitionMap = builderMap
                .filterValues(MutableAttributeMap::isNotEmpty)
                .mapValues { (_, map: MutableAttributeMap) -> DefinitionImpl(map) }
            LOGGER.info("Gathered Material Attributes!")
        }
    }

    //    HTMaterialManager    //

    override fun contains(material: HTMaterialLike): Boolean = definitionMap.contains(material.asMaterialKey())

    override fun get(material: HTMaterialLike): HTMaterialDefinition? = definitionMap[material.asMaterialKey()]

    override val keys: Set<HTMaterialKey>
        get() = definitionMap.keys
    override val entries: Set<Map.Entry<HTMaterialKey, HTMaterialDefinition>>
        get() = definitionMap.entries

    //    DefinitionImpl    //

    @JvmRecord
    private data class DefinitionImpl(private val map: AttributeMap) : HTMaterialDefinition {
        override fun contains(clazz: Class<out HTMaterialAttribute>): Boolean = map.contains(clazz)

        @Suppress("UNCHECKED_CAST")
        override fun <T : HTMaterialAttribute> get(clazz: Class<T>): T? = map[clazz] as? T

        override fun getAllAttributes(): Collection<HTMaterialAttribute> = map.values
    }

    //    BuilderImpl    //

    @JvmRecord
    private data class BuilderImpl(private val key: HTMaterialKey, private val attributeMap: MutableAttributeMap) :
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
