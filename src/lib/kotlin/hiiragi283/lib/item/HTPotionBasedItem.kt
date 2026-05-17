package hiiragi283.lib.item

import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion

/**
 * ポーションに基づいた[アイテム][Item]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
open class HTPotionBasedItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {

    override fun getCreatorModId(registries: HolderLookup.Provider, itemStack: ItemStack): String? = HTPotionHelper.getPotion(itemStack)
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .getOrNull()
        ?.identifier()
        ?.namespace
        ?: super.getCreatorModId(registries, itemStack)

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemLike<*>, context: HTSubCreativeTabContents.Context) {
        context.provider
            .lookupOrThrow(Registries.POTION)
            .filterFeatures(context.enabledFeatures)
            .listElements()
            .map(::BottledPotionContents)
            .map { HTPotionHelper.setContents(baseItem.toStack(), it) }
            .forEach(context)
    }

    override fun shouldAddDefault(): Boolean = false
}
