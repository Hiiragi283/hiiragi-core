package hiiragi283.core.api.integration.emi

import com.lowdragmc.lowdraglib2.gui.ui.utils.IModularUIProvider
import dev.emi.emi.api.recipe.EmiRecipeCategory
import hiiragi283.core.api.math.HTBounds
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * バニラの[Recipe]に基づいた[HTModularEmiRecipe]の拡張クラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
abstract class HTHolderModularEmiRecipe<RECIPE : Recipe<*>> : HTModularEmiRecipe<RECIPE> {
    /**
     * レシピのID付きインスタンス
     */
    private val holder: RecipeHolder<RECIPE>

    constructor(
        provider: IModularUIProvider<RECIPE>,
        category: EmiRecipeCategory,
        holder: RecipeHolder<RECIPE>,
        bounds: HTBounds,
    ) : super(
        provider,
        category,
        holder.id,
        holder.value,
        bounds,
    ) {
        this.holder = holder
    }

    constructor(
        provider: IModularUIProvider<RECIPE>,
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : super(provider, category, id, recipe) {
        this.holder = RecipeHolder(id, recipe)
    }

    constructor(
        provider: IModularUIProvider<RECIPE>,
        category: HTEmiRecipeCategory,
        holder: RecipeHolder<RECIPE>,
    ) : this(provider, category, holder, category.bounds)

    /**
     * EMIがレシピのIDを取得するために必要
     */
    final override fun getBackingRecipe(): RecipeHolder<*> = holder
}
