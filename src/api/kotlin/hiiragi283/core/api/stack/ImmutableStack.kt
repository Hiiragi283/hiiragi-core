package hiiragi283.core.api.stack

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText

/**
 * 種類と量を保持する不変なオブジェクトを表すインターフェースです。
 *
 * EMPTYなんか大っ嫌い！
 * @param TYPE 保持する種類のクラス
 * @param STACK [ImmutableStack]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface ImmutableStack<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>> :
    HTHasText,
    HTIdLike {
    /**
     * 保持している種類を返します。
     * @return [TYPE]型の値
     */
    fun type(): TYPE

    /**
     * 保持している量を返します。
     * @return [Int]型での量
     */
    fun amount(): Int

    /**
     * このスタックを指定した量でコピーします。
     * @param amount コピー後の量
     * @return 新しいスタックが無効の場合は`null`
     */
    fun copyWithAmount(amount: Int): STACK?
}
