package hiiragi283.core.client.jei.category

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.viewer.HTFakeRecipeViewerType
import hiiragi283.core.api.resource.IdToValue
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.ResourceLocation

/**
 * [HTBasicRecipeCategory]の拡張クラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see mekanism.client.recipe_viewer.jei.HolderRecipeCategory
 */
abstract class HTFakeRecipeCategory<RECIPE : Any>(guiHelper: IGuiHelper, recipeType: HTFakeRecipeViewerType<*, RECIPE>) :
    HTBasicRecipeCategory<IdToValue<RECIPE>>(guiHelper, recipeType) {
    private val codec: Codec<IdToValue<RECIPE>> = createCodec(recipeType)

    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: IdToValue<RECIPE>, focuses: IFocusGroup) {
        setupRecipe(builder, recipe.second, focuses)
    }

    protected abstract fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    final override fun getRegistryName(recipe: IdToValue<RECIPE>): ResourceLocation = recipe.first

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<IdToValue<RECIPE>> = codec

    companion object {
        @JvmStatic
        private fun <RECIPE : Any> createCodec(recipeType: HTRecipeType.Fake<*, RECIPE>): Codec<IdToValue<RECIPE>> =
            ResourceLocation.CODEC.comapFlatMap(
                { id: ResourceLocation ->
                    recipeType
                        .getAllRecipes()
                        .firstOrNull { it.first == id }
                        ?.let { DataResult.success(it) }
                        ?: DataResult.error { "Could not find recipe for key: $id" }
                },
                IdToValue<RECIPE>::first,
            )
    }
}
