package hiiragi283.core.api.data.advancement

import net.minecraft.advancements.Advancement
import net.neoforged.neoforge.common.conditions.ICondition

fun interface HTAdvancementExporter {
    /**
     * 受け取った進捗を処理します。
     * @param key 受け取った進捗のID
     * @param advancement 受け取った進捗の値
     * @param conditions 進捗を読み込む条件の一覧
     */
    fun accept(key: AdvancementKey, advancement: Advancement, conditions: List<ICondition>)

    /**
     * 受け取ったレシピを処理します。
     * @param key 受け取ったレシピのID
     * @param advancement 受け取った進捗の値
     */
    fun accept(key: AdvancementKey, advancement: Advancement) {
        accept(key, advancement, listOf())
    }
}
