package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.RecipeKey
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.RecipeInput

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[IModPlugin]の抽象クラスです。
 * @param modId 対象のMOD ID
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 * @see mekanism.client.recipe_viewer.jei.MekanismJEI
 * @see mekanism.client.recipe_viewer.jei.RecipeRegistryHelper
 */
abstract class HTJeiPlugin(protected val modId: String) : IModPlugin {
    override fun getPluginUid(): Identifier = modId.toId("jei_plugin")

    //    Extensions    //

    companion object {
        // Recipe Type
        @JvmStatic
        private val recipeTypeCache: MutableMap<HTRecipeViewerType<*>, IRecipeType<*>> = hashMapOf()

        /**
         * 指定した[recipeType]から[IRecipeType]を取得します。
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <RECIPE : Any> getRecipeType(recipeType: HTRecipeViewerType<RECIPE>): IRecipeType<RECIPE> =
            recipeTypeCache.computeIfAbsent(recipeType) { recipeTypeIn: HTRecipeViewerType<*> ->
                IRecipeType.create(recipeTypeIn.getId(), recipeTypeIn.recipeClass)
            } as IRecipeType<RECIPE>

        // Recipe
        @JvmStatic
        private fun <HOLDER : Any> createSorter(lookup: HTRecipeLookup<*, *, HOLDER>): Comparator<HOLDER> =
            compareBy(HTComparators.ID, lookup::getId)

        @JvmStatic
        protected fun createLookupContext(): HTRecipeLookup.Context = TODO()

        /**
         * @since 0.12.0
         */
        @JvmStatic
        protected fun <T : Any> IRecipeRegistration.addRecipes(recipeType: IRecipeType<T>, recipes: Sequence<T>) {
            this.addRecipes(recipeType, recipes.toList())
        }

        /**
         * 指定した[recipeType]と[lookup]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param HOLDER [RecipeKey]と[RECIPE]を束ねたクラス
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Any, HOLDER : Any> IRecipeRegistration.addRecipes(
            recipeType: HTRecipeViewerType<HOLDER>,
            lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                lookup.getAllRecipes(createLookupContext()).sortedWith(createSorter(lookup)),
            )
        }

        /**
         * 指定した[recipeType]と[lookup]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param HOLDER [RecipeKey]と[RECIPE]を束ねたクラス
         * @param sorter レシピの順番の制御
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Any, HOLDER : Any> IRecipeRegistration.addRecipes(
            recipeType: HTRecipeViewerType<HOLDER>,
            lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
            sorter: Comparator<RECIPE>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                lookup
                    .getAllRecipes(createLookupContext())
                    .sortedWith(compareBy(sorter, lookup::getRecipe).thenComparing(createSorter(lookup))),
            )
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param HOLDER [RecipeKey]と[RECIPE]を束ねたクラス
         * @param TYPE [HTRecipeViewerType]と[HTRecipeLookup]を実装したクラス
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Any, HOLDER : Any, TYPE> IRecipeRegistration.addRecipes(
            recipeType: TYPE,
        ) where TYPE : HTRecipeViewerType<HOLDER>, TYPE : HTRecipeLookup<INPUT, RECIPE, HOLDER> {
            this.addRecipes(
                getRecipeType(recipeType),
                recipeType.getAllRecipes(createLookupContext()).sortedWith(createSorter(recipeType)),
            )
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param HOLDER [RecipeKey]と[RECIPE]を束ねたクラス
         * @param TYPE [HTRecipeViewerType]と[HTRecipeLookup]を実装したクラス
         * @param sorter レシピの順番の制御
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Any, HOLDER : Any, TYPE> IRecipeRegistration.addRecipes(
            recipeType: TYPE,
            sorter: Comparator<RECIPE>,
        ) where TYPE : HTRecipeViewerType<HOLDER>, TYPE : HTRecipeLookup<INPUT, RECIPE, HOLDER> {
            this.addRecipes(
                getRecipeType(recipeType),
                recipeType
                    .getAllRecipes(createLookupContext())
                    .sortedWith(compareBy(sorter, recipeType::getRecipe).thenComparing(createSorter(recipeType))),
            )
        }

        @JvmStatic
        protected fun IRecipeCatalystRegistration.addCraftingStations(vararg recipeTypes: HTRecipeViewerType<*>) {
            for (recipeType: HTRecipeViewerType<*> in recipeTypes) {
                this.addCraftingStations(
                    getRecipeType(recipeType),
                    VanillaTypes.ITEM_STACK,
                    recipeType.workStations.map(ItemStackTemplate::create),
                )
            }
        }
    }
}
