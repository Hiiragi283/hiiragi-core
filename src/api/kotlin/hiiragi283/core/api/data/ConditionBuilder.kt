package hiiragi283.core.api.data

import hiiragi283.core.api.util.HTBuilderMarker
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition

/**
 * [ICondition]の一覧を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
@JvmInline
value class ConditionBuilder(private val conditions: MutableList<ICondition>) {
    /**
     * [ICondition]を追加します。
     */
    operator fun ICondition.unaryPlus() {
        conditions += this
    }

    /**
     * [String]と同じIDを持つmodが登録されているか判定する[ICondition]を追加します。
     */
    operator fun String.unaryPlus() {
        +ModLoadedCondition(this)
    }

    /**
     * [TagKey]が登録されていないか判定する[ICondition]を追加します。
     */
    operator fun TagKey<Item>.unaryMinus() {
        +TagEmptyCondition(this)
    }

    /**
     * [TagKey]が登録されているか判定する[ICondition]を追加します。
     */
    operator fun TagKey<Item>.unaryPlus() {
        +NotCondition(TagEmptyCondition(this))
    }
}
