package hiiragi283.core.common.recipe.custom

import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.support.crafting.HTCustomRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTBlueprintCloningRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    private fun getBlueprints(input: List<ItemStack>): Pair<Int, ItemStack> {
        var empty = 0
        var target: ItemStack = ItemStack.EMPTY
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (stack.`is`(HCItems.BLUEPRINT)) {
                if (stack.get(HCDataComponents.BLUEPRINT_NUMBER) != 0) {
                    if (!target.isEmpty) {
                        break
                    }
                    target = stack
                } else {
                    empty++
                }
            }
        }
        return empty to target
    }

    override fun matches(input: List<ItemStack>, level: Level): Boolean {
        val (empty: Int, target: ItemStack) = getBlueprints(input)
        return !target.isEmpty && empty > 0
    }

    override fun assemble(input: List<ItemStack>, registries: HolderLookup.Provider): ItemStack {
        val (empty: Int, target: ItemStack) = getBlueprints(input)
        return when {
            !target.isEmpty && empty > 0 -> target.copyWithCount(empty + 1)
            else -> ItemStack.EMPTY
        }
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.BLUEPRINT_CLONING
}
