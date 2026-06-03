package hiiragi283.lib.gui.menu.type

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.neoforged.neoforge.network.IContainerFactory

class HTMenuTypeWithContext<out MENU : AbstractContainerMenu, out C>(private val clazz: Class<C>, factory: HTContainerFactory<MENU, C>, constructor: IContainerFactory<MENU>) :
    HTMenuType<MENU, HTContainerFactory<MENU, C>>(factory, constructor),
    HTContainerFactory<MENU, C> by factory {

    fun createOrNull(containerId: Int, inventory: Inventory, context: Any): MENU? = when {
        clazz.isInstance(context) -> this.create(containerId, inventory, clazz.cast(context))
        else -> null
    }

    fun createOrNull(context: Any): MenuConstructor? = when {
        clazz.isInstance(context) -> MenuConstructor { containerId: Int, inventory: Inventory, _ -> this.create(containerId, inventory, clazz.cast(context)) }
        else -> null
    }
}
