package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.TextureWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.text.HTCommonTranslation
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[EmiRecipe]の抽象クラスです。
 * @param RECIPE 元となるレシピのクラス
 * @param category レシピの[カテゴリ][EmiRecipeCategory]
 * @param id このレシピの[ID][ResourceLocation]
 * @param recipe [RECIPE]のインスタンス
 * @param bounds このレシピが表示される範囲
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.client.recipe_viewer.emi.recipe.MekanismEmiRecipe
 * @see HTEmiHolderRecipe
 */
abstract class HTEmiRecipe<RECIPE : Any>(
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    protected val recipe: RECIPE,
    private val bounds: Bounds,
) : AbstractContainerEventHandler(),
    EmiRecipe {
    constructor(
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : this(category, id, recipe, category.bounds)

    private val inputs: MutableList<EmiIngredient> = mutableListOf()
    private val catalysts: MutableList<EmiIngredient> = mutableListOf()
    private val outputs: MutableList<EmiStack> = mutableListOf()
    private val renderOutputs: MutableList<EmiIngredient> = mutableListOf()

    /**
     * 指定した[インデックス][index]に対応する[材料][EmiIngredient]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[EmiStack.EMPTY]
     */
    protected fun input(index: Int): EmiIngredient = inputs.getOrNull(index) ?: EmiStack.EMPTY

    /**
     * 指定した[インデックス][index]に対応する[触媒][EmiIngredient]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[EmiStack.EMPTY]
     */
    protected fun catalyst(index: Int): EmiIngredient = catalysts.getOrNull(index) ?: EmiStack.EMPTY

    /**
     * 指定した[インデックス][index]に対応する[完成品のプレビュー][EmiIngredient]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[EmiStack.EMPTY]
     */
    protected fun output(index: Int): EmiIngredient = renderOutputs.getOrNull(index) ?: EmiStack.EMPTY

    /**
     * アイテムの材料を追加します。
     */
    protected fun addInput(ingredient: HTItemIngredient?) {
        addInput(ingredient?.let(HTItemIngredient::toEmi))
    }

    /**
     * 液体の材料を追加します。
     */
    protected fun addInput(ingredient: HTFluidIngredient?) {
        addInput(ingredient?.let(HTFluidIngredient::toEmi))
    }

    /**
     * 材料を追加します。
     */
    protected fun addInput(ingredient: EmiIngredient?) {
        inputs.add(ingredient ?: EmiStack.EMPTY)
    }

    /**
     * 空の材料を追加します。
     */
    protected fun addEmptyInput() {
        inputs.add(EmiStack.EMPTY)
    }

    /**
     * アイテムの触媒を追加します。
     */
    protected fun addCatalyst(ingredient: HTItemIngredient?) {
        addCatalyst(ingredient?.let(HTItemIngredient::toEmi))
    }

    /**
     * 触媒を追加します。
     */
    protected fun addCatalyst(ingredient: EmiIngredient?) {
        catalysts.add(ingredient ?: EmiStack.EMPTY)
    }

    /**
     * アイテムの完成品を追加します。
     */
    protected fun addOutputs(result: HTItemResult?) {
        addOutputs(result?.let(::result))
    }

    /**
     * 液体の完成品を追加します。
     */
    protected fun addOutputs(result: HTFluidResult?) {
        addOutputs(result?.let(::result))
    }

    /**
     * アイテムと液体の完成品を追加します。
     */
    protected fun addOutputs(results: Ior<HTItemResult, HTFluidResult>) {
        addOutputs(results.getLeft())
        addOutputs(results.getRight())
    }

    /**
     * 完成品を追加します。
     */
    protected fun addOutputs(stacks: EmiStack?) {
        addOutputs(listOfNotNull(stacks))
    }

    /**
     * 完成品を追加します。
     */
    protected fun addOutputs(stacks: List<EmiStack>) {
        if (stacks.isEmpty()) {
            outputs.add(EmiStack.EMPTY)
            renderOutputs.add(EmiStack.EMPTY)
        } else {
            outputs.addAll(stacks)
            renderOutputs.add(EmiIngredient.of(stacks))
        }
    }

    protected fun result(result: HTItemResult): EmiStack = result.toEmi()

    protected fun result(result: HTFluidResult): EmiStack = result.toEmi()

    //    EmiRecipe    //

    final override fun getCategory(): EmiRecipeCategory = category

    final override fun getId(): ResourceLocation = id

    final override fun getInputs(): List<EmiIngredient> = inputs.filterNot(EmiIngredient::isEmpty)

    final override fun getCatalysts(): List<EmiIngredient> = catalysts.filterNot(EmiIngredient::isEmpty)

    final override fun getOutputs(): List<EmiStack> = outputs.filterNot(EmiStack::isEmpty)

    final override fun getDisplayWidth(): Int = bounds.width

    final override fun getDisplayHeight(): Int = bounds.height

    override fun getBackingRecipe(): RecipeHolder<*>? = null

    //    AbstractContainerEventHandler    //

    final override fun children(): List<GuiEventListener> = listOf()

    //    Extensions    //

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Int): Int = index * 18

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Double): Int = (index * 18).toInt()

    fun WidgetHolder.addArrow(x: Int = getPosition(3.5), y: Int = getPosition(1)): TextureWidget = addFillingArrow(x, y, 2000)

    fun WidgetHolder.addArrow(time: Int, x: Int = getPosition(3.5), y: Int = getPosition(1)): TextureWidget =
        addFillingArrow(x, y, 50 * time).tooltipText(listOf(HTCommonTranslation.SECONDS.translate(time / 20.0f, time)))

    fun WidgetHolder.addBurning(x: Int, y: Int, time: Int) {
        addAnimatedTexture(
            EmiTexture.FULL_FLAME,
            x + 2,
            y + 2,
            1000 * time / 20,
            false,
            true,
            true,
        )
    }

    fun WidgetHolder.addPlus(x: Int, y: Int): TextureWidget = addTexture(EmiTexture.PLUS, x + 3, y + 3)

    fun WidgetHolder.setShapeless(x: Int, y: Int): TextureWidget = addTexture(EmiTexture.SHAPELESS, x + 1, y)

    /**
     * このレシピに材料スロットを追加します。
     * @param ingredient 材料のインスタンス
     * @param x x軸方向の座標
     * @param y y軸方向の座標
     * @param type スロットの種類
     * @since 0.8.0
     */
    fun WidgetHolder.addSlot(
        ingredient: EmiIngredient,
        x: Int,
        y: Int,
        type: HTBackgroundType,
    ): SlotWidget {
        addTexture(HTEmiTextures.SLOT_TEXTURES[type]!!, x, y)
        val slot: SlotWidget = addSlot(ingredient, x, y).drawBack(false)
        if (type == HTBackgroundType.NONE) {
            slot.catalyst(true)
        }
        if (type.isOutput) {
            slot.recipeContext(this@HTEmiRecipe)
        }
        return slot
    }

    /**
     * このレシピに液体タンクを追加します。
     * @param ingredient 表示する材料
     * @param x x軸方向の座標
     * @param capacity このタンクの容量
     * @param y y軸方向の座標
     * @since 0.5.0
     */
    fun WidgetHolder.addTank(
        ingredient: EmiIngredient,
        x: Int,
        type: HTBackgroundType,
        y: Int = getPosition(0),
        capacity: Int = validateCapacity(ingredient.amount),
    ): SlotWidget {
        addTexture(HTEmiTextures.TANK_TEXTURES[type]!!, x, y)
        val slot: SlotWidget = addTank(ingredient, x, y, 18, 18 * 3, capacity).drawBack(false)
        if (type == HTBackgroundType.NONE) {
            slot.catalyst(true)
        }
        if (type.isOutput) {
            slot.recipeContext(this@HTEmiRecipe)
        }
        return slot
    }

    private fun validateCapacity(value: Long): Int {
        val value1: Int = value.toInt()
        return when {
            value1 <= 0 -> HTConst.DEFAULT_FLUID_AMOUNT
            else -> value1
        }
    }
}
