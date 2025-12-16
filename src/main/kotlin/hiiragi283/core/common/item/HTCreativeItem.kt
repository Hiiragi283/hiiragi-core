package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTColoredNameItem
import net.minecraft.ChatFormatting
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

class HTCreativeItem(properties: Properties) : HTColoredNameItem(properties) {
    override fun getNameColor(stack: ItemStack): ChatFormatting? = ChatFormatting.RED

    override fun isFoil(stack: ItemStack): Boolean = true

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false
}
