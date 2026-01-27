package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.common.gui.factory.HTItemWidgetHolderContext
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension

object HCMenuTypes {
    @JvmField
    val REGISTER: HTDeferredRegister<MenuType<*>> = HTDeferredRegister(Registries.MENU, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val BLOCK: HTDeferredHolder<MenuType<*>, MenuType<HTWidgetContainerMenu>> = REGISTER.register(
        "block",
    ) { _: ResourceLocation -> IMenuTypeExtension.create(HTBlockWidgetHolderContext::create) }

    @JvmField
    val ITEM: HTDeferredHolder<MenuType<*>, MenuType<HTWidgetContainerMenu>> = REGISTER.register(
        "item",
    ) { _: ResourceLocation -> IMenuTypeExtension.create(HTItemWidgetHolderContext::create) }
}
