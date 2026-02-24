package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTFakeRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
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
    final override fun getPluginUid(): ResourceLocation = modId.toId("jei_plugin")

    //    Extensions    //

    companion object {
        // Recipe Type
        @JvmStatic
        private val recipeTypeCache: MutableMap<HTRecipeViewerType<*>, JeiRecipeType<*>> = hashMapOf()

        /**
         * 指定した[recipeType]から[JeiRecipeType]を取得します。
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <RECIPE : Any> getRecipeType(recipeType: HTRecipeViewerType<RECIPE>): JeiRecipeType<RECIPE> =
            recipeTypeCache.computeIfAbsent(recipeType) { recipeTypeIn: HTRecipeViewerType<*> ->
                JeiRecipeType(recipeTypeIn.getId(), recipeTypeIn.recipeClass)
            } as JeiRecipeType<RECIPE>

        // Recipe
        @JvmStatic
        private fun <HOLDER : Any> createSorter(lookup: HTRecipeLookup<*, *, HOLDER>): Comparator<HOLDER> =
            compareBy(HTComparators.ID, lookup::getId)

        @JvmStatic
        protected fun <T : Any> IRecipeRegistration.addRecipes(recipeType: JeiRecipeType<T>, recipes: Sequence<T>) {
            this.addRecipes(recipeType, recipes.toList())
        }

        /**
         * 指定した[recipeType]と[lookup]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Any, HOLDER : Any> IRecipeRegistration.addRecipes(
            recipeType: HTRecipeViewerType<HOLDER>,
            lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                lookup.getAllRecipes().sortedWith(createSorter(lookup)),
            )
        }

        /**
         * 指定した[recipeType]と[lookup]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
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
                    .getAllRecipes()
                    .sortedWith(compareBy(sorter, lookup::getRecipe).thenComparing(createSorter(lookup))),
            )
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTHolderRecipeViewerType<INPUT, RECIPE>,
        ) {
            this.addRecipes(getRecipeType(recipeType), recipeType.getAllRecipes().sortedWith(createSorter(recipeType)))
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param sorter レシピの順番の制御
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTHolderRecipeViewerType<INPUT, RECIPE>,
            sorter: Comparator<RECIPE>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                recipeType
                    .getAllRecipes()
                    .sortedWith(compareBy(sorter, recipeType::getRecipe).thenComparing(createSorter(recipeType))),
            )
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Any> IRecipeRegistration.addRecipes(
            recipeType: HTFakeRecipeViewerType<INPUT, RECIPE>,
        ) {
            this.addRecipes(getRecipeType(recipeType), recipeType.getAllRecipes().sortedWith(createSorter(recipeType)))
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param sorter レシピの順番の制御
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Any> IRecipeRegistration.addRecipes(
            recipeType: HTFakeRecipeViewerType<INPUT, RECIPE>,
            sorter: Comparator<RECIPE>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                recipeType
                    .getAllRecipes()
                    .sortedWith(compareBy(sorter, recipeType::getRecipe).thenComparing(createSorter(recipeType))),
            )
        }

        @JvmStatic
        protected fun IRecipeCatalystRegistration.addRecipeCatalysts(vararg recipeTypes: HTRecipeViewerType<*>) {
            for (recipeType: HTRecipeViewerType<*> in recipeTypes) {
                this.addRecipeCatalysts(
                    getRecipeType(recipeType),
                    VanillaTypes.ITEM_STACK,
                    recipeType.workStations,
                )
            }
        }
    }
}
