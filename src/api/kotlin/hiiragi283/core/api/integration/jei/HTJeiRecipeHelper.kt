package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.id
import hiiragi283.core.api.recipe.recipe
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * [IRecipeRegistration]へのレシピ登録を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@JvmInline
value class HTJeiRecipeHelper(@PublishedApi internal val registration: IRecipeRegistration) {
    companion object {
        @JvmStatic
        private fun createContext(): HTRecipeLookup.Context = Minecraft
            .getInstance()
            .level
            ?.let(HTRecipeLookup.Context::create)
            ?: error("Cannot create recipe lookup context on client side")

        @JvmField
        val DISPLAY_SORTER: Comparator<in HTRecipeDisplay> = compareBy(HTComparators.ID, HTRecipeDisplay::getId)

        @JvmField
        val HOLDER_SORTER: Comparator<in HTRecipeHolder<*>> = compareBy(HTComparators.ID, HTRecipeHolder<*>::id)
    }

    fun <T : Any> addRecipes(recipeType: JeiRecipeType<T>, recipes: Sequence<T>) {
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

    // HTRecipeHolder
    fun <T : Any> addHolderRecipes(viewerType: HTHolderRecipeViewerType<T>, recipes: Sequence<HTRecipeHolder<T>>) {
        this.addRecipes(viewerType, recipes, HOLDER_SORTER)
    }

    fun <T : Any> addHolderRecipes(viewerType: HTHolderRecipeViewerType<T>, recipes: Sequence<HTRecipeHolder<T>>, sorter: Comparator<in T>) {
        this.addRecipes(viewerType, recipes, compareBy(sorter, HTRecipeHolder<T>::recipe).thenComparing(HOLDER_SORTER))
    }

    // HTRecipeLookup
    fun <T : Any> addLookupRecipes(viewerType: HTHolderRecipeViewerType<T>, lookup: HTRecipeLookup<T>) {
        this.addHolderRecipes(viewerType, lookup.asSequence(createContext()))
    }

    fun <T : Any> addLookupRecipes(viewerType: HTHolderRecipeViewerType<T>, lookup: HTRecipeLookup<T>, sorter: Comparator<in T>) {
        this.addHolderRecipes(viewerType, lookup.asSequence(createContext()), sorter)
    }

    // HTRecipeDisplay
    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipes: Sequence<DISPLAY>) {
        this.addRecipes(viewerType, recipes, DISPLAY_SORTER)
    }

    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipes: Sequence<DISPLAY>, sorter: Comparator<DISPLAY>) {
        this.addRecipes(viewerType, recipes, sorter.thenComparing(DISPLAY_SORTER))
    }

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, recipeType: RecipeType<RECIPE>, transform: (HTRecipeHolder<RECIPE>) -> DISPLAY) {
        this.addDisplayRecipes(viewerType, HiiragiCoreAccess.INSTANCE.asSequence(createContext(), recipeType).map(transform))
    }

    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, lookup: HTRecipeLookup<BASE>, transform: (HTRecipeHolder<BASE>) -> DISPLAY?) {
        this.addDisplayRecipes(viewerType, lookup.asSequence(createContext()).mapNotNull(transform))
    }

    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, lookup: HTRecipeLookup<BASE>, sorter: Comparator<DISPLAY>, transform: (HTRecipeHolder<BASE>) -> DISPLAY?) {
        this.addDisplayRecipes(viewerType, lookup.asSequence(createContext()).mapNotNull(transform), sorter)
    }

    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addFlatDisplayRecipes(viewerType: HTRecipeViewerType<DISPLAY>, lookup: HTRecipeLookup<BASE>, transform: (HTRecipeHolder<BASE>) -> Sequence<DISPLAY>) {
        this.addDisplayRecipes(viewerType, lookup.asSequence(createContext()).flatMap(transform))
    }
}
