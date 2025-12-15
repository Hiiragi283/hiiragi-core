package hiiragi283.core.api.stack

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText

/**
 * 種類と量を保持する不変なインターフェース
 *
 * EMPTYなんか大っ嫌い！
 * @param TYPE 保持する種類のクラス
 * @param STACK [ImmutableStack]を実装したクラス
 */
interface ImmutableStack<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>> :
    HTHasText,
    HTIdLike {
    /**
     * スタックの種類を返します。
     * @return スタックの種類
     */
    fun getType(): TYPE

    /**
     * スタックの量を返します。
     * @return [Int]での量
     */
    fun getAmount(): Int

    /**
     * このスタックのコピーを指定した個数で返します。
     * @param amount コピー後の個数
     * @return 新しいスタックが無効の場合は`null`
     */
    fun copyWithAmount(amount: Int): STACK?
}
