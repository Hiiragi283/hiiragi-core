package hiiragi283.lib.gui.menu.type

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

fun interface HTContainerFactory<out MENU : AbstractContainerMenu, out C> {
    fun create(containerId: Int, inventory: Inventory, context: @UnsafeVariance C): MENU

    fun interface Sided<out MENU : AbstractContainerMenu, out C> : HTContainerFactory<MENU, C> {
        fun create(containerId: Int, inventory: Inventory, context: @UnsafeVariance C, isRemote: Boolean): MENU

        override fun create(containerId: Int, inventory: Inventory, context: @UnsafeVariance C): MENU = create(containerId, inventory, context, false)
    }
}
