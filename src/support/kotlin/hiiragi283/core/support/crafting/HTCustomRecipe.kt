package hiiragi283.core.support.crafting

import hiiragi283.core.api.recipe.input.asList
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.level.Level

abstract class HTCustomRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    final override fun matches(input: CraftingInput, level: Level): Boolean = matches(input.asList(), level)

    protected abstract fun matches(input: List<ItemStack>, level: Level): Boolean

    final override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack = assemble(input.asList(), registries)

    protected abstract fun assemble(input: List<ItemStack>, registries: HolderLookup.Provider): ItemStack
}
