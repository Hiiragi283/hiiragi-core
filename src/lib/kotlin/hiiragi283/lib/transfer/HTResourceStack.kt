@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.resource.ResourceStack

/**
 * 数量を[Long]で管理する[ResourceStack]の代替クラスです。
 *
 * 参照 : [Mekanism - LargeResourceStack](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/resource/LargeResourceStack.java)
 * @param RESOURCE 保持するリソースのクラス
 * @param resource リソースの種類
 * @param amountAsLong リソースの量
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTResourceStack<RESOURCE : Resource> private constructor(val resource: RESOURCE, val amountAsLong: Long) {
    companion object {
        /**
         * [HTResourceStack]の[Codec]を作成します。
         * @param RESOURCE 保持するリソースのクラス
         * @param resource [RESOURCE]の[Codec]
         */
        @JvmStatic
        fun <RESOURCE : Resource> codec(resource: Codec<RESOURCE>): Codec<HTResourceStack<RESOURCE>> = HTCodecs.record { instance ->
            instance.group(
                resource.fieldOf("resource").forGetter(HTResourceStack<RESOURCE>::resource),
                HTCodecs.NON_NEGATIVE_LONG.fieldOf(HTConstants.AMOUNT).forGetter(HTResourceStack<RESOURCE>::amountAsLong),
            ).apply(instance, ::HTResourceStack)
        }

        /**
         * 新しい[HTResourceStack]のインスタンスを作成します。
         * @param RESOURCE 保持するリソースのクラス
         * @param resource リソースの種類
         * @param amount リソースの量
         * @return [isEmpty]が`true`の場合は`null`
         */
        @JvmStatic
        fun <RESOURCE : Resource> of(resource: RESOURCE, amount: Long): HTResourceStack<RESOURCE>? = when {
            isEmpty(resource, amount) -> null
            else -> HTResourceStack(resource, amount)
        }

        @JvmStatic
        operator fun <RESOURCE : Resource> invoke(resource: RESOURCE, amount: Long): HTResourceStack<RESOURCE>? = of(resource, amount)

        /**
         * 空かどうか判定します。
         * @return [Resource.isEmpty]または[amountAsLong]が`0`以下の場合
         */
        @JvmStatic
        fun isEmpty(resource: Resource, amount: Long): Boolean = resource.isEmpty || amount <= 0
    }

    /**
     * リソースの量
     */
    val amountAsInt: Int get() = Ints.saturatedCast(amountAsLong)

    /**
     * このスタックが空かどうか判定します。
     */
    fun isEmpty(): Boolean = isEmpty(resource, amountAsLong)

    /**
     * 別のオブジェクトに変換します。
     * @param T 戻り値の値
     * @param transform リソースの種類と量を変換するブロック
     * @return [transform]の戻り値
     */
    inline fun <T> mapAsLong(transform: (RESOURCE, Long) -> T): T {
        contract {
            callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
        }
        return transform(resource, amountAsLong)
    }

    /**
     * 別のオブジェクトに変換します。
     * @param T 戻り値の値
     * @param transform リソースの種類と量を変換するブロック
     * @return [transform]の戻り値
     */
    inline fun <T> mapAsInt(transform: (RESOURCE, Int) -> T): T {
        contract {
            callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
        }
        return transform(resource, amountAsInt)
    }
}
