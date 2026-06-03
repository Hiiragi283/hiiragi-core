package hiiragi283.lib.gui.menu.type

import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.network.IContainerFactory

abstract class HTMenuType<out MENU : AbstractContainerMenu, out FACTORY>(factory: FACTORY, constructor: IContainerFactory<MENU>) : MenuType<@UnsafeVariance MENU>(constructor, FeatureFlags.VANILLA_SET)
