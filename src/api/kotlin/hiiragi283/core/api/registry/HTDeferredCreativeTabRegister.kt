package hiiragi283.core.api.registry

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

class HTDeferredCreativeTabRegister(namespace: String) : HTDeferredRegister<CreativeModeTab>(Registries.CREATIVE_MODE_TAB, namespace) {
    companion object {
        @Suppress("DEPRECATION")
        @JvmStatic
        fun addHoldersToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, holders: Sequence<SupplierWithId<ItemLike>>) {
            for (holder: SupplierWithId<ItemLike> in holders) {
                addToDisplay(parameters, output, holder.get().asItem().builtInRegistryHolder())
            }
        }

        @JvmStatic
        fun addToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, items: Sequence<Holder<Item>>) {
            for (item: Holder<Item> in items) {
                addToDisplay(parameters, output, item)
            }
        }

        /**
         * @see mekanism.common.registration.impl.CreativeTabDeferredRegister.addToDisplay
         */
        @JvmStatic
        fun addToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, vararg items: Holder<Item>) {
            val visibility: CreativeModeTab.TabVisibility = when (output) {
                is BuildCreativeModeTabContentsEvent -> CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
                else -> CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            }
            for (holder: Holder<Item> in items) {
                val stack = ItemStack(holder)
                if (stack.isEmpty) continue

                val item: Item = stack.item
                if (!item.isEnabled(parameters.enabledFeatures())) continue
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
    }

    fun registerSimpleTab(name: String, title: HTTranslation, icon: ItemLike, builder: CreativeModeTab.DisplayItemsGenerator): HTSimpleDeferredHolder<CreativeModeTab> = this.register(name) { _ ->
        CreativeModeTab
            .builder()
            .title(title.translate())
            .icon { ItemStack(icon) }
            .displayItems(builder)
            .build()
    }

    fun registerTab(name: String, title: HTTranslation, icon: ItemLike, builderAction: CreativeModeTab.Builder.() -> Unit): HTSimpleDeferredHolder<CreativeModeTab> = this.register(name) { _ ->
        CreativeModeTab
            .builder()
            .title(title.translate())
            .icon { ItemStack(icon) }
            .apply(builderAction)
            .build()
    }
}
