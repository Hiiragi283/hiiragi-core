package hiiragi283.core.api.data.holder

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import net.minecraft.core.registries.Registries
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
     * 指定した[pair]から，[TagKey]を要求する[ICondition]を追加します。
     */
    @JvmName("addTagCondition")
    operator fun plusAssign(pair: Pair<HTTagPrefix, HTMaterialLike>) {
        val (prefix: HTTagPrefix, material: HTMaterialLike) = pair
        this.plusAssign(prefix.materialTag(material))
    }

    /**
     * 指定した[rawTagKey]を要求する[ICondition]を追加します。
     */
    @JvmName("addTagCondition")
    operator fun plusAssign(rawTagKey: RawTagKey) {
        this.plusAssign(rawTagKey.create(Registries.ITEM))
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
