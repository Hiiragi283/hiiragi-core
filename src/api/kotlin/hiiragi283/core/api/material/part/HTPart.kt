package hiiragi283.core.api.material.part

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyManager
import hiiragi283.core.api.resource.modifyPath
import net.minecraft.resources.ResourceLocation

/**
 * 部品の種類を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
class HTPart internal constructor(override val key: HTPartKey, private val idPattern: String, getter: HTPropertyGetter) :
    HTPartLike,
    HTPropertyManager.Entry<HTPartKey>,
    HTPropertyGetter by getter,
    Comparable<HTPart> {
    override fun asPart(): HTPart = this

    override fun createId(key: HTMaterialKey): ResourceLocation = key.getId().modifyPath { idPattern.replace("%s", it) }

    override fun compareTo(other: HTPart): Int = this.key.compareTo(other.key)
}
