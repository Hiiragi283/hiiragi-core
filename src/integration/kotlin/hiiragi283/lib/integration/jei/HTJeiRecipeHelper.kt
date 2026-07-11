package hiiragi283.lib.integration.jei

import hiiragi283.lib.HTComparators
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay
import java.util.function.Supplier
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * [IRecipeRegistration]へのレシピ登録を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class HTJeiRecipeHelper(@PublishedApi internal val registration: IRecipeRegistration) {
    companion object {
        @JvmStatic
        fun createContext(): ContextMap = HTPhysicalSideHelper.runForSide(HTRecipeLookupContext::createOnClient, HTRecipeLookupContext::create) ?: ContextMap.EMPTY

        @JvmField
        val DISPLAY_SORTER: Comparator<in HTRecipeDisplay> = compareBy(HTComparators.ID, HTRecipeDisplay::getId)
    }

    fun <T : Any> addRecipes(recipeType: IRecipeType<T>, recipes: Sequence<T>) {
        val list: List<T> = recipes.toList()
        if (list.isEmpty()) return
        registration.addRecipes(recipeType, list)
    }

    // HTRecipeViewerType
    fun <T : Any> addRecipes(viewerType: HTRecipeViewerType<T>, recipes: Sequence<T>) {
        this.addRecipes(HTJeiPlugin.getRecipeType(viewerType), recipes)
    }

    fun <T : Any> addRecipes(viewerType: HTRecipeViewerType<T>, recipes: Sequence<T>, sorter: Comparator<in T>) {
        this.addRecipes(viewerType, recipes.sortedWith(sorter))
    }

    // HTRecipeDisplay
    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipes: Sequence<DISPLAY>) {
        this.addRecipes(viewerType, recipes, DISPLAY_SORTER)
    }

    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipes: Sequence<DISPLAY>, sorter: Comparator<DISPLAY>) {
        this.addRecipes(viewerType, recipes, sorter.thenComparing(DISPLAY_SORTER))
    }

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipeType: RecipeType<RECIPE>, transform: (HTRecipeHolder<RECIPE>) -> DISPLAY) {
        this.addDisplayRecipes(viewerType, HTVanillaRecipeLookup(recipeType).asSequence(createContext()).map(transform))
    }

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipeType: Supplier<out RecipeType<RECIPE>>, transform: (HTRecipeHolder<RECIPE>) -> DISPLAY) {
        this.addDisplayRecipes(viewerType, HTVanillaRecipeLookup(recipeType).asSequence(createContext()).map(transform))
    }

    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, lookup: HTRecipeLookup<BASE>, transform: (HTRecipeHolder<BASE>) -> DISPLAY?) {
        this.addDisplayRecipes(viewerType, lookup.asSequence(createContext()).mapNotNull(transform))
    }

    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, lookup: HTRecipeLookup<BASE>, sorter: Comparator<DISPLAY>, transform: (HTRecipeHolder<BASE>) -> DISPLAY?) {
        this.addDisplayRecipes(viewerType, lookup.asSequence(createContext()).mapNotNull(transform), sorter)
    }
}
