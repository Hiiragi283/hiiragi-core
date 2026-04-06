package hiiragi283.core.client.jei.category.base

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.ResourceLocation

/**
 * [HTRecipeLookup]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
abstract class HTLookupRecipeCategory<RECIPE : Any>(guiHelper: IGuiHelper, recipeType: HTLookupRecipeViewerType<*, RECIPE>) :
    HTBasicRecipeCategory<HTRecipeHolder<RECIPE>>(guiHelper, recipeType) {
    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTRecipeHolder<RECIPE>, focuses: IFocusGroup) {
        setupRecipe(builder, recipe.recipe, focuses)
    }

    protected abstract fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    final override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTRecipeHolder<RECIPE>, focuses: IFocusGroup) {
        createRecipeExtrasImpl(builder, recipe.recipe, focuses)
    }

    protected open fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {}

    final override fun getRegistryName(recipe: HTRecipeHolder<RECIPE>): ResourceLocation = recipe.id

    private val codec: Codec<HTRecipeHolder<RECIPE>> = ResourceLocation.CODEC.comapFlatMap(
        { id: ResourceLocation ->
            recipeType
                .getHolder(id)
                ?.let { DataResult.success(it) }
                ?: DataResult.error { "Could not find recipe for key: $id" }
        },
        HTRecipeHolder<RECIPE>::id,
    )

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTRecipeHolder<RECIPE>> = codec
}
