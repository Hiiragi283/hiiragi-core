package hiiragi283.core.client.integration.jei.category.base

import com.mojang.serialization.Codec
import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.integration.jei.HTJeiDrawables
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.text.Text
import hiiragi283.core.client.gui.widget.HTGuiWidget
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.placement.IPlaceable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import org.joml.Matrix3x2fStack

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[IRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.jei.BaseRecipeCategory
 */
abstract class HTBasicRecipeCategory<RECIPE : Any>(
    private val guiHelper: IGuiHelper,
    private val recipeType: IRecipeType<RECIPE>,
    private val title: Text,
    private val icon: IDrawable,
    private val bounds: HTBounds,
) : AbstractContainerEventHandler(),
    IRecipeCategory<RECIPE>,
    HTAbstractGui {
    companion object {
        @JvmStatic
        protected fun createIcon(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<*>): IDrawable = recipeType.icon.map(
            { id: Identifier -> guiHelper.drawableBuilder(id, 0, 0, 18, 18).setTextureSize(18, 18).build() },
            { stack: ItemStackTemplate -> guiHelper.createDrawableItemStack(stack.create()) },
        )
    }

    constructor(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<RECIPE>) : this(
        guiHelper,
        HTJeiPlugin.getRecipeType(recipeType),
        recipeType.getText(),
        createIcon(guiHelper, recipeType),
        recipeType.bounds,
    )

    private val widgets: MutableList<HTGuiWidget<*>> = mutableListOf()

    protected fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET {
        this.widgets += HTGuiWidget(this, widget)
        return widget
    }

    override fun children(): List<GuiEventListener> = widgets

    override fun getRectangle(): ScreenRectangle = ScreenRectangle(getGuiLeft(), getGuiTop(), getXSize(), getYSize())

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

    override fun draw(
        recipe: RECIPE,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphicsExtractor,
        mouseX: Double,
        mouseY: Double,
    ) {
        val pose: Matrix3x2fStack = guiGraphics.pose()
        pose.pushMatrix()
        pose.translate(bounds.left.toFloat(), bounds.top.toFloat())
        renderWidgets(recipe, recipeSlotsView, guiGraphics, mouseX.toInt(), mouseY.toInt())
        pose.popMatrix()
    }

    protected fun renderWidgets(
        recipe: RECIPE,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
    ) {
        val pose: Matrix3x2fStack = guiGraphics.pose()
        for (widget: HTGuiWidget<*> in widgets) {
            pose.pushMatrix()
            widget.extractRenderState(guiGraphics, mouseX, mouseY, 0f)
            pose.popMatrix()
        }
    }

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
    fun getPosition(index: Float): Int = (index * 18).toInt()

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Double): Int = (index * 18).toInt()

    // IRecipeSlotBuilder
    protected fun IRecipeSlotBuilder.setSlotBackground(type: HTBackgroundType): IRecipeSlotBuilder =
        this.setBackground(HTJeiDrawables.getSlot(type, guiHelper), -1, -1).setSlotName(type.name)

    protected fun IRecipeSlotBuilder.setTankBackground(type: HTBackgroundType): IRecipeSlotBuilder =
        this.setBackground(HTJeiDrawables.getTank(type, guiHelper), -1, -1).setSlotName(type.name)

    // IRecipeExtrasBuilder
    protected fun IRecipeExtrasBuilder.addRecipePlus(x: Int, y: Int = getPosition(0)): IPlaceable<*> =
        this.addRecipePlusSign().setPosition(x + 2, y + 2)
}
