package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HCChargingRecipe(val ingredient: Ingredient, val result: HTItemResult, val requiredEnergy: Int) :
    HTSerializableRecipe<HCChargingRecipe.Input> {
    companion object {
        const val DEFAULT_ENERGY = 1_024_000
    }

    override fun test(input: Input): Boolean {
        val (item: ItemStack, energy: Int?) = input
        if (!ingredient.test(item)) return false
        return energy == null || energy >= requiredEnergy
    }

    override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack =
        result.getStackOrEmpty(registries, input.energy == null)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CHARGING.get()

    @JvmRecord
    data class Input(val item: ItemStack, val energy: Int?) : RecipeInput {
        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> item
            else -> error("No item for index $index")
        }

        override fun size(): Int = 1
    }
}
