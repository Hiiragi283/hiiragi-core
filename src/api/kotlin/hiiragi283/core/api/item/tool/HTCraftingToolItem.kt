package hiiragi283.core.api.item.tool

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TieredItem

class HTCraftingToolItem(tier: Tier, properties: Properties) : TieredItem(tier, properties) {
    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean = stack.damageValue <= stack.maxDamage

    override fun getCraftingRemainingItem(itemStack: ItemStack): ItemStack {
        if (itemStack.damageValue == itemStack.maxDamage) {
            return ItemStack.EMPTY
        }
        val stack: ItemStack = itemStack.copy()
        stack.damageValue++
        return stack
    }
}
