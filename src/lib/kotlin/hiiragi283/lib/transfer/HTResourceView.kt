package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import hiiragi283.lib.math.fixedFraction
import net.neoforged.neoforge.transfer.resource.Resource
import org.apache.commons.lang3.math.Fraction

/**
 * 単一の不変のリソースを保持するインターフェースです。
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTResourceSlot
 */
interface HTResourceView<T : Resource> {
    fun getResource(): T

    fun getAmountAsLong(): Long

    fun getAmountAsInt(): Int = Ints.saturatedCast(getAmountAsLong())

    fun getCapacityAsLong(resource: T): Long

    fun getCapacityAsInt(resource: T): Int = Ints.saturatedCast(getCapacityAsLong(resource))

    /**
     * 指定した[resource]から空き容量を取得します。
     */
    fun getNeededAsLong(resource: T): Long = maxOf(0, getCapacityAsLong(resource) - getAmountAsLong())

    fun getNeededAsInt(resource: T): Int = Ints.saturatedCast(getNeededAsLong(resource))

    fun getFilledLevel(resource: T): Fraction = fixedFraction(getAmountAsLong(), getCurrentCapacityAsLong())

    fun getCurrentCapacityAsLong(): Long = getCapacityAsLong(getResource())

    fun getCurrentCapacityAsInt(): Int = Ints.saturatedCast(getCurrentCapacityAsLong())
}
