package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.level.material.Fluid

class HTPotionBucketItem(content: Fluid, properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {
    override fun getName(stack: ItemStack): Component = translatableText(descriptionId, translatableText(getDescriptionId(stack)))

    override fun getDescriptionId(stack: ItemStack): String =
        HTPotionHelper.getPotionName(stack, stack.getOrDefault(HCDataComponents.BOTTLE_TYPE, HTBottleType.DEFAULT))

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        HTPotionHelper.getPotion(stack).addPotionTooltip(tooltips::add, 1f, context.tickRate())
    }

    override fun getCreatorModId(itemStack: ItemStack): String? = HTPotionHelper
        .getPotion(itemStack)
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::location)
        .map(ResourceLocation::getNamespace)
        .orElse(super.getCreatorModId(itemStack))

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        context.provider
            .lookupOrThrow(Registries.POTION)
            .listElements()
            .map { potion: Holder.Reference<Potion> -> HTPotionHelper.createPotion(baseItem, potion) }
            .forEach(context)
    }

    override fun shouldAddDefault(): Boolean = false
}
