package hiiragi283.core.api.integration.emi

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import com.lowdragmc.lowdraglib2.integration.xei.emi.ModularUIEMIRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.gui.element.HTFluidSlotElement
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.integration.emi.slot.HTListFluidTank
import hiiragi283.core.api.integration.emi.slot.HTListItemSlot
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[EmiRecipe]の抽象クラスです。
 * @param RECIPE 元となるレシピのクラス
 * @param factory [recipe]からUIを作成するブロック
 * @param category レシピの[カテゴリ][EmiRecipeCategory]
 * @param id このレシピの[ID][ResourceLocation]
 * @param recipe [RECIPE]のインスタンス
 * @param bounds このレシピが表示される範囲
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 * @see HTHolderModularEmiRecipe
 */
abstract class HTModularEmiRecipe<RECIPE : Any>(
    factory: (RECIPE, UIElement) -> Unit,
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    protected val recipe: RECIPE,
    private val bounds: HTBounds,
) : ModularUIEMIRecipe({
        HTModularUIHelper.createVanillaUI(
            UIElement()
                .layout { it.widthPercent(100f).heightPercent(100f) }
                .alineCenter()
                .apply(factory.partially1(recipe)),
        )
    }) {
    constructor(
        factory: (RECIPE, UIElement) -> Unit,
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : this(factory, category, id, recipe, category.bounds)

    //    EmiRecipe    //

    final override fun getCategory(): EmiRecipeCategory = category

    final override fun getId(): ResourceLocation = id

    final override fun getDisplayWidth(): Int = bounds.width

    final override fun getDisplayHeight(): Int = bounds.height

    override fun getBackingRecipe(): RecipeHolder<*>? = null

    //    Extensions    //

    companion object {
        @JvmStatic
        fun inputSlot(ingredient: HTItemIngredient?): ItemSlot {
            val slot: ItemSlot = ingredient
                ?.let(::HTListItemSlot)
                ?.let(::HTItemSlotElement)
                ?: ItemSlot()
            return slot.xeiRecipeIngredient(IngredientIO.INPUT).xeiRecipeSlot()
        }

        @JvmStatic
        fun inputSlot(ingredient: HTFluidIngredient?): FluidSlot {
            val slot: FluidSlot = ingredient
                ?.let(::HTListFluidTank)
                ?.let(::HTFluidSlotElement)
                ?: FluidSlot()
            return slot.xeiRecipeIngredient(IngredientIO.INPUT).xeiRecipeSlot()
        }

        @JvmStatic
        fun catalystSlot(ingredient: HTItemIngredient?): ItemSlot {
            val slot: ItemSlot = ingredient
                ?.let(::HTListItemSlot)
                ?.let(::HTItemSlotElement)
                ?: ItemSlot()
            return slot.xeiRecipeIngredient(IngredientIO.CATALYST).xeiRecipeSlot()
        }

        @JvmStatic
        fun catalystSlot(ingredient: HTFluidIngredient?): FluidSlot {
            val slot: FluidSlot = ingredient
                ?.let(::HTListFluidTank)
                ?.let(::HTFluidSlotElement)
                ?: FluidSlot()
            return slot.xeiRecipeIngredient(IngredientIO.CATALYST).xeiRecipeSlot()
        }

        @JvmStatic
        fun outputSlot(result: HTItemResult, chance: Float = 1f): ItemSlot = ItemSlot()
            .setItem(
                result.getStackResult(null).mapOrElse(identity()) { message: Component ->
                    createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, message)
                },
            ).xeiRecipeIngredient(IngredientIO.OUTPUT)
            .xeiRecipeSlot(IngredientIO.OUTPUT, chance)

        @JvmStatic
        fun outputSlot(result: HTChancedItemResult): ItemSlot = outputSlot(result.result, result.chance.toFloat())

        @JvmStatic
        fun outputSlot(result: HTFluidResult): FluidSlot = FluidSlot()
            .setFluid(result.getStackOrEmpty(null))
            .xeiRecipeIngredient(IngredientIO.OUTPUT)
            .xeiRecipeSlot()
    }
}
