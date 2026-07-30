package hiiragi283.core.api.material.part

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyKey
import net.minecraft.resources.ResourceLocation

/**
 * [HTPartManager]に基づいた[HTPartLike]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
@JvmInline
value class HTDeferredPart(override val key: HTPartKey) : HTPartLike {
    constructor(name: String) : this(HTPartKey(name))

    override fun asPart(): HTPart = HTPartManager.getInstance().getOrThrow(key)

    override fun createId(key: HTMaterialKey): ResourceLocation = asPart().createId(key)

    override fun contains(key: HTPropertyKey<*>): Boolean = asPart().contains(key)

    override fun <T> get(key: HTPropertyKey<T>): T? = asPart()[key]
}
