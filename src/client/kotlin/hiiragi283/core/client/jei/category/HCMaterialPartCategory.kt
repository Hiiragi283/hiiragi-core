package hiiragi283.core.client.jei.category

import com.mojang.serialization.Codec
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.base.HTBasicRecipeCategory
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawablesView
import mezz.jei.api.gui.placement.HorizontalAlignment
import mezz.jei.api.gui.placement.VerticalAlignment
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.gui.widgets.IScrollGridWidget
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HCMaterialPartCategory(guiHelper: IGuiHelper) :
    HTBasicRecipeCategory<HTMaterialManager.Entry>(guiHelper, HCJeiRecipeTypes.MaterialType) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTMaterialManager.Entry, focuses: IFocusGroup) {
        val ingredients: Sequence<Ingredient> = HTTagPrefix.instances
            .values
            .asSequence()
            .map { it.itemTagKey(recipe) }
            .distinct()
            .map(Ingredient::of)

        builder
            .addInputSlot()
            .addIngredients(recipe.getDefaultPart(recipe)?.let(Ingredient::of) ?: Ingredient.EMPTY)
            .setStandardSlotBackground()

        for (ingredient: Ingredient in ingredients) {
            builder.addOutputSlot().addIngredients(ingredient)
        }
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTMaterialManager.Entry, focuses: IFocusGroup) {
        // Material Name?
        val recipeSlots: IRecipeSlotDrawablesView = builder.recipeSlots
        val outputSlots: List<IRecipeSlotDrawable> = recipeSlots.getSlots(RecipeIngredientRole.OUTPUT)

        val widget: IScrollGridWidget = builder
            .addScrollGridWidget(outputSlots, 7, 5)
            .setPosition(0, 0, width, height, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM)
        recipeSlots
            .getSlots(RecipeIngredientRole.INPUT)
            .first()
            .setPosition(widget.screenRectangle.position.x + 1, 1)
    }

    override fun getRegistryName(recipe: HTMaterialManager.Entry): ResourceLocation = recipe.getId()

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTMaterialManager.Entry> =
        codecHelper.getSlowRecipeCategoryCodec(this, recipeManager) // TODO
}
