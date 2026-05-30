package hiiragi283.core.common.item.endgame

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.HTColoredNameItem
import net.minecraft.network.chat.TextColor
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

open class HTCreativeItem(properties: Properties) : HTColoredNameItem(properties) {
    override fun getNameColor(stack: ItemStack): TextColor = HTDefaultColor.RED.textColor

    override fun isFoil(stack: ItemStack): Boolean = true

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false
}
