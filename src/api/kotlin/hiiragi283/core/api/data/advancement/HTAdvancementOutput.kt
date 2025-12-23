package hiiragi283.core.api.data.advancement

import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * 進捗の出力先を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTAdvancementOutput {
    /**
     * 進捗を出力します。
     * @param id 進捗のID
     * @param advancement 進捗のインスタンス
     * @param conditions 進捗を読み込む条件の一覧
     */
    fun accept(id: ResourceLocation, advancement: Advancement, vararg conditions: ICondition) {
        accept(id, advancement, conditions.toList())
    }

    /**
     * 進捗を出力します。
     * @param id 進捗のID
     * @param advancement 進捗のインスタンス
     * @param conditions 進捗を読み込む条件の一覧
     */
    fun accept(id: ResourceLocation, advancement: Advancement, conditions: List<ICondition>)
}
