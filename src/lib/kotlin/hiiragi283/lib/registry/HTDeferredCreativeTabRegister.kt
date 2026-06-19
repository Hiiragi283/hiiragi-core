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

/**
 * [クリエタブ][CreativeModeTab]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredCreativeTabRegister(namespace: String) : HTDeferredRegister<CreativeModeTab>(Registries.CREATIVE_MODE_TAB, namespace) {
    companion object {
        /**
         * [HTSubCreativeTabContents]に基づいてアイテムをクリエタブに追加します。
         * @param parameters レジストリへのアクセスなどのコンテキスト
         * @param output クリエタブに追加するアイテムの登録先
         * @param items クリエタブに追加するアイテムの一覧
         */
        @JvmStatic
        fun addToDisplay(parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, items: Sequence<Holder<Item>>) {
            for (item: Holder<Item> in items) {
                addToDisplay(parameters, output, item)
            }
        }

        /**
         * [HTSubCreativeTabContents]に基づいてアイテムをクリエタブに追加します。
         * @param parameters レジストリへのアクセスなどのコンテキスト
         * @param output クリエタブに追加するアイテムの登録先
         * @param holder クリエタブに追加するアイテム
         */
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
                item.addItems(holder, parameters, output)
            } else {
                output.accept(stack, visibility)
            }
        }
    }

    /**
     * 新しい[クリエタブ][CreativeModeTab]を登録します。
     * @param name クリエタブのIDのパス
     * @param title クリエタブのタイトル
     * @param icon クリエタブのアイコン
     * @param builder クリエタブの要素を初期化するブロック
     * @return 新しい[DeferredHolder]のインスタンス
     */
    fun registerSimpleTab(name: String, title: HTTranslation, icon: ItemLike, builder: CreativeModeTab.DisplayItemsGenerator): DeferredHolder<CreativeModeTab, CreativeModeTab> = this.register(name) { _ ->
        CreativeModeTab
            .builder()
            .title(title.translate())
            .icon { ItemStack(icon) }
            .displayItems(builder)
            .build()
    }

    /**
     * 新しい[クリエタブ][CreativeModeTab]を登録します。
     * @param name クリエタブのIDのパス
     * @param title クリエタブのタイトル
     * @param icon クリエタブのアイコン
     * @param builderAction [CreativeModeTab.Builder]を初期化するブロック
     * @return 新しい[DeferredHolder]のインスタンス
     */
    fun registerTab(name: String, title: HTTranslation, icon: ItemLike, builderAction: CreativeModeTab.Builder.() -> Unit): DeferredHolder<CreativeModeTab, CreativeModeTab> = this.register(name) { _ ->
        CreativeModeTab
            .builder()
            .title(title.translate())
            .icon { ItemStack(icon) }
            .apply(builderAction)
            .build()
    }
}
