package hiiragi283.lib.registry

import hiiragi283.lib.gui.menu.type.HTMenuTypeWithContext
import hiiragi283.lib.text.Text
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

/**
 * [MenuType]向けの[HTDeferredHolder]の拡張クラスです。
 * @param MENU メニューのクラス
 * @param TYPE [MenuType]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed class HTDeferredMenuType<out MENU : AbstractContainerMenu, out TYPE : MenuType<@UnsafeVariance MENU>>(key: ResourceKey<MenuType<*>>) : HTDeferredHolder<MenuType<*>, TYPE>(key) {
    /**
     * 新しい[MenuProvider]のインスタンスを作成します。
     * @param title メニューのタイトル
     */
    fun getVanillaProvider(title: Text): MenuProvider = SimpleMenuProvider(
        { containerId: Int, inventory: Inventory, _ -> get().create(containerId, inventory) },
        title,
    )

    /**
     * [HTMenuTypeWithContext]向けの[HTDeferredMenuType]の実装クラスです。
     * @param MENU メニューのクラス
     * @param C コンテキストのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    class WithContext<out MENU : AbstractContainerMenu, out C>(key: ResourceKey<MenuType<*>>) : HTDeferredMenuType<MENU, HTMenuTypeWithContext<MENU, C>>(key) {
        /**
         * 新しい[MenuProvider]のインスタンスを作成します。
         * @param title メニューのタイトル
         * @param context メニューのコンテキスト
         * @return [context]の型が一致していない場合は`null`
         */
        fun getProvider(title: Text, context: Any): MenuProvider? = get().createOrNull(context)?.let { SimpleMenuProvider(it, title) }

        /**
         * メニューを開きます。
         * @param player メニューを開こうとしているプレイヤー
         * @param title メニューのタイトル
         * @param context メニューのコンテキスト
         * @param writer クライアント側にコンテキストの情報を送るブロック
         * @return メニューを開けたかどうか
         */
        fun openMenu(player: Player, title: Text, context: Any, writer: (RegistryFriendlyByteBuf) -> Unit): InteractionResult {
            if (player.level().isClientSide) {
                return InteractionResult.SUCCESS
            } else {
                val provider: MenuProvider = getProvider(title, context) ?: return InteractionResult.FAIL
                player.openMenu(provider, writer)
                return InteractionResult.CONSUME
            }
        }
    }
}
