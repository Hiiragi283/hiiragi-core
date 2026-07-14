package hiiragi283.core.api.item

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.text.Text
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

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

    override fun getCreatorModId(itemStack: ItemStack): String? = HTPotionHelper.getPotionModId(itemStack) ?: super.getCreatorModId(itemStack)

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: Holder<Item>, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output) {
        parameters.holders()
            .lookupOrThrow(Registries.POTION)
            .filterFeatures(parameters.enabledFeatures)
            .listElements()
            .map(::BottledPotionContents)
            .map { HTPotionHelper.setContents(ItemStack(baseItem), it) }
            .forEach(output::accept)
    }

    override fun shouldAddDefault(): Boolean = false
}
