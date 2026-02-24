package hiiragi283.core.client.jei.category.base

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTFakeRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.resource.IdToValue
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * [HTRecipeLookup]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see Managed
 * @see Fake
 */
sealed class HTLookupRecipeCategory<RECIPE : Any, HOLDER : Any>(
    guiHelper: IGuiHelper,
    recipeType: HTRecipeViewerType<HOLDER>,
    protected val lookup: HTRecipeLookup<*, RECIPE, HOLDER>,
) : HTBasicRecipeCategory<HOLDER>(guiHelper, recipeType) {
    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HOLDER, focuses: IFocusGroup) {
        setupRecipe(builder, lookup.getRecipe(recipe), focuses)
    }

    protected abstract fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    final override fun getRegistryName(recipe: HOLDER): ResourceLocation = lookup.getId(recipe)

    /**
     * [HTHolderRecipeViewerType]に基づいた[HTLookupRecipeCategory]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    abstract class Managed<RECIPE : Recipe<*>>(guiHelper: IGuiHelper, recipeType: HTHolderRecipeViewerType<*, RECIPE>) :
        HTLookupRecipeCategory<RECIPE, RecipeHolder<RECIPE>>(guiHelper, recipeType, recipeType) {
        final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RecipeHolder<RECIPE>> =
            codecHelper.getRecipeHolderCodec()
    }

    /**
     * [HTFakeRecipeViewerType]に基づいた[HTLookupRecipeCategory]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    abstract class Fake<RECIPE : Any>(guiHelper: IGuiHelper, recipeType: HTFakeRecipeViewerType<*, RECIPE>) :
        HTLookupRecipeCategory<RECIPE, IdToValue<RECIPE>>(guiHelper, recipeType, recipeType) {
        private val codec: Codec<IdToValue<RECIPE>> = ResourceLocation.CODEC.comapFlatMap(
            { id: ResourceLocation ->
                recipeType
                    .getAllRecipes()
                    .firstOrNull { it.first == id }
                    ?.let { DataResult.success(it) }
                    ?: DataResult.error { "Could not find recipe for key: $id" }
            },
            IdToValue<RECIPE>::first,
        )

        final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<IdToValue<RECIPE>> = codec
    }
}
