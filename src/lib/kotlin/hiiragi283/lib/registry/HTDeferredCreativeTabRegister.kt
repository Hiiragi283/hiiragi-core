package hiiragi283.lib.registry

import hiiragi283.lib.item.HTSubCreativeTabContents
import hiiragi283.lib.text.HTTranslation
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredHolder

class HTDeferredCreativeTabRegister(namespace: String) : HTDeferredRegister<CreativeModeTab>(Registries.CREATIVE_MODE_TAB, namespace) {
    companion object {
        @JvmStatic
        fun addToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, items: Sequence<Holder<Item>>) {
            for (item: Holder<Item> in items) {
                addToDisplay(parameters, output, item)
            }
        }

        @JvmStatic
        fun addToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, holder: Holder<Item>) {
            val visibility: CreativeModeTab.TabVisibility = when (output) {
                is BuildCreativeModeTabContentsEvent -> CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
                else -> CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            }
            val stack = ItemStack(holder)
            if (stack.isEmpty) return

            val item: Item = stack.item
            if (!item.isEnabled(parameters.enabledFeatures())) return
            if (item is HTSubCreativeTabContents) {
                if (item.shouldAddDefault()) {
                    output.accept(stack, visibility)
                }
                item.addItems(holder, HTSubCreativeTabContents.Context(parameters, output))
            } else {
                output.accept(stack, visibility)
            }
        }
    }

    fun registerSimpleTab(name: String, title: HTTranslation, icon: ItemLike, builder: CreativeModeTab.DisplayItemsGenerator): DeferredHolder<CreativeModeTab, CreativeModeTab> = this.register(name) { _ ->
        CreativeModeTab
            .builder()
            .title(title.translate())
            .icon { ItemStack(icon) }
            .displayItems(builder)
            .build()
    }

    fun registerTab(name: String, title: HTTranslation, icon: ItemLike, builderAction: CreativeModeTab.Builder.() -> Unit): DeferredHolder<CreativeModeTab, CreativeModeTab> = this.register(name) { _ ->
        CreativeModeTab
            .builder()
            .title(title.translate())
            .icon { ItemStack(icon) }
            .apply(builderAction)
            .build()
    }
}
