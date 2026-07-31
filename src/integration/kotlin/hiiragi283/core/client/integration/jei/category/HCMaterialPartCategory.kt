package hiiragi283.core.client.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.core.api.integration.jei.category.HTBasicRecipeCategory
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.property.itemTagKey
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
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
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class HCMaterialPartCategory(guiHelper: IGuiHelper) : HTBasicRecipeCategory<HTMaterial>(guiHelper, HCRecipeViewerTypes.MaterialType) {
    private fun getIngredients(entry: HTMaterial): Sequence<List<ItemStack>> = HTPart
        .getManager()
        .asSequence()
        .mapNotNull { it.itemTagKey(entry.key) }
        .distinct()
        .map { tagKey: TagKey<Item> -> BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).map(::ItemStack) }
        .filterNot(Iterable<ItemStack>::none)

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTMaterial, focuses: IFocusGroup) {
        builder
            .addInputSlot()
            .addIngredients(recipe.getDefaultPart(recipe.key)?.let(Ingredient::of) ?: Ingredient.EMPTY)
            .setStandardSlotBackground()

        for (ingredient: Iterable<ItemStack> in getIngredients(recipe)) {
            builder.addOutputSlot().addItemStacks(ingredient.toList())
        }
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTMaterial, focuses: IFocusGroup) {
        builder
            .addText(recipe.key.getText(), width - 22, 20)
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

    override fun isHandled(recipe: HTMaterial): Boolean = getIngredients(recipe).any() || recipe.getDefaultPart(recipe.key) != null

    override fun getRegistryName(recipe: HTMaterial): ResourceLocation = recipe.getId()

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTMaterial> = HTMaterial.CODEC
}
