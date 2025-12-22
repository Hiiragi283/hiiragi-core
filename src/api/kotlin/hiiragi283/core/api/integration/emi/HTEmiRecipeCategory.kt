package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.render.EmiRenderable
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.text.HTHasText
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import java.util.Comparator

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[EmiRecipeCategory]の拡張クラスです。
 * @param bounds レシピが表示される範囲
 * @param hasText カテゴリのタイトル
 * @param workStations カテゴリに紐づいたアイテムの一覧
 * @param id カテゴリのID
 * @param icon カテゴリのアイコン
 * @param simplified レシピツリーなどで使われる簡略化されたアイコン
 * @param sorter レシピの表示順を定める[Comparator]
 */
open class HTEmiRecipeCategory(
    val bounds: HTBounds,
    private val hasText: HTHasText,
    val workStations: List<EmiStack>,
    id: ResourceLocation,
    icon: EmiRenderable,
    simplified: EmiRenderable = icon,
    sorter: Comparator<EmiRecipe> = EmiRecipeSorting.compareOutputThenInput(),
) : EmiRecipeCategory(id, icon, simplified, sorter) {
    companion object {
        /**
         * 指定した引数から[HTEmiRecipeCategory]を作成します。
         * @param bounds レシピが表示される範囲
         * @param hasText カテゴリのタイトル
         * @param id カテゴリのID
         * @param workStations カテゴリに紐づいたアイテムの一覧
         * @param sorter レシピの表示順を定める[Comparator]
         * @return 新しい[HTEmiRecipeCategory]のインスタンス
         */
        @JvmStatic
        fun create(
            bounds: HTBounds,
            hasText: HTHasText,
            id: ResourceLocation,
            vararg workStations: ItemLike,
            sorter: Comparator<EmiRecipe> = EmiRecipeSorting.compareOutputThenInput(),
        ): HTEmiRecipeCategory = HTEmiRecipeCategory(
            bounds,
            hasText,
            workStations.map(ItemLike::toEmi),
            id,
            workStations[0].toEmi(),
            sorter = sorter,
        )
    }

    override fun getName(): Component = hasText.getText()
}
