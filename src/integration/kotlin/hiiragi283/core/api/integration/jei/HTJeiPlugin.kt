package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.ResourceLocation

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[IModPlugin]の抽象クラスです。
 *
 * 参照 : [Mekanism - MekanismJEI](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/recipe_viewer/jei/MekanismJEI.java)
 * @param modId 対象のMOD ID
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
abstract class HTJeiPlugin(protected val modId: String) : IModPlugin {
    final override fun getPluginUid(): ResourceLocation = modId.toId("jei_plugin")

    final override fun registerRecipes(registration: IRecipeRegistration) {
        registerRecipes(HTJeiRecipeHelper(registration))
    }

    protected abstract fun registerRecipes(helper: HTJeiRecipeHelper)

    final override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registerRecipeCatalysts(HTJeiWorkstationHelper(registration))
    }

    protected abstract fun registerRecipeCatalysts(helper: HTJeiWorkstationHelper)

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
        fun <RECIPE> getRecipeType(recipeType: HTRecipeViewerType<RECIPE>): JeiRecipeType<RECIPE> = recipeTypeCache.computeIfAbsent(recipeType) { recipeTypeIn: HTRecipeViewerType<*> ->
            JeiRecipeType(recipeTypeIn.getId(), recipeTypeIn.recipeClass)
        } as JeiRecipeType<RECIPE>
    }
}
