package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.math.fixedFraction
import hiiragi283.core.api.storage.amount.HTAmountView
import org.apache.commons.lang3.math.Fraction
import kotlin.math.max

/**
 * 単一の不変のリソースを保持するインターフェースです。
 * @param RESOURCE 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTResourceSlot
 */
interface HTResourceView<RESOURCE : HTResourceType<*>> : HTAmountView.IntSized {
    /**
     * 保持しているリソースを取得します。
     */
    fun getResource(): RESOURCE?

    /**
     * 指定した[resource]から容量を取得します。
     * @return [Int]型での容量
     */
    fun getCapacity(resource: RESOURCE?): Int

    /**
     * 指定した[resource]から空き容量を取得します。
     * @return [Int]型での空き容量
     */
    fun getNeeded(resource: RESOURCE?): Int = max(0, getCapacity(resource) - getAmount())

    /**
     * 指定した[resource]から占有率を取得します。
     * @return [Fraction]型での占有率
     */
    fun getStoredLevel(resource: RESOURCE?): Fraction = fixedFraction(getAmount(), getCapacity(resource))

    override fun getCapacity(): Int = getCapacity(null)
}
