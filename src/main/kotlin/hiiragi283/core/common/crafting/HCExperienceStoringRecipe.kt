package hiiragi283.core.common.crafting

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.util.ExpValue
import hiiragi283.core.util.HTExperienceHelper
import hiiragi283.core.util.storedExperience
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class HCExperienceStoringRecipe(category: CraftingBookCategory) : HTCustomRecipe(category) {
    override fun matches(input: ImmutableRecipeInput, level: Level): Boolean {
        var tomeCount = 0
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (stack.`is`(HCItems.EXPERIENCE_TOME)) {
                tomeCount++
            }
        }
        return tomeCount == 1
    }

    override fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val player: Player = HiiragiCoreAPI.getCraftingPlayer() ?: return ItemStack.EMPTY
        val currentExp: ExpValue = player.storedExperience
        if (currentExp <= 0) return ItemStack.EMPTY

        var tomeStack: ItemStack = ItemStack.EMPTY
        for (stack: ItemStack in input) {
            if (stack.isEmpty) continue
            if (stack.`is`(HCItems.EXPERIENCE_TOME)) {
                tomeStack = stack.copy()
                break
            }
        }
        if (tomeStack.isEmpty) return ItemStack.EMPTY

        val needed: ExpValue = minOf(Long.MAX_VALUE - HTExperienceHelper.getStoredExp(tomeStack), currentExp)
        if (needed <= 0) return ItemStack.EMPTY
        HTExperienceHelper.updateStoredExp(tomeStack) { it + needed }
        player.storedExperience -= needed
        return tomeStack
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = width * height >= 1

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EXPERIENCE_STORING
}
