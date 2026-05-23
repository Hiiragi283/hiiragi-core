package hiiragi283.core.common.crafting

import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.level.Level

data object HCEternalSmithingRecipe : SmithingRecipe {
    @JvmField
    val ADDITIONAL_TAG: Ingredient = Ingredient.of(CommonTagPrefixes.INGOT.itemTagKey(CommonMaterialKeys.IRIDIUM))

    //    SmithingRecipe    //

    override fun isTemplateIngredient(stack: ItemStack): Boolean = stack.`is`(HCItems.ETERNAL_UPGRADE)

    override fun isBaseIngredient(stack: ItemStack): Boolean = stack.isDamageableItem

    override fun isAdditionIngredient(stack: ItemStack): Boolean = ADDITIONAL_TAG.test(stack)

    override fun matches(input: SmithingRecipeInput, level: Level): Boolean = isTemplateIngredient(input.template) && isBaseIngredient(input.base) && isAdditionIngredient(input.addition)

    override fun assemble(input: SmithingRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val stack: ItemStack = input.base.copy()
        stack.set(DataComponents.UNBREAKABLE, Unbreakable(true))
        return stack
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun isSpecial(): Boolean = true

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.ETERNAL_UPGRADE
}
