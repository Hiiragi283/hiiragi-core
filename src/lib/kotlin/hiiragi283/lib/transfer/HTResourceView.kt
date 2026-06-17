package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import hiiragi283.lib.math.fixedFraction
import net.neoforged.neoforge.transfer.resource.Resource
import org.apache.commons.lang3.math.Fraction

/**
 * 単一のリソースを保持するインターフェースです。
 *
 * 参照 : [Mekanism - IResourceContainer](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/resource/IResourceContainer.java)
 * @param RESOURCE 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTResourceView<RESOURCE : Resource> {
    /**
     * 保持しているリソースの種類
     */
    val resource: RESOURCE

    /**
     * 保持しているリソースの量
     */
    val amountAsLong: Long

    /**
     * 保持しているリソースの量
     */
    val amountAsInt: Int get() = Ints.saturatedCast(amountAsLong)

    /**
     * このビューが空かどうか判定します。
     * @return [Resource.isEmpty]または[amountAsLong]が`0`以下の場合
     */
    fun isEmpty(): Boolean = HTResourceStack.isEmpty(resource, amountAsLong)

    /**
     * 容量を取得します。
     * @param resource 対象となるリソース
     * @return [resource]に対する容量
     */
    fun getCapacityAsLong(resource: RESOURCE): Long

    /**
     * 容量を取得します。
     * @param resource 対象となるリソース
     * @return [resource]に対する容量
     */
    fun getCapacityAsInt(resource: RESOURCE): Int = Ints.saturatedCast(getCapacityAsLong(resource))

    /**
     * 空き容量を取得します。
     */
    fun getNeededAsLong(resource: RESOURCE): Long = maxOf(0, getCapacityAsLong(resource) - amountAsLong)

    /**
     * 空き容量を取得します。
     */
    fun getNeededAsInt(resource: RESOURCE): Int = Ints.saturatedCast(getNeededAsLong(resource))

    /**
     * 占有率を取得します。
     */
    fun getFilledLevel(resource: RESOURCE): Fraction = fixedFraction(amountAsLong, getCapacityAsLong(resource))
}
