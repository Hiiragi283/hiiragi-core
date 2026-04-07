package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCForgingRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTBasicForgingRecipe(
    val base: HTItemIngredient,
    val addition: Optional<HTItemIngredient>,
    val results: List<HTItemResult>,
    override val time: Int,
) : HCForgingRecipe.Serializable {
    override fun testBase(stack: ItemStack): Boolean = base.test(stack)

    override fun testAddition(stack: ItemStack): Boolean = addition.map { it.test(stack) }.orElseGet(stack::isEmpty)

    override fun assembleItems(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): List<ItemStack> =
        results.map { it.getStackOrEmpty(registries) }.filterNot(ItemStack::isEmpty)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.FORGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.FORGING.get()
}
