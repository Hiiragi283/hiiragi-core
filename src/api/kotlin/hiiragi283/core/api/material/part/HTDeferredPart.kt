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
@JvmInline
value class HTDeferredPart(private val name: String) : HTPartLike {
    override fun asPart(): HTPart = HiiragiCoreAccess.INSTANCE.partManager[name] ?: error("Unregistered part: $name")

    override fun asPartName(): String = name

    override fun createId(material: HTMaterialLike): ResourceLocation = asPart().createId(material)

    override fun contains(key: HTPropertyKey<*>): Boolean = asPart().contains(key)

    override fun <T> get(key: HTPropertyKey<T>): T? = asPart()[key]
}
