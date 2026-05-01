package hiiragi283.core.api.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.placement.IPlaceable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.ResourceLocation

/**
 * [HTRecipeDisplay]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.3
 */
abstract class HTDisplayRecipeCategory<T : HTRecipeDisplay>(
    guiHelper: IGuiHelper,
    recipeType: HTRecipeViewerType<T>,
    private val codec: Codec<T>,
) : HTBasicRecipeCategory<T>(guiHelper, recipeType) {
    final override fun isHandled(recipe: T): Boolean = recipe.isHandled()

    final override fun getRegistryName(recipe: T): ResourceLocation = recipe.getId()

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<T> = codec

    abstract class Basic<T : HTRecipeDisplay.Simple>(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<T>, codec: Codec<T>) :
        HTDisplayRecipeCategory<T>(guiHelper, recipeType, codec) {
        final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: T, focuses: IFocusGroup) {
            setRecipe(builder, recipe.contents, focuses)
        }

        protected abstract fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup)
    }

    abstract class Simple(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTRecipeDisplay.Simple>) :
        Basic<HTRecipeDisplay.Simple>(guiHelper, recipeType, HTRecipeDisplay.Simple.CODEC)

    abstract class Progress(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTProgressRecipeDisplay>) :
        Basic<HTProgressRecipeDisplay>(guiHelper, recipeType, HTProgressRecipeDisplay.CODEC) {
        protected fun IRecipeExtrasBuilder.addRecipeArrow(display: HTProgressRecipeDisplay): IPlaceable<*> =
            this.addRecipeArrow(display.progressData)
    }
}
