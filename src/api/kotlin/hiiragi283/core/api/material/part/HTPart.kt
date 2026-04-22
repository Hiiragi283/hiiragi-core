package hiiragi283.core.api.material.part

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.property.HTPropertyGetter
import net.minecraft.resources.ResourceLocation

/**
 * 部品の種類を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
class HTPart(val name: String, private val idPattern: String, getter: HTPropertyGetter) :
    HTPartLike,
    HTPropertyGetter by getter,
    Comparable<HTPart> {
    override fun asPart(): HTPart = this

    override fun asPartName(): String = name

    override fun createId(material: HTMaterialLike): ResourceLocation = material.asMaterialId().withPath { idPattern.replace("%s", it) }

    override fun compareTo(other: HTPart): Int = this.name.compareTo(other.name)
}
