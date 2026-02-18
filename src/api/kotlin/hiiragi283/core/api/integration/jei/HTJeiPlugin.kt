package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

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
        val recipeTypeCache: MutableMap<HTJeiRecipeType<*>, JeiRecipeType<*>> = hashMapOf()

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <RECIPE : Any> getRecipeType(recipeType: HTJeiRecipeType<RECIPE>): JeiRecipeType<RECIPE> =
            recipeTypeCache.computeIfAbsent(recipeType) { recipeTypeIn: HTJeiRecipeType<*> ->
                JeiRecipeType(recipeTypeIn.getId(), recipeTypeIn.recipeClass)
            } as JeiRecipeType<RECIPE>

        // Recipe
        @JvmStatic
        protected fun getRecipeManager(): RecipeManager = runForDist(
            { Minecraft.getInstance().level?.recipeManager },
            { HiiragiCoreAPI.getActiveServer()?.recipeManager },
        ) ?: error("Failed to access vanilla recipe manager")

        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTJeiHolderRecipeType<RECIPE>,
            recipeType1: RecipeType<RECIPE>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                getRecipeManager().getAllRecipesFor(recipeType1).sortedWith(compareBy { it.id }),
            )
        }

        @JvmStatic
        protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTJeiHolderRecipeType<RECIPE>,
            recipeType1: RecipeType<RECIPE>,
            sorter: Comparator<RECIPE>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                getRecipeManager()
                    .getAllRecipesFor(recipeType1)
                    .sortedWith(compareBy<RecipeHolder<RECIPE>, RECIPE>(sorter) { it.value }.thenComparing { it.id }),
            )
        }

        @JvmStatic
        protected inline fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> IRecipeRegistration.addRecipes(
            recipeType: HTJeiHolderRecipeType<RECIPE>,
            recipeType1: RecipeType<RECIPE>,
            crossinline sorter: (RECIPE) -> Comparable<*>,
        ) {
            this.addRecipes(
                getRecipeType(recipeType),
                getRecipeManager()
                    .getAllRecipesFor(recipeType1)
                    .sortedWith(compareBy<RecipeHolder<RECIPE>, RECIPE>(compareBy(sorter)) { it.value }.thenComparing { it.id }),
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
