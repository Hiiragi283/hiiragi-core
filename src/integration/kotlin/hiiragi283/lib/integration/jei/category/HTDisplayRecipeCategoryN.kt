package hiiragi283.lib.integration.jei.category

import com.lowdragmc.lowdraglib2.gui.ui.utils.IModularUIProvider
import com.mojang.serialization.Codec
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.Identifier

open class HTDisplayRecipeCategoryN<T : HTRecipeDisplay>(
    guiHelper: IGuiHelper,
    recipeType: HTRecipeViewerType<T>,
    private val codec: Codec<T>,
    provider: IModularUIProvider<T>,
) : HTBasicRecipeCategoryN<T>(guiHelper, recipeType, provider) {
    final override fun isHandled(recipe: T): Boolean = recipe.isHandled()

    final override fun getIdentifier(recipe: T): Identifier = recipe.getId()

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<T> = codec

    /**
     * [HTDisplayRecipeCategoryN]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Simple(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTRecipeDisplay.Simple>, provider: IModularUIProvider<HTRecipeDisplay.Simple>) : HTDisplayRecipeCategoryN<HTRecipeDisplay.Simple>(guiHelper, recipeType, HTRecipeDisplay.Simple.CODEC, provider)

    /**
     * [HTProgressRecipeDisplay]向けの[HTDisplayRecipeCategoryN]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Progress(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTProgressRecipeDisplay>, provider: IModularUIProvider<HTProgressRecipeDisplay>) : HTDisplayRecipeCategoryN<HTProgressRecipeDisplay>(guiHelper, recipeType, HTProgressRecipeDisplay.CODEC, provider)
}
