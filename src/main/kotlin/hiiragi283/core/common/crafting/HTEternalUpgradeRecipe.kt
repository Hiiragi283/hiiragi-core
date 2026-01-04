package hiiragi283.core.common.crafting

import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTEternalUpgradeRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var damageable = 0
        var component = 0
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (stack.isDamageableItem) {
                damageable++
            } else if (stack.`is`(HCItems.ETERNAL_TICKET)) {
                component++
            }
        }
        return damageable == 1 && component == 1
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var item: ItemStack = ItemStack.EMPTY
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (stack.isDamageableItem) {
                item = stack.copy()
                break
            }
        }
        if (!item.isEmpty) {
            item.set(DataComponents.UNBREAKABLE, Unbreakable(true))
        }
        return item
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.ETERNAL_UPGRADE
}
