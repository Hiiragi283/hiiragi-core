package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.recipe.EmiRecipeCategory
import hiiragi283.core.api.math.HTBounds
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * バニラの[Recipe]に基づいた[HTEmiRecipe]の拡張クラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe
 */
abstract class HTEmiHolderRecipe<RECIPE : Recipe<*>> : HTEmiRecipe<RECIPE> {
    /**
     * レシピのID付きインスタンス
     */
    private val holder: RecipeHolder<RECIPE>

    constructor(category: EmiRecipeCategory, holder: RecipeHolder<RECIPE>, bounds: HTBounds) : super(
        category,
        holder.id,
        holder.value,
        bounds,
    ) {
        this.holder = holder
    }

    constructor(
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : super(category, id, recipe) {
        this.holder = RecipeHolder(id, recipe)
    }

    constructor(
        category: HTEmiRecipeCategory,
        holder: RecipeHolder<RECIPE>,
    ) : this(category, holder, category.bounds)

    /**
     * EMIがレシピのIDを取得するために必要
     */
    override fun getBackingRecipe(): RecipeHolder<*> = holder
}
