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
import org.apache.commons.lang3.math.Fraction

class HCExplodingRecipe(val ingredient: Ingredient, val result: HTItemResult, val requiredPower: Fraction) :
    HTSerializableRecipe<HCExplodingRecipe.Input> {
    override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EXPLODING

    override fun getType(): RecipeType<*> = HCRecipeTypes.EXPLODING.get()

    override fun test(input: Input): Boolean = ingredient.test(input.item) && input.power >= requiredPower

    @JvmRecord
    data class Input(val item: ItemStack, val power: Fraction) : RecipeInput {
        override fun getItem(index: Int): ItemStack = item

        override fun size(): Int = 1
    }
}
