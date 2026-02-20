package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.integration.jei.type.HTHolderJeiRecipeType
import hiiragi283.core.api.integration.jei.type.HTJeiRecipeType
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
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
        private val recipeTypeCache: MutableMap<HTJeiRecipeType<*>, JeiRecipeType<*>> = hashMapOf()

        /**
         * 指定した[recipeType]から[JeiRecipeType]を取得します。
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <RECIPE : Any> getRecipeType(recipeType: HTJeiRecipeType<RECIPE>): JeiRecipeType<RECIPE> =
            recipeTypeCache.computeIfAbsent(recipeType) { recipeTypeIn: HTJeiRecipeType<*> ->
                JeiRecipeType(recipeTypeIn.getId(), recipeTypeIn.recipeClass)
            } as JeiRecipeType<RECIPE>

        // Recipe
        @JvmStatic
        protected val RECIPE_COMPARATOR: Comparator<RecipeHolder<*>> = compareBy(HTComparators.ID) { it.id }

        /**
         * 指定した[recipeType]と[lookup]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTJeiRecipeType<RecipeHolder<RECIPE>>,
            lookup: HTRecipeLookup<INPUT, RECIPE>,
        ) {
            this.addRecipes(getRecipeType(recipeType), lookup.getAllRecipes().sortedWith(RECIPE_COMPARATOR))
        }

        /**
         * 指定した[recipeType]と[lookup]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param sorter レシピの順番の制御
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTJeiRecipeType<RecipeHolder<RECIPE>>,
            lookup: HTRecipeLookup<INPUT, RECIPE>,
            sorter: Comparator<RECIPE>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                lookup
                    .getAllRecipes()
                    .sortedWith(compareBy<RecipeHolder<RECIPE>, RECIPE>(sorter) { it.value }.thenComparing(RECIPE_COMPARATOR)),
            )
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTHolderJeiRecipeType<INPUT, RECIPE>,
        ) {
            this.addRecipes(getRecipeType(recipeType), recipeType.getAllRecipes().sortedWith(RECIPE_COMPARATOR))
        }

        /**
         * 指定した[recipeType]からレシピを登録します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         * @param sorter レシピの順番の制御
         */
        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTHolderJeiRecipeType<INPUT, RECIPE>,
            sorter: Comparator<RECIPE>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                recipeType
                    .getAllRecipes()
                    .sortedWith(compareBy<RecipeHolder<RECIPE>, RECIPE>(sorter) { it.value }.thenComparing(RECIPE_COMPARATOR)),
            )
        }

        @JvmStatic
        protected fun IRecipeCatalystRegistration.addRecipeCatalysts(vararg recipeTypes: HTJeiRecipeType<*>) {
            for (recipeType: HTJeiRecipeType<*> in recipeTypes) {
                this.addRecipeCatalysts(
                    getRecipeType(recipeType),
                    VanillaTypes.ITEM_STACK,
                    recipeType.workStations,
                )
            }
        }
    }
}
