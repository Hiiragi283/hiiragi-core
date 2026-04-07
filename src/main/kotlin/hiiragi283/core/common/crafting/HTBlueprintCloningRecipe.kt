package hiiragi283.core.common.crafting

import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HTBlueprintCloningRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    private fun getBlueprints(input: ImmutableRecipeInput): Pair<Int, ItemStack> {
        var empty = 0
        var target: ItemStack = ItemStack.EMPTY
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (HCItems.BLUEPRINT.isOf(stack)) {
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

    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        val (empty: Int, target: ItemStack) = getBlueprints(input)
        return !target.isEmpty && empty > 0
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val (empty: Int, target: ItemStack) = getBlueprints(input)
        return when {
            !target.isEmpty && empty > 0 -> target.copyWithCount(empty + 1)
            else -> ItemStack.EMPTY
        }
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.BLUEPRINT_CLONING
}
