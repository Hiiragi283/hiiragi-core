package hiiragi283.lib.registry

import hiiragi283.lib.item.HTItemLike
import hiiragi283.lib.item.HTSubCreativeTabContents
import hiiragi283.lib.resource.SupplierWithId
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
        @Suppress("DEPRECATION")
        @JvmStatic
        fun addHoldersToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, holders: Sequence<SupplierWithId<ItemLike>>) {
            for (holder: SupplierWithId<ItemLike> in holders) {
                addToDisplay(parameters, output, { holder.get().asItem() })
            }
        }

        @JvmStatic
        fun addToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, items: Sequence<Holder<Item>>) {
            for (item: Holder<Item> in items) {
                addToDisplay(parameters, output, { item.value() })
            }
        }

        /**
         * @see mekanism.common.registration.impl.CreativeTabDeferredRegister.addToDisplay
         */
        @JvmStatic
        fun addToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, vararg items: HTItemLike<*>) {
            val visibility: CreativeModeTab.TabVisibility = when (output) {
                is BuildCreativeModeTabContentsEvent -> CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
                else -> CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            }
            for (like: HTItemLike<*> in items) {
                val stack: ItemStack = like.toStack()
                if (stack.isEmpty) continue

                val item: Item = stack.item
                if (!item.isEnabled(parameters.enabledFeatures())) continue
                if (item is HTSubCreativeTabContents) {
                    if (item.shouldAddDefault()) {
                        output.accept(stack, visibility)
                    }
                    item.addItems(like, HTSubCreativeTabContents.Context(parameters, output))
                } else {
                    output.accept(stack, visibility)
                }
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
