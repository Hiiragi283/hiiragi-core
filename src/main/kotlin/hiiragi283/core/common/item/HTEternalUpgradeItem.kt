package hiiragi283.core.common.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.text.withStyle
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.network.chat.Component
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SmithingTemplateItem

class HTEternalUpgradeItem :
    SmithingTemplateItem(
        HCTranslation.ETERNAL_UPGRADE_APPLIES_TO.translateColored(HTDefaultColor.BLUE),
        HCTranslation.ETERNAL_UPGRADE_INGREDIENTS.translateColored(HTDefaultColor.BLUE),
        HCTranslation.ETERNAL_UPGRADE.translateColored(HTDefaultColor.GRAY),
        HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION.translate(),
        HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION.translate(),
        listOf(
            "item/empty_armor_slot_helmet",
            "item/empty_armor_slot_chestplate",
            "item/empty_armor_slot_leggings",
            "item/empty_armor_slot_boots",
            "item/empty_slot_hoe",
            "item/empty_slot_axe",
            "item/empty_slot_sword",
            "item/empty_slot_shovel",
            "item/empty_slot_pickaxe",
        ).map(HTConst.MINECRAFT::toId),
        listOf(HTConst.MINECRAFT.toId("item", "empty_slot_ingot")),
    ) {
    override fun getName(stack: ItemStack): Component = translatableText(getDescriptionId(stack)).withStyle(HTDefaultColor.RED)

    override fun isFoil(stack: ItemStack): Boolean = true

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false
}
