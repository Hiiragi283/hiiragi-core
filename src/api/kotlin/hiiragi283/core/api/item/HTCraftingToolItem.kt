package hiiragi283.core.api.item

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.extensions.IItemExtension

interface HTCraftingToolItem : IItemExtension {
    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean = stack.isDamageableItem || stack.damageValue <= stack.maxDamage

    override fun getCraftingRemainingItem(itemStack: ItemStack): ItemStack {
        val copies: ItemStack = itemStack.copy()
        if (copies.damageValue == copies.maxDamage) {
            return ItemStack.EMPTY
        }
        if (copies.isDamageableItem) {
            copies.damageValue += 1
        }
        return copies
    }
}
