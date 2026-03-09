package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.common.gui.factory.HTItemWidgetHolderContext
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object HCMenuTypes {
    @JvmField
    val REGISTER: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val BLOCK: DeferredHolder<MenuType<*>, MenuType<HTWidgetContainerMenu>> = REGISTER.register(
        "block",
    ) { _: ResourceLocation -> IMenuTypeExtension.create(HTBlockWidgetHolderContext::create) }

    @JvmField
    val ITEM: DeferredHolder<MenuType<*>, MenuType<HTWidgetContainerMenu>> = REGISTER.register(
        "item",
    ) { _: ResourceLocation -> IMenuTypeExtension.create(HTItemWidgetHolderContext::create) }
}
