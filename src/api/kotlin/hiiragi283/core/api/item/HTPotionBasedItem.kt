package hiiragi283.core.api.item

import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLikeDelegate
import hiiragi283.core.api.registry.asSequence
import hiiragi283.core.api.text.Text
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.Potion

/**
 * ポーションに基づいた[アイテム][Item]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
open class HTPotionBasedItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {
        HTPotionHelper.getPotion(stack).addPotionTooltip(tooltips::add, 1f, context.tickRate())
    }

    override fun getCreatorModId(itemStack: ItemStack): String? =
        HTPotionHelper.getPotionModId(itemStack) ?: super.getCreatorModId(itemStack)

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        context.provider
            .lookupOrThrow(Registries.POTION)
            .filterFeatures(context.enabledFeatures)
            .asSequence()
            .mapNotNull { holder: HTSimpleHolderLikeDelegate<Potion> ->
                val potion: Holder<Potion> = holder.getHolder()
                val contents: HTPotionContents = HTPotionContents.of(potion, HTBottleType.DEFAULT) ?: return@mapNotNull null
                HTPotionHelper.setContents(baseItem.toStack(), contents)
            }.forEach(context)
    }

    override fun shouldAddDefault(): Boolean = false
}
