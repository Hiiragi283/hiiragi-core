package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.register.HTDeferredRegister
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTDeferredCreativeTabRegister(namespace: String) :
    HTDeferredRegister<CreativeModeTab>(
        Registries.CREATIVE_MODE_TAB,
        namespace,
    ) {
    fun registerSimpleTab(
        name: String,
        title: HTTranslation,
        icon: Holder<Item>,
        builder: CreativeModeTab.DisplayItemsGenerator,
    ): HTDeferredHolder<CreativeModeTab, CreativeModeTab> = register(name) { _ ->
        CreativeModeTab
            .builder()
            .title(title.translate())
            .icon { ItemStack(icon) }
            .displayItems(builder)
            .build()
    }
}
