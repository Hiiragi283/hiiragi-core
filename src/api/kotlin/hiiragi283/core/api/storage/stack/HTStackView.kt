package hiiragi283.core.api.storage.stack

import hiiragi283.core.api.math.fixedFraction
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.storage.amount.HTAmountView
import org.apache.commons.lang3.math.Fraction
import kotlin.math.max

/**
 * 単一の不変のスタックを保持するインターフェースです。
 * @param STACK 保持するスタックのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTStackSlot
 */
interface HTStackView<STACK : ImmutableStack<*, STACK>> : HTAmountView.IntSized {
    /**
     * 保持しているスタックを取得します。
     */
    fun getStack(): STACK?

    /**
     * 指定した[stack]から容量を取得します。
     * @return [Int]型での容量
     */
    fun getCapacity(stack: STACK?): Int

    /**
     * 指定した[stack]から空き容量を取得します。
     * @return [Int]型での空き容量
     */
    fun getNeeded(stack: STACK?): Int = max(0, getCapacity(stack) - getAmount())

    /**
     * 指定した[stack]から占有率を取得します。
     * @return [Fraction]型での占有率
     */
    fun getStoredLevel(stack: STACK?): Fraction = fixedFraction(getAmount(), getCapacity(stack))

    override fun getAmount(): Int = getStack()?.amount() ?: 0

    override fun getCapacity(): Int = getCapacity(null)
}
