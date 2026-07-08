package hiiragi283.lib.integration.jei.category

import com.lowdragmc.lowdraglib2.gui.ui.utils.IModularUIProvider
import com.lowdragmc.lowdraglib2.integration.xei.jei.ModularUIRecipeCategory
import com.mojang.serialization.Codec
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.math.HTBounds
import hiiragi283.lib.math.fraction
import hiiragi283.lib.math.times
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.text.Text
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Seriesで使用される[ModularUIRecipeCategory]の拡張クラスです。
 *
 * 参照 : [Mekanism - BaseRecipeCategory](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/client/recipe_viewer/jei/BaseRecipeCategory.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBasicRecipeCategory<RECIPE : Any>(
    private val guiHelper: IGuiHelper,
    private val recipeType: IRecipeType<RECIPE>,
    private val title: Text,
    private val icon: IDrawable,
    private val bounds: HTBounds,
    provider: IModularUIProvider<RECIPE>,
) : ModularUIRecipeCategory<RECIPE>(provider) {
    companion object {
        @JvmStatic
        protected fun createIcon(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<*>): IDrawable = recipeType.icon.fold(
            { id: Identifier -> guiHelper.drawableBuilder(id, 0, 0, 18, 18).setTextureSize(18, 18).build() },
            { stack: ItemStack -> guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack) },
        )
    }

    constructor(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<RECIPE>, provider: IModularUIProvider<RECIPE>) : this(
        guiHelper,
        HTJeiPlugin.getRecipeType(recipeType),
        recipeType.getText(),
        createIcon(guiHelper, recipeType),
        recipeType.bounds,
        provider,
    )

    //    IRecipeCategory    //

    final override fun getRecipeType(): IRecipeType<RECIPE> = recipeType

    final override fun getTitle(): Text = title

    override fun getWidth(): Int = bounds.width

    override fun getHeight(): Int = bounds.height

    final override fun getIcon(): IDrawable = icon

    abstract override fun getIdentifier(recipe: RECIPE): Identifier?

    abstract override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RECIPE>

    //    Extensions    //

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Int): Int = index * 18

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Float): Int = getPosition(fraction(index))

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Double): Int = getPosition(fraction(index))

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Fraction): Int = (index * 18).toInt()
}
