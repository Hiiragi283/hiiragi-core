package hiiragi283.core.common.recipe.custom

import hiiragi283.core.common.recipe.ingredient.HTDamageableIngredient
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.lib.tag.HTCommonTags
import java.util.Optional
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.Unit
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.PlacementInfo
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput

data object HCEternalSmithingRecipe : SmithingRecipe {
    override fun getSerializer(): RecipeSerializer<HCEternalSmithingRecipe> = HCRecipeSerializers.ETERNAL_UPGRADE

    override fun templateIngredient(): Optional<Ingredient> = Optional.of(Ingredient.of(HCItems.ETERNAL_UPGRADE))

    override fun baseIngredient(): Ingredient = HTDamageableIngredient.toVanilla()

    override fun additionIngredient(): Optional<Ingredient> = BuiltInRegistries.ITEM.get(HTCommonTags.Items.INGOTS_IRIDIUM).map(Ingredient::of)

    override fun assemble(input: SmithingRecipeInput): ItemStack {
        val stack: ItemStack = input.base.copy()
        stack.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        return stack
    }

    override fun showNotification(): Boolean = true

    override fun group(): String = ""

    override fun placementInfo(): PlacementInfo = PlacementInfo.createFromOptionals(listOf(templateIngredient(), Optional.of(baseIngredient()), additionIngredient()))
}
