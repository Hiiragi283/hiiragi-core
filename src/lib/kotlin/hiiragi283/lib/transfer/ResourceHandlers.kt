@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import hiiragi283.lib.math.fixedFraction
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.neoforged.neoforge.transfer.RangedResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import org.apache.commons.lang3.math.Fraction

/**
 * この[ResourceHandler][this]の有効なインデックスの範囲を返します。
 *
 * 参照 : [Kotlin - Collection.indices][Collection.indices]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val ResourceHandler<*>.indices: IntRange get() = (0..<size())

val ResourceHandler<out Resource>.isEmpty: Boolean get() = ResourceHandlerUtil.isEmpty(this)

fun <T : Resource> ResourceHandler<T>.getNeededAsLong(index: Int, resource: T = getResource(index)): Long = maxOf(0, getCapacityAsLong(index, resource) - getAmountAsLong(index))

fun <T : Resource> ResourceHandler<T>.getNeededAsInt(index: Int, resource: T = getResource(index)): Int = Ints.saturatedCast(this.getNeededAsLong(index, resource))

fun <T : Resource> ResourceHandler<T>.getFilledLevel(index: Int, resource: T = getResource(index)): Fraction = fixedFraction(getAmountAsLong(index), getCapacityAsLong(index, resource))

fun <T : Resource> ResourceHandler<T>.extractSelf(index: Int, amount: Int = this.getAmountAsInt(index), transaction: TransactionContext): Int = this.extract(index, this.getResource(index), amount, transaction)

// Ranged
fun <T : Resource> ResourceHandler<T>.ranged(start: Int, end: Int): ResourceHandler<T> = RangedResourceHandler.of(this, start, end)

@Suppress("DEPRECATION")
infix fun <T : Resource> ResourceHandler<T>.ranged(range: IntRange): ResourceHandler<T> = this.ranged(range.first, range.endExclusive)

//    Transaction    //

/**
 * [Transaction]を安全に使用します。
 * @param T 戻り値のクラス
 * @param parent 現在開いている親のトランザクション
 * @param action 現在のトランザクションを使用するブロック
 * @return [action]の戻り値
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <T> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> T): T {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    return Transaction.open(parent).use(action)
}
