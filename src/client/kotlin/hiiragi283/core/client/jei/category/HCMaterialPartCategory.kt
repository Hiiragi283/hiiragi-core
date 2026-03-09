package hiiragi283.core.client.jei.category

import com.mojang.serialization.Codec
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.material.property.getDefaultPart
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
        val ingredients: Sequence<Ingredient> = HiiragiCoreAccess.INSTANCE
            .partManager
            .values
            .asSequence()
            .mapNotNull(HTPartLike::tagPrefix)
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
        builder
            .addText(recipe.asMaterialKey().getText(), width - 22, 20)
            .setPosition(22, 0)
            .setColor(0x505050)
            .setLineSpacing(0)
            .setTextAlignment(VerticalAlignment.CENTER)
            .setTextAlignment(HorizontalAlignment.CENTER)

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

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTMaterialManager.Entry> = CODEC

    companion object {
        @JvmStatic
        private val CODEC: Codec<HTMaterialManager.Entry> = HTMaterialKey.CODEC
            .flatXmap(
                { key: HTMaterialKey ->
                    HiiragiCoreAccess.INSTANCE
                        .materialManager
                        .entries
                        .firstOrNull(key::isOf)
                        ?: error("Unknown material; ${key.getId()}")
                },
                HTMaterialManager.Entry::asMaterialKey,
            ).codec
    }
}
