package hiiragi283.lib.integration.jei

import hiiragi283.lib.HTComparators
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.viewer.HTHolderRecipeViewerType
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
 * [IRecipeRegistration]へのレシピ登録を簡略化するヘルパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
data object HTJeiRecipeHelper {
    @JvmStatic
    fun createContext(): ContextMap = HTPhysicalSideHelper.runForSide(HTRecipeLookupContext::createOnClient, HTRecipeLookupContext::create) ?: ContextMap.EMPTY

    @JvmField
    val DISPLAY_SORTER: Comparator<in HTRecipeDisplay> = compareBy(HTComparators.ID, HTRecipeDisplay::getId)

    @JvmField
    val HOLDER_SORTER: Comparator<in HTRecipeHolder<*>> = compareBy(HTComparators.ID, HTRecipeHolder<*>::getId)

    /**
     * @since 0.12.0
     */
    @JvmStatic
    fun <T : Any> addRecipes(registration: IRecipeRegistration, recipeType: IRecipeType<T>, recipes: Sequence<T>) {
        val list: List<T> = recipes.toList()
        if (list.isEmpty()) return
        registration.addRecipes(recipeType, list)
    }

    // HTRecipeViewerType

    /**
     * @since 0.15.1
     */
    @JvmStatic
    fun <T : Any> addRecipes(registration: IRecipeRegistration, viewerType: HTRecipeViewerType<T>, recipes: Sequence<T>) {
        this.addRecipes(registration, HTJeiPlugin.getRecipeType(viewerType), recipes)
    }

    /**
     * @since 0.15.1
     */
    @JvmStatic
    fun <T : Any> addRecipes(
        registration: IRecipeRegistration,
        viewerType: HTRecipeViewerType<T>,
        recipes: Sequence<T>,
        sorter: Comparator<in T>,
    ) {
        this.addRecipes(registration, viewerType, recipes.sortedWith(sorter))
    }

    // HTRecipeHolder

    /**
     * @since 0.15.1
     */
    @JvmStatic
    fun <T : Any> addHolderRecipes(
        registration: IRecipeRegistration,
        viewerType: HTHolderRecipeViewerType<T>,
        recipes: Sequence<HTRecipeHolder<T>>,
    ) {
        this.addRecipes(registration, viewerType, recipes, HOLDER_SORTER)
    }

    /**
     * @since 0.15.1
     */
    @JvmStatic
    fun <T : Any> addHolderRecipes(
        registration: IRecipeRegistration,
        viewerType: HTHolderRecipeViewerType<T>,
        recipes: Sequence<HTRecipeHolder<T>>,
        sorter: Comparator<in T>,
    ) {
        this.addRecipes(registration, viewerType, recipes, compareBy(sorter, HTRecipeHolder<T>::recipe).thenComparing(HOLDER_SORTER))
    }

    // HTRecipeLookup

    /**
     * 指定した[viewerType]と[lookup]からレシピを登録します。
     * @param T レシピのクラス
     * @since 0.11.0
     */
    @JvmStatic
    fun <T : Any> addLookupRecipes(registration: IRecipeRegistration, viewerType: HTHolderRecipeViewerType<T>, lookup: HTRecipeLookup<T>) {
        this.addHolderRecipes(registration, viewerType, lookup.getAllRecipes(createContext()))
    }

    /**
     * 指定した[viewerType]と[lookup]からレシピを登録します。
     * @param T レシピのクラス
     * @param sorter レシピの順番の制御
     * @since 0.11.0
     */
    @JvmStatic
    fun <T : Any> addLookupRecipes(
        registration: IRecipeRegistration,
        viewerType: HTHolderRecipeViewerType<T>,
        lookup: HTRecipeLookup<T>,
        sorter: Comparator<in T>,
    ) {
        this.addHolderRecipes(registration, viewerType, lookup.getAllRecipes(createContext()), sorter)
    }

    // HTRecipeDisplay

    /**
     * @since 0.16.0
     */
    @JvmStatic
    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(
        registration: IRecipeRegistration,
        viewerType: HTRecipeViewerType<DISPLAY>,
        recipes: Sequence<DISPLAY>,
    ) {
        this.addRecipes(registration, viewerType, recipes, DISPLAY_SORTER)
    }

    /**
     * @since 0.16.0
     */
    @JvmStatic
    fun <DISPLAY : HTRecipeDisplay> addDisplayRecipes(
        registration: IRecipeRegistration,
        viewerType: HTRecipeViewerType<DISPLAY>,
        recipes: Sequence<DISPLAY>,
        sorter: Comparator<DISPLAY>,
    ) {
        this.addRecipes(registration, viewerType, recipes, sorter.thenComparing(DISPLAY_SORTER))
    }

    @JvmStatic
    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, DISPLAY : HTRecipeDisplay> addDisplayRecipes(
        registration: IRecipeRegistration,
        viewerType: HTRecipeViewerType<DISPLAY>,
        recipeType: RecipeType<RECIPE>,
        transform: (HTRecipeHolder<RECIPE>) -> DISPLAY,
    ) {
        this.addDisplayRecipes(
            registration,
            viewerType,
            HTVanillaRecipeLookup(recipeType).getAllRecipes(createContext()).map(transform),
        )
    }

    @JvmStatic
    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, DISPLAY : HTRecipeDisplay> addDisplayRecipes(
        registration: IRecipeRegistration,
        viewerType: HTRecipeViewerType<DISPLAY>,
        recipeType: Supplier<out RecipeType<RECIPE>>,
        transform: (HTRecipeHolder<RECIPE>) -> DISPLAY,
    ) {
        this.addDisplayRecipes(
            registration,
            viewerType,
            HTVanillaRecipeLookup(recipeType).getAllRecipes(createContext()).map(transform),
        )
    }

    /**
     * @since 0.16.0
     */
    @JvmStatic
    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(
        registration: IRecipeRegistration,
        viewerType: HTRecipeViewerType<DISPLAY>,
        lookup: HTRecipeLookup<BASE>,
        transform: (HTRecipeHolder<BASE>) -> DISPLAY?,
    ) {
        this.addDisplayRecipes(
            registration,
            viewerType,
            lookup
                .getAllRecipes(createContext())
                .mapNotNull(transform),
        )
    }

    /**
     * @since 0.16.0
     */
    @JvmStatic
    fun <BASE : Any, DISPLAY : HTRecipeDisplay> addDisplayRecipes(
        registration: IRecipeRegistration,
        viewerType: HTRecipeViewerType<DISPLAY>,
        lookup: HTRecipeLookup<BASE>,
        sorter: Comparator<DISPLAY>,
        transform: (HTRecipeHolder<BASE>) -> DISPLAY?,
    ) {
        this.addDisplayRecipes(
            registration,
            viewerType,
            lookup
                .getAllRecipes(createContext())
                .mapNotNull(transform),
            sorter,
        )
    }
}
