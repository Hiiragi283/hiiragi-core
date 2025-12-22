package hiiragi283.core.common.registry.register

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

class HTDeferredCreativeTabRegister(namespace: String) :
    HTDeferredRegister<CreativeModeTab>(
        Registries.CREATIVE_MODE_TAB,
        namespace,
    ) {
    companion object {
        @JvmStatic
        fun addToDisplay(
            parameters: CreativeModeTab.ItemDisplayParameters,
            output: CreativeModeTab.Output,
            items: Iterable<HTItemHolderLike<*>>,
        ) {
            for (like: HTItemHolderLike<*> in items) {
                addToDisplay(parameters, output, like)
            }
        }

        @JvmStatic
        fun addToDisplay(
            parameters: CreativeModeTab.ItemDisplayParameters,
            output: CreativeModeTab.Output,
            items: Sequence<HTItemHolderLike<*>>,
        ) {
            for (like: HTItemHolderLike<*> in items) {
                addToDisplay(parameters, output, like)
            }
        }

        /**
         * @see mekanism.common.registration.impl.CreativeTabDeferredRegister.addToDisplay
         */
        @JvmStatic
        fun addToDisplay(
            parameters: CreativeModeTab.ItemDisplayParameters,
            output: CreativeModeTab.Output,
            vararg items: HTItemHolderLike<*>,
        ) {
            val visibility: CreativeModeTab.TabVisibility = when (output) {
                is BuildCreativeModeTabContentsEvent -> CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
                else -> CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            }
            for (like: HTItemHolderLike<*> in items) {
                val item: Item = like.asItem()
                if (!item.isEnabled(parameters.enabledFeatures())) continue
                if (item is HTSubCreativeTabContents) {
                    if (item.shouldAddDefault()) {
                        output.accept(item, visibility)
                    }
                    item.addItems(like, parameters) { output.accept(it, visibility) }
                } else {
                    output.accept(item, visibility)
                }
            }
        }
    }

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
