package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import mezz.jei.api.registration.IRecipeRegistration

/**
 * [IRecipeRegistration]へのレシピ登録を簡略化するヘルパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 * @see mekanism.client.recipe_viewer.jei.RecipeRegistryHelper
 */
data object HTJeiRecipeHelper {
    @JvmField
    val DISPLAY_SORTER: Comparator<in HTRecipeDisplay> = compareBy(HTComparators.ID, HTRecipeDisplay::getId)

    @JvmField
    val HOLDER_SORTER: Comparator<in HTRecipeHolder<*>> = compareBy(HTComparators.ID, HTRecipeHolder<*>::id)

    /**
     * @since 0.12.0
     */
    @JvmStatic
    fun <T : Any> addRecipes(registration: IRecipeRegistration, recipeType: JeiRecipeType<T>, recipes: Sequence<T>) {
        registration.addRecipes(recipeType, recipes.toList())
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
        this.addHolderRecipes(registration, viewerType, lookup.getAllRecipes())
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
        this.addHolderRecipes(registration, viewerType, lookup.getAllRecipes(), sorter)
    }

    // HTRecipeDisplay

    /**
     * @since 0.15.3
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
     * @since 0.15.3
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

    /**
     * @since 0.15.3
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
                .getAllRecipes()
                .mapNotNull(transform),
        )
    }

    /**
     * @since 0.15.3
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
                .getAllRecipes()
                .mapNotNull(transform),
            sorter,
        )
    }
}
