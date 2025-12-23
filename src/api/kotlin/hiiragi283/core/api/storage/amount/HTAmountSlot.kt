package hiiragi283.core.api.storage.amount

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction

/**
 * 量を搬入/搬出できることを表すインターフェースです。
 * @param N 保持する数値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
sealed interface HTAmountSlot<N> where N : Number, N : Comparable<N> {
    /**
     * このスロットが空かどうか判定します。
     */
    fun isEmpty(): Boolean

    /**
     * このスロットに量を搬入します。
     * @param amount 搬入する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬入される量
     */
    fun insert(amount: N, action: HTStorageAction, access: HTStorageAccess): N

    /**
     * このスロットから量を搬出します。
     * @param amount 搬出する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出される量
     */
    fun extract(amount: N, action: HTStorageAction, access: HTStorageAccess): N

    /**
     * [Int]値を扱う[HTAmountSlot]の拡張インターフェース
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    interface IntSized :
        HTAmountSlot<Int>,
        HTAmountView.IntSized {
        override fun isEmpty(): Boolean = getAmount() <= 0
    }

    /**
     * [Long]値を扱う[HTAmountSlot]の拡張インターフェース
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    interface LongSized :
        HTAmountSlot<Long>,
        HTAmountView.LongSized {
        override fun isEmpty(): Boolean = getAmount() <= 0
    }
}
