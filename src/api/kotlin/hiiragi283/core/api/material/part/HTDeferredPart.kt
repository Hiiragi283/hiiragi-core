package hiiragi283.core.api.material.part

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.property.HTPropertyKey
import net.minecraft.resources.ResourceLocation

/**
 * [HiiragiCoreAccess.partManager]に基づいた[HTPartLike]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
class HTDeferredPart(private val name: String) : HTPartLike {
    private lateinit var partCache: HTPart

    override fun asPart(): HTPart {
        if (!::partCache.isInitialized) {
            partCache = HiiragiCoreAccess.INSTANCE.partManager[name] ?: error("Unregistered part: $name")
        }
        return partCache
    }

    override fun asPartName(): String = name

    override fun createId(material: HTMaterialLike): ResourceLocation = asPart().createId(material)

    override fun isEmpty(): Boolean = asPart().isEmpty()

    override fun contains(key: HTPropertyKey<*>): Boolean = asPart().contains(key)

    override fun <T> get(key: HTPropertyKey<T>): T? = asPart()[key]
}
