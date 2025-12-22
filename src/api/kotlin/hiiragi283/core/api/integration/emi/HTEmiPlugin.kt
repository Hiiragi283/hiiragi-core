package hiiragi283.core.api.integration.emi

import com.mojang.logging.LogUtils
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipe
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import org.slf4j.Logger

abstract class HTEmiPlugin(protected val modId: String) : EmiPlugin {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Extensions    //

    protected fun addCategory(registry: EmiRegistry, category: HTEmiRecipeCategory) {
        registry.addCategory(category)
        category.workStations.forEach(registry::addWorkstation.partially1(category))
    }

    protected fun skipRecipe(id: ResourceLocation) {
        LOGGER.warn("Skipped recipe for EMI registration: $id")
    }

    protected inline fun addRecipeSafe(registry: EmiRegistry, id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            registry.addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }

    protected inline fun addCraftingRecipe(
        registry: EmiRegistry,
        id: ResourceLocation,
        prefix: String,
        factory: (ResourceLocation) -> EmiRecipe,
    ) {
        addRecipeSafe(registry, id.withPrefix("/shapeless/$modId/$prefix"), factory)
    }

    /**
     * @see mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes
     */
    protected inline fun <BASE : HTRecipe, reified RECIPE : BASE, EMI_RECIPE : EmiRecipe> addRegistryRecipes(
        registry: EmiRegistry,
        recipeType: HTHolderLike<RecipeType<*>, RecipeType<BASE>>,
        noinline factory: (RecipeHolder<RECIPE>) -> EMI_RECIPE?,
    ) {
        registry.recipeManager
            .getAllRecipesFor(recipeType.get())
            .asSequence()
            .mapNotNull { holder: RecipeHolder<BASE> ->
                val id: ResourceLocation = holder.id
                val recipe: BASE = holder.value
                if (recipe is RECIPE) {
                    RecipeHolder(id, recipe)
                } else {
                    skipRecipe(id)
                    null
                }
            }.mapNotNull(factory)
            .forEach(registry::addRecipe)
    }

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param RECIPE [recipes]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     */
    private fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addRecipes(
        registry: EmiRegistry,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: Factory<RECIPE, EMI_RECIPE>,
    ) {
        recipes.mapNotNull(factory::create).forEach(registry::addRecipe)
    }

    fun interface Factory<RECIPE : Any, EMI_RECIPE : EmiRecipe> {
        fun create(id: ResourceLocation, recipe: RECIPE): EMI_RECIPE?

        fun create(pair: Pair<ResourceLocation, RECIPE>): EMI_RECIPE? = create(pair.first, pair.second)
    }
}
