package hiiragi283.lib.registry

import hiiragi283.lib.gui.menu.type.HTMenuTypeWithContext
import hiiragi283.lib.registry.HTDeferredHolder
import hiiragi283.lib.text.Text
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

sealed class HTDeferredMenuType<out MENU : AbstractContainerMenu, out TYPE : MenuType<@UnsafeVariance MENU>> : HTDeferredHolder<MenuType<*>, TYPE> {
    constructor(key: ResourceKey<MenuType<*>>) : super(key)

    constructor(id: Identifier) : super(Registries.MENU, id)

    fun getVanillaProvider(title: Text): MenuProvider = SimpleMenuProvider(
        { containerId: Int, inventory: Inventory, _ -> get().create(containerId, inventory) },
        title,
    )

    class WithContext<out MENU : AbstractContainerMenu, out C> : HTDeferredMenuType<MENU, HTMenuTypeWithContext<MENU, C>> {
        constructor(key: ResourceKey<MenuType<*>>) : super(key)

        constructor(id: Identifier) : super(id)

        fun getProvider(title: Text, context: Any): MenuProvider? = get().create(context)?.let { SimpleMenuProvider(it, title) }

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
