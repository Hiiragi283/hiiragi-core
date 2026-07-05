package hiiragi283.lib.integration.jei.category

import com.lowdragmc.lowdraglib2.gui.ui.utils.IModularUIProvider
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.Identifier

/**
 * [HTRecipeHolder]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 *
 * 参照 : [Mekanism - HolderRecipeCategory](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/client/recipe_viewer/jei/HolderRecipeCategory.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTHolderRecipeCategory<RECIPE : Any>(
    guiHelper: IGuiHelper,
    recipeType: HTRecipeViewerType<HTRecipeHolder<RECIPE>>,
    private val codec: Codec<HTRecipeHolder<RECIPE>>,
    provider: IModularUIProvider<HTRecipeHolder<RECIPE>>,
) : HTBasicRecipeCategoryN<HTRecipeHolder<RECIPE>>(guiHelper, recipeType, provider) {
    constructor(
        guiHelper: IGuiHelper,
        recipeType: HTHolderRecipeViewerType<RECIPE>,
        codec: MapCodec<RECIPE>,
        provider: IModularUIProvider<RECIPE>,
    ) : this(guiHelper, recipeType, HTRecipeHolder.codec(codec), IModularUIProvider { provider.createModularUI(it.recipe) })

    final override fun getIdentifier(recipe: HTRecipeHolder<RECIPE>): Identifier = recipe.getId()

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTRecipeHolder<RECIPE>> = codec
}
