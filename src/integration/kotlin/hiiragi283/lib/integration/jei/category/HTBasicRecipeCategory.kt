package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.lib.gui.HTAbstractGui
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTJeiDrawables
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.math.HTBounds
import hiiragi283.lib.math.fraction
import hiiragi283.lib.math.times
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.text.Text
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.placement.IPlaceable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[IRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
abstract class HTBasicRecipeCategory<RECIPE : Any>(
    private val guiHelper: IGuiHelper,
    private val recipeType: IRecipeType<RECIPE>,
    private val title: Text,
    private val icon: IDrawable,
    private val bounds: HTBounds,
) : IRecipeCategory<RECIPE>,
    HTAbstractGui {
    companion object {
        @JvmStatic
        protected fun createIcon(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<*>): IDrawable = recipeType.icon.fold(
            { id: Identifier -> guiHelper.drawableBuilder(id, 0, 0, 18, 18).setTextureSize(18, 18).build() },
            { stack: ItemStack -> guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack) },
        )
    }

    constructor(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<RECIPE>) : this(
        guiHelper,
        HTJeiPlugin.getRecipeType(recipeType),
        recipeType.getText(),
        createIcon(guiHelper, recipeType),
        recipeType.bounds,
    )

    //    HTAbstractGui    //

    override fun getGuiLeft(): Int = bounds.left

    override fun getGuiTop(): Int = bounds.top

    override fun getXSize(): Int = bounds.width

    override fun getYSize(): Int = bounds.height

    //    IRecipeCategory    //

    final override fun getRecipeType(): IRecipeType<RECIPE> = recipeType

    final override fun getTitle(): Text = title

    override fun getWidth(): Int = getXSize()

    override fun getHeight(): Int = getYSize()

    final override fun getIcon(): IDrawable = icon

    abstract override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

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

    // IRecipeSlotBuilder
    protected fun IRecipeSlotBuilder.setSlotBackground(type: HTBackgroundType): IRecipeSlotBuilder = this.setBackground(HTJeiDrawables.getSlot(type, guiHelper), -1, -1).setSlotName(type.name)

    protected fun IRecipeSlotBuilder.setSlotBackground(type: HTBackgroundType, capacity: Int): IRecipeSlotBuilder = this
        .setBackground(HTJeiDrawables.getSlot(type, guiHelper), -1, -1)
        .setSlotName(type.name)
        .setFluidRenderer(fixCapacity(capacity), false, 16, 16)

    protected fun IRecipeSlotBuilder.setTankBackground(type: HTBackgroundType, capacity: Int): IRecipeSlotBuilder = this
        .setBackground(HTJeiDrawables.getTank(type, guiHelper), -1, -1)
        .setSlotName(type.name)
        .setFluidRenderer(fixCapacity(capacity), false, 16, 18 * 3 - 2)

    private fun fixCapacity(capacity: Int): Long = maxOf(capacity, 1).toLong()

    // IRecipeExtrasBuilder
    protected fun IRecipeExtrasBuilder.addRecipePlus(x: Int, y: Int = getPosition(0)): IPlaceable<*> = this.addRecipePlusSign().setPosition(x + 2, y + 2)

    /**
     * @since 0.16.0
     */
    protected fun IRecipeExtrasBuilder.addRecipeArrow(progressData: HTProgressData): IPlaceable<*> = when (progressData) {
        is HTProgressData.Energy -> this.addRecipeArrow()
        is HTProgressData.Time -> this.addAnimatedRecipeArrow(progressData.value)
    }
}
