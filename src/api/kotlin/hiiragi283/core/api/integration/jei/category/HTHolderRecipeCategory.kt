package hiiragi283.core.api.integration.jei.category

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.id
import hiiragi283.core.api.recipe.recipe
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.serialization.codec.HTCodecs
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.ResourceLocation

/**
 * [HTRecipeHolder]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
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
    ) : this(guiHelper, recipeType, HTCodecs.mapPair(ResourceLocation.CODEC.fieldOf(HTConst.ID), codec).codec())

    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTRecipeHolder<RECIPE>, focuses: IFocusGroup) {
        setupRecipe(builder, recipe.recipe, focuses)
    }

    protected abstract fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    final override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTRecipeHolder<RECIPE>, focuses: IFocusGroup) {
        setupRecipeExtras(builder, recipe.recipe, focuses)
    }

    protected open fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {}

    final override fun getRegistryName(recipe: HTRecipeHolder<RECIPE>): ResourceLocation = recipe.id

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTRecipeHolder<RECIPE>> = codec
}
