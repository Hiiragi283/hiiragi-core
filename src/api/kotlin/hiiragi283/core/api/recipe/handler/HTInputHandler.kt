package hiiragi283.core.api.recipe.handler

import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.Optional

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param INGREDIENT 対象となる材料のクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.IInputHandler
 */
interface HTInputHandler<INGREDIENT : Any> {
    /**
     * 指定した[材料][ingredient]から消費される数量を取得します。
     * @return [ingredient]が要求する数量
     */
    fun getMatchingAmount(ingredient: INGREDIENT): Int

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: INGREDIENT?, transaction: TransactionContext) {
        ingredient?.let(::getMatchingAmount)?.let { consume(it, transaction) }
    }

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: Optional<out INGREDIENT>, transaction: TransactionContext) {
        ingredient.ifPresent { consume(it, transaction) }
    }

    /**
     * 指定した[数量][amount]だけ中身を消費します。
     */
    fun consume(amount: Int, transaction: TransactionContext)
}
