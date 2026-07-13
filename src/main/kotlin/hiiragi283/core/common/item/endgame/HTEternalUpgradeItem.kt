package hiiragi283.core.common.item.endgame

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.item.HTSmithingTemplateItem
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.text.withStyle
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

class HTEternalUpgradeItem :
    HTSmithingTemplateItem(
        HCTranslation.ETERNAL_UPGRADE_APPLIES_TO,
        HCTranslation.ETERNAL_UPGRADE_INGREDIENTS,
        HCTranslation.ETERNAL_UPGRADE_DESC,
        HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION,
        HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
    ) {
    override fun getName(stack: ItemStack): Text = translatableText(getDescriptionId(stack)).withStyle(HTDefaultColor.RED)

    override fun isFoil(stack: ItemStack): Boolean = true

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false
}
