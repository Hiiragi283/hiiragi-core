package hiiragi283.core.api.data.holder

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

    /**
     * 指定した[modId]に対応するmodを要求する[ICondition]を追加します。
     */
    @JvmName("addModCondition")
    operator fun plusAssign(modId: String) {
        this.plusAssign(ModLoadedCondition(modId))
    }

    /**
     * 指定した[tagKey]を要求する[ICondition]を追加します。
     */
    @JvmName("addTagCondition")
    operator fun plusAssign(tagKey: TagKey<Item>) {
        this.plusAssign(NotCondition(TagEmptyCondition(tagKey)))
    }

    /**
     * 指定した[condition]を追加します。
     */
    @JvmName("addCondition")
    operator fun plusAssign(condition: ICondition) {
        conditions += condition
    }

    /**
     * [List]に変換します。
     */
    fun toList(): List<ICondition> = conditions

    /**
     * [Array]に変換します。
     */
    fun toArray(): Array<ICondition> = conditions.toTypedArray()
}
