package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.fixedFraction
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
interface HTResourceView<RESOURCE : HTResourceType<*>> : HTAmountView {
    /**
     * 保持しているリソースを取得します。
     */
    fun getResource(): RESOURCE?

    /**
     * 指定した[resource]から容量を取得します。
     */
    fun getCapacity(resource: RESOURCE?): Int

    /**
     * 指定した[resource]から空き容量を取得します。
     */
    fun getNeeded(resource: RESOURCE?): Int = max(0, getCapacity(resource) - getAmount())

    /**
     * 指定した[resource]から占有率を取得します。
     * @return [Fraction]型での占有率
     */
    fun getLevelAsFraction(resource: RESOURCE?): Fraction = fixedFraction(getAmount(), getCapacity(resource))

    /**
     * 占有率を返します。
     * @return [Float]型での占有率
     * @since v0.7.0
     */
    fun getLevelAsFloat(resource: RESOURCE?): Float = getLevelAsFraction(resource).toFloat()

    override fun getCapacity(): Int = getCapacity(null)

    override fun isEmpty(): Boolean = super.isEmpty() || getResource() == null

    //    Mutable    //

    /**
     * @since 0.9.0
     */
    abstract class Mutable<RESOURCE : HTResourceType<*>> :
        HTAmountView.Mutable(),
        HTResourceView<RESOURCE> {
        /**
         * 指定した[resource]で中身を置換します。
         */
        abstract fun setResource(resource: RESOURCE?)
    }
}
