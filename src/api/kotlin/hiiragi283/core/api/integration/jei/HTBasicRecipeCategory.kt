package hiiragi283.core.api.integration.jei

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.serialization.Codec
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.fraction
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.times
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[IRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.jei.BaseRecipeCategory
 */
abstract class HTBasicRecipeCategory<RECIPE : Any>(
    private val guiHelper: IGuiHelper,
    private val recipeType: RecipeType<RECIPE>,
    private val title: Component,
    private val icon: IDrawable,
    private val bounds: HTBounds,
) : IRecipeCategory<RECIPE> {
    companion object {
        @JvmStatic
        protected fun createIcon(guiHelper: IGuiHelper, recipeType: HTJeiRecipeType<*>): IDrawable = recipeType.icon.map(
            { id: ResourceLocation -> guiHelper.drawableBuilder(id, 0, 0, 18, 18).setTextureSize(18, 18).build() },
            { stack: ItemStack -> guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack) },
        )
    }

    constructor(guiHelper: IGuiHelper, recipeType: HTJeiRecipeType<RECIPE>) : this(
        guiHelper,
        HTJeiPlugin.getRecipeType(recipeType),
        recipeType.getText(),
        createIcon(guiHelper, recipeType),
        recipeType.bounds,
    )

    //    IRecipeCategory    //

    final override fun getRecipeType(): RecipeType<RECIPE> = recipeType

    final override fun getTitle(): Component = title

    override fun getWidth(): Int = bounds.width

    override fun getHeight(): Int = bounds.height

    final override fun getIcon(): IDrawable = icon

    abstract override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    abstract override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup)

    override fun draw(
        recipe: RECIPE,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphics,
        mouseX: Double,
        mouseY: Double,
    ) {
        val pose = guiGraphics.pose()
        pose.pushPose()
        pose.translate(bounds.left.toDouble(), bounds.top.toDouble(), 0.0)
        renderWidgets(recipe, recipeSlotsView, guiGraphics, mouseX.toInt(), mouseY.toInt())
        pose.popPose()
    }

    protected fun renderWidgets(
        recipe: RECIPE,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
    ) {
        val pose: PoseStack = guiGraphics.pose()
        for (widget: HTWidget in widgets) {
            pose.pushPose()
            HiiragiCoreAccess.Client.INSTANCE
                .createRenderer(widget)
                ?.render(
                    widget.bounds.copy(x = bounds.x, y = bounds.y),
                    guiGraphics,
                    mouseX,
                    mouseY,
                    0f,
                )
            pose.popPose()
        }
    }

    abstract override fun getRegistryName(recipe: RECIPE): ResourceLocation?

    abstract override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RECIPE>?

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

    private val widgets: MutableList<HTWidget> = mutableListOf()

    protected fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET {
        this.widgets += widget
        return widget
    }

    // Item
    private fun createError(message: Component): ItemStack = createItemStack(
        Items.BARRIER,
        DataComponents.CUSTOM_NAME,
        message,
    )

    protected fun IRecipeLayoutBuilder.addItemSlot(x: Int, y: Int, ingredient: HTItemIngredient?): IRecipeSlotBuilder = when (ingredient) {
        null -> this.addItemSlot(RecipeIngredientRole.RENDER_ONLY, x, y, listOf())
        else -> {
            val (role: RecipeIngredientRole, amount: Int) = when {
                ingredient.isCatalyst -> RecipeIngredientRole.CATALYST to 1
                else -> RecipeIngredientRole.INPUT to ingredient.amount
            }

            val stacks: List<ItemStack> = ingredient.unwrap().map(
                { tagKey: TagKey<Item> ->
                    BuiltInRegistries.ITEM
                        .getTagOrEmpty(tagKey)
                        .map { ItemStack(it, amount) }
                        .takeUnless(List<ItemStack>::isEmpty)
                        ?: listOf(createError(HTCommonTranslation.EMPTY_TAG_KEY.translate(tagKey.location)))
                },
                { resources: List<HTItemResourceType> ->
                    resources
                        .map { it.toStack(amount) }
                        .takeUnless(List<ItemStack>::isEmpty)
                        ?: listOf(createError(HTCommonTranslation.EMPTY.translate()))
                },
            )
            this.addItemSlot(role, x, y, stacks)
        }
    }

    protected fun IRecipeLayoutBuilder.addItemSlot(x: Int, y: Int, result: HTItemResult?): IRecipeSlotBuilder = when (result) {
        null -> this.addItemSlot(RecipeIngredientRole.RENDER_ONLY, x, y, listOf())
        else -> this.addItemSlot(
            RecipeIngredientRole.OUTPUT,
            x,
            y,
            listOf(result.getStackResult(null).mapOrElse(identity(), ::createError)),
        )
    }

    protected fun IRecipeSlotBuilder.setSlotBackground(type: HTBackgroundType): IRecipeSlotBuilder =
        this.setBackground(HTJeiDrawables.getSlot(type, guiHelper), 0, 0)

    protected fun IRecipeLayoutBuilder.addItemSlot(
        role: RecipeIngredientRole,
        x: Int,
        y: Int,
        stacks: List<ItemStack>,
    ): IRecipeSlotBuilder = this.addSlot(role, x + 1, y + 1).addItemStacks(stacks)
}
