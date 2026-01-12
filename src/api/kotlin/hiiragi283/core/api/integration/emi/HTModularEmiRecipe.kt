package hiiragi283.core.api.integration.emi

import com.lowdragmc.lowdraglib2.gui.ui.utils.IModularUIProvider
import com.lowdragmc.lowdraglib2.integration.xei.emi.ModularUIEMIRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import hiiragi283.core.api.math.HTBounds
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[EmiRecipe]の抽象クラスです。
 * @param RECIPE 元となるレシピのクラス
 * @param provider [recipe]からUIを作成するブロック
 * @param category レシピの[カテゴリ][EmiRecipeCategory]
 * @param id このレシピの[ID][ResourceLocation]
 * @param recipe [RECIPE]のインスタンス
 * @param bounds このレシピが表示される範囲
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 * @see HTHolderModularEmiRecipe
 */
abstract class HTModularEmiRecipe<RECIPE : Any>(
    provider: IModularUIProvider<RECIPE>,
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    protected val recipe: RECIPE,
    private val bounds: HTBounds,
) : ModularUIEMIRecipe({ provider.createModularUI(recipe) }) {
    constructor(
        provider: IModularUIProvider<RECIPE>,
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : this(provider, category, id, recipe, category.bounds)

    //    EmiRecipe    //

    final override fun getCategory(): EmiRecipeCategory = category

    final override fun getId(): ResourceLocation = id

    final override fun getDisplayWidth(): Int = bounds.width

    final override fun getDisplayHeight(): Int = bounds.height

    override fun getBackingRecipe(): RecipeHolder<*>? = null
}
