package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import hiiragi283.lib.math.fixedFraction
import net.neoforged.neoforge.transfer.resource.Resource
import org.apache.commons.lang3.math.Fraction

/**
 * 単一の不変のリソースを保持するインターフェースです。
 * @param RESOURCE 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTResourceSlot
 */
interface HTResourceView<RESOURCE : Resource> {
    val resource: RESOURCE

    val amountAsLong: Long

    val amountAsInt: Int get() = Ints.saturatedCast(amountAsLong)

    fun isEmpty(): Boolean = resource.isEmpty || amountAsLong <= 0

    fun getCapacityAsLong(resource: RESOURCE): Long

    fun getCapacityAsInt(resource: RESOURCE): Int = Ints.saturatedCast(getCapacityAsLong(resource))

    /**
     * 指定した[resource]から空き容量を取得します。
     */
    fun getNeededAsLong(resource: RESOURCE): Long = maxOf(0, getCapacityAsLong(resource) - amountAsLong)

    fun getNeededAsInt(resource: RESOURCE): Int = Ints.saturatedCast(getNeededAsLong(resource))

    fun getFilledLevel(resource: RESOURCE): Fraction = fixedFraction(amountAsLong, getCurrentCapacityAsLong())

    fun getCurrentCapacityAsLong(): Long = getCapacityAsLong(resource)

    fun getCurrentCapacityAsInt(): Int = Ints.saturatedCast(getCurrentCapacityAsLong())
}
