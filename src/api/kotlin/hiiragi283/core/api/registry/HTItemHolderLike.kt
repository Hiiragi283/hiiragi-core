package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * [HTIdLike]と[ItemLike]とその他諸々を継承した[HTIdLike]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTItemHolderLike<ITEM : Item> :
    HTIdLike,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    fun getItemHolder(): Holder<Item>

    fun getItemKey(): ResourceKey<Item> = getItemHolder().unwrapKey().orElseThrow()

    override fun asItem(): ITEM

    /**
     * @since 0.7.0
     */
    interface Delegated<ITEM : Item> : HTItemHolderLike<ITEM> {
        override fun getId(): ResourceLocation = getItemKey().location()

        override val translationKey: String get() = asItem().descriptionId

        override fun getText(): Component = asItem().description
    }
}
