package hiiragi283.core.common.registry.register

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLikeDelegate
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

class HTDeferredCreativeTabRegister(namespace: String) :
    HTDeferredRegister<CreativeModeTab>(
        Registries.CREATIVE_MODE_TAB,
        namespace,
    ) {
    companion object {
        @JvmStatic
        fun addHoldersToDisplay(
            parameters: CreativeModeTab.ItemDisplayParameters,
            output: CreativeModeTab.Output,
            holders: Sequence<HTHolderLike<out ItemLike, *>>,
        ) {
            for (holder: HTHolderLike<out ItemLike, *> in holders) {
                addToDisplay(parameters, output, holder.get().asItem().toLike())
            }
        }

        @JvmStatic
        fun addToDisplay(
            parameters: CreativeModeTab.ItemDisplayParameters,
            output: CreativeModeTab.Output,
            items: Sequence<HTItemHolderLike<*>>,
        ) {
            for (item: HTItemHolderLike<*> in items) {
                addToDisplay(parameters, output, item)
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

    fun registerSimpleTab(
        name: String,
        title: HTTranslation,
        icon: ItemLike,
        builder: CreativeModeTab.DisplayItemsGenerator,
    ): HTSimpleHolderLikeDelegate<CreativeModeTab> = delegate
        .register(name) { _ ->
            CreativeModeTab
                .builder()
                .title(title.translate())
                .icon { ItemStack(icon) }
                .displayItems(builder)
                .build()
        }.toLike()

    @HTBuilderMarker
    fun registerTab(
        name: String,
        title: HTTranslation,
        icon: ItemLike,
        builderAction: CreativeModeTab.Builder.() -> Unit,
    ): HTSimpleHolderLikeDelegate<CreativeModeTab> = delegate
        .register(name) { _ ->
            CreativeModeTab
                .builder()
                .title(title.translate())
                .icon { ItemStack(icon) }
                .apply(builderAction)
                .build()
        }.toLike()
}
