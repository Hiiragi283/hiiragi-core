package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import java.util.Random
import java.util.function.Function

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
    private val bounds: HTBounds,
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

    final override fun getInputs(): List<EmiIngredient> = inputs

    final override fun getCatalysts(): List<EmiIngredient> = catalysts

    final override fun getOutputs(): List<EmiStack> = outputs

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

    /**
     * このレシピに材料スロットを追加します。
     * @param index 材料のインデックス
     * @param x x軸方向の座標
     * @param y y軸方向の座標
     */
    fun WidgetHolder.addInput(index: Int, x: Int, y: Int): SlotWidget {
        val input: EmiIngredient = input(index)
        return addSlot(input, x, y).drawBack(false)
    }

    /**
     * このレシピに触媒スロットを追加します。
     * @param index 触媒のインデックス
     * @param x x軸方向の座標
     * @param y y軸方向の座標
     */
    fun WidgetHolder.addCatalyst(index: Int, x: Int, y: Int): SlotWidget {
        val catalyst: EmiIngredient = catalyst(index)
        return addSlot(catalyst, x, y).catalyst(!catalyst.isEmpty).drawBack(false)
    }

    /**
     * このレシピに完成品スロットを追加します。
     * @param index 触媒のインデックス
     * @param x x軸方向の座標
     * @param y y軸方向の座標
     * @param large スロットを大型で表示するかどうか
     * @param drawBack スロットの背景を描画するかどうか
     */
    fun WidgetHolder.addOutput(
        index: Int,
        x: Int,
        y: Int,
        large: Boolean = false,
        drawBack: Boolean = false,
    ): SlotWidget = when {
        large -> addSlot(output(index), x - 4, y - 4).large(true)
        else -> addSlot(output(index), x, y)
    }.recipeContext(this@HTEmiRecipe).drawBack(drawBack)

    /**
     * このレシピに動的な完成品スロットを追加します。
     * @param factory 乱数からプレビューを生成するブロック
     * @param unique 多分識別用のユニークなIDな気がする
     * @param x x軸方向の座標
     * @param y y軸方向の座標
     * @param large スロットを大型で表示するかどうか
     * @param drawBack スロットの背景を描画するかどうか
     */
    fun WidgetHolder.addGeneratedOutput(
        factory: Function<Random, EmiIngredient>,
        unique: Int,
        x: Int,
        y: Int,
        large: Boolean = false,
        drawBack: Boolean = true,
    ): SlotWidget = when {
        large -> addGeneratedSlot(factory, unique, x - 4, y - 4).large(true)
        else -> addGeneratedSlot(factory, unique, x, y)
    }.recipeContext(this@HTEmiRecipe).drawBack(drawBack)
}
