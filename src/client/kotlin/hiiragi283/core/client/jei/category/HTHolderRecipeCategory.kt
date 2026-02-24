package hiiragi283.core.client.jei.category

import com.mojang.serialization.Codec
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * バニラの[Recipe]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.jei.HolderRecipeCategory
 */
abstract class HTHolderRecipeCategory<RECIPE : Recipe<*>>(guiHelper: IGuiHelper, recipeType: HTHolderRecipeViewerType<*, RECIPE>) :
    HTBasicRecipeCategory<RecipeHolder<RECIPE>>(guiHelper, recipeType) {
    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<RECIPE>, focuses: IFocusGroup) {
        setupRecipe(builder, recipe.value, focuses)
    }

    protected abstract fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    final override fun getRegistryName(recipe: RecipeHolder<RECIPE>): ResourceLocation = recipe.id

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RecipeHolder<RECIPE>> =
        codecHelper.getRecipeHolderCodec()
}
