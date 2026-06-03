package hiiragi283.lib.registry

import hiiragi283.lib.gui.menu.type.HTContainerFactory
import hiiragi283.lib.gui.menu.type.HTMenuTypeWithContext
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

private typealias ClientContextDecoder<C> = (RegistryFriendlyByteBuf?) -> C

class HTDeferredMenuTypeRegister(namespace: String) : HTDeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    inline fun <MENU : AbstractContainerMenu, reified C : Any> registerType(
        name: String,
        factory: HTContainerFactory<MENU, C>,
        noinline decoder: ClientContextDecoder<C>,
    ): HTDeferredMenuType.WithContext<MENU, C> = registerType(C::class.java, name, factory, decoder)

    fun <MENU : AbstractContainerMenu, C> registerType(
        clazz: Class<C>,
        name: String,
        factory: HTContainerFactory<MENU, C>,
        decoder: ClientContextDecoder<C>,
    ): HTDeferredMenuType.WithContext<MENU, C> {
        val holder = HTDeferredMenuType.WithContext<MENU, C>(createId(name))
        register(name) { _ ->
            HTMenuTypeWithContext(clazz, factory) { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf? ->
                factory.create(containerId, inventory, decoder(buf))
            }
        }
        return holder
    }
}
