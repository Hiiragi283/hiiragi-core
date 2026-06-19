package hiiragi283.lib.registry

import hiiragi283.lib.gui.menu.type.HTContainerFactory
import hiiragi283.lib.gui.menu.type.HTMenuTypeWithContext
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

private typealias ClientContextDecoder<C> = (RegistryFriendlyByteBuf?) -> C

/**
 * [MenuType]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredMenuTypeRegister(namespace: String) : HTDeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    /**
     * 新しい[MenuType]を登録します。
     * @param MENU メニューのクラス
     * @param C コンテキストのクラス
     * @param name [MenuType]のIDのパス
     * @param factory メニューを作成するブロック
     * @param decoder クライアント側でパケットからコンテキストに変換するブロック
     * @return 新しい[HTDeferredMenuType.WithContext]のインスタンス
     */
    inline fun <MENU : AbstractContainerMenu, reified C : Any> registerType(
        name: String,
        factory: HTContainerFactory<MENU, C>,
        noinline decoder: ClientContextDecoder<C>,
    ): HTDeferredMenuType.WithContext<MENU, C> = registerType(C::class.java, name, factory, decoder)

    /**
     * 新しい[MenuType]を登録します。
     * @param MENU メニューのクラス
     * @param C コンテキストのクラス
     * @param clazz コンテキストのクラス
     * @param name [MenuType]のIDのパス
     * @param factory メニューを作成するブロック
     * @param decoder クライアント側でパケットからコンテキストに変換するブロック
     * @return 新しい[HTDeferredMenuType.WithContext]のインスタンス
     */
    fun <MENU : AbstractContainerMenu, C> registerType(
        clazz: Class<C>,
        name: String,
        factory: HTContainerFactory<MENU, C>,
        decoder: ClientContextDecoder<C>,
    ): HTDeferredMenuType.WithContext<MENU, C> {
        val holder = HTDeferredMenuType.WithContext<MENU, C>(createKey(name))
        register(name) { _ ->
            HTMenuTypeWithContext(clazz, factory) { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf? ->
                factory.create(containerId, inventory, decoder(buf))
            }
        }
        return holder
    }
}
