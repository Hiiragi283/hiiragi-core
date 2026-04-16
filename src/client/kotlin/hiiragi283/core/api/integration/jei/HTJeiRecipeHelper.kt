package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import mezz.jei.api.registration.IRecipeRegistration

/**
 * [IRecipeRegistration]へのレシピ登録を簡略化するヘルパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 * @see mekanism.client.recipe_viewer.jei.RecipeRegistryHelper
 */
data object HTJeiRecipeHelper {
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
    fun <T : Any> addLookupRecipes(
        registration: IRecipeRegistration,
        viewerType: HTHolderRecipeViewerType<T>,
        lookup: HTRecipeLookup<*, T>,
    ) {
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
        lookup: HTRecipeLookup<*, T>,
        sorter: Comparator<in T>,
    ) {
        this.addHolderRecipes(registration, viewerType, lookup.getAllRecipes(), sorter)
    }

    // HTLookupRecipeViewerType

    /**
     * 指定した[viewerType]と[lookup]からレシピを登録します。
     * @param BASE [lookup]で取得できるレシピのクラス
     * @param RECIPE [BASE]を継承したクラス
     * @since 0.15.1
     */
    @JvmStatic
    inline fun <BASE : HTRecipe<*>, reified RECIPE : BASE> addLookupRecipes(
        registration: IRecipeRegistration,
        viewerType: HTLookupRecipeViewerType<BASE, RECIPE>,
        lookup: HTRecipeLookup<*, BASE>,
    ) {
        this.addHolderRecipes(
            registration,
            viewerType,
            lookup
                .getAllRecipes()
                .mapNotNull { holder: HTRecipeHolder<BASE> -> holder.mapRecipeOrNull { it as? RECIPE } },
        )
    }

    /**
     * 指定した[viewerType]と[lookup]からレシピを登録します。
     * @param BASE [lookup]で取得できるレシピのクラス
     * @param RECIPE [BASE]を継承したクラス
     * @param sorter レシピの順番の制御
     * @since 0.11.0
     */
    @JvmStatic
    inline fun <BASE : HTRecipe<*>, reified RECIPE : BASE> addLookupRecipes(
        registration: IRecipeRegistration,
        viewerType: HTLookupRecipeViewerType<BASE, RECIPE>,
        lookup: HTRecipeLookup<*, BASE>,
        sorter: Comparator<in RECIPE>,
    ) {
        this.addHolderRecipes(
            registration,
            viewerType,
            lookup
                .getAllRecipes()
                .mapNotNull { holder: HTRecipeHolder<BASE> -> holder.mapRecipeOrNull { it as? RECIPE } },
            sorter,
        )
    }
}
