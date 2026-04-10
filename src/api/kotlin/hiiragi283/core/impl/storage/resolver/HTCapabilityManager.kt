package hiiragi283.core.impl.storage.resolver

import net.minecraft.core.Direction

/**
 * 向きに応じたキャパビリティを取得するインターフェース
 * @param CONTAINER 要素を保持するクラス
 * @see mekanism.common.capabilities.resolver.ICapabilityResolver
 * @see mekanism.common.capabilities.resolver.manager.ICapabilityHandlerManager
 */
interface HTCapabilityManager<CONTAINER : Any> {
    /**
     * 指定された引数からキャパビリティを取得します。
     * @param T キャパビリティのクラス
     * @param side アクセスする面
     * @return 見つからない場合は`null`
     */
    fun <T : Any> resolve(side: Direction?): T?

    /**
     * このCapabilityが運用できるか判定します。
     */
    fun canHandle(): Boolean

    /**
     * 指定された[side]から[CONTAINER]の一覧を返します。
     */
    fun getContainers(side: Direction?): List<CONTAINER>
}
