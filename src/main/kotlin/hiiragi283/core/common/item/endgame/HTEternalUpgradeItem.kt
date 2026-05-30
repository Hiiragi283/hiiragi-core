package hiiragi283.core.common.item.endgame

import hiiragi283.core.api.text.HCTranslation
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.item.HTSmithingTemplateItem
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.withStyle
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

class HTEternalUpgradeItem(properties: Properties) : HTSmithingTemplateItem(HCTranslation.ETERNAL_UPGRADE_APPLIES_TO, HCTranslation.ETERNAL_UPGRADE_INGREDIENTS, HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION, HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, properties) {
    override fun getName(stack: ItemStack): Text = super.getName(stack).copy().withStyle(HTDefaultColor.RED)

    override fun isFoil(stack: ItemStack): Boolean = true

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false
}
