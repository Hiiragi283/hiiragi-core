package hiiragi283.lib.transfer.resolver

import net.minecraft.core.Direction

/**
 * 向きに応じたキャパビリティを取得するインターフェース
 * @param SLOT 要素を保持するクラス
 */
interface HTCapabilityManager<SLOT> {
    /**
     * 指定された引数からキャパビリティを取得します。
     * @param T キャパビリティのクラス
     * @param side アクセスする面
     * @return 見つからない場合は`null`
     */
    fun <T : Any> resolve(side: Direction?): T?

    /**
     * 指定された[side]から[SLOT]の一覧を返します。
     */
    fun getContainers(side: Direction?): List<SLOT>
}
