package hiiragi283.core.api.data.holder

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition

/**
 * 複数の[ICondition]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTConditionHolder {
    private val conditions: MutableList<ICondition> = mutableListOf()

    @JvmName("addModCondition")
    operator fun plusAssign(modId: String) {
        this.plusAssign(ModLoadedCondition(modId))
    }

    @JvmName("addTagCondition")
    operator fun plusAssign(pair: Pair<HTTagPrefix, HTMaterialLike>) {
        val (prefix: HTTagPrefix, material: HTMaterialLike) = pair
        this.plusAssign(prefix.itemTagKey(material))
    }

    @JvmName("addTagCondition")
    operator fun plusAssign(tagKey: TagKey<Item>) {
        this.plusAssign(NotCondition(TagEmptyCondition(tagKey)))
    }

    @JvmName("addCondition")
    operator fun plusAssign(condition: ICondition) {
        conditions += condition
    }

    fun toList(): List<ICondition> = conditions

    fun toArray(): Array<ICondition> = conditions.toTypedArray()
}
