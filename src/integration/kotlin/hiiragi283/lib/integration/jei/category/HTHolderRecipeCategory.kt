package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.viewer.HTHolderRecipeViewerType
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.Identifier

/**
 * [HTRecipeHolder]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 *
 * 参照 : [Mekanism - HolderRecipeCategory](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/client/recipe_viewer/jei/HolderRecipeCategory.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTHolderRecipeCategory<RECIPE : Any>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderRecipeViewerType<RECIPE>,
    private val codec: Codec<HTRecipeHolder<RECIPE>>,
) : HTBasicRecipeCategory<HTRecipeHolder<RECIPE>>(guiHelper, recipeType) {
    constructor(
        guiHelper: IGuiHelper,
        recipeType: HTHolderRecipeViewerType<RECIPE>,
        codec: MapCodec<RECIPE>,
    ) : this(guiHelper, recipeType, HTRecipeHolder.codec(codec))

    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTRecipeHolder<RECIPE>, focuses: IFocusGroup) {
        setupRecipe(builder, recipe.recipe, focuses)
    }

    protected abstract fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    final override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTRecipeHolder<RECIPE>, focuses: IFocusGroup) {
        setupRecipeExtras(builder, recipe.recipe, focuses)
    }

    protected open fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {}

    final override fun getIdentifier(recipe: HTRecipeHolder<RECIPE>): Identifier = recipe.getId()

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTRecipeHolder<RECIPE>> = codec
}
