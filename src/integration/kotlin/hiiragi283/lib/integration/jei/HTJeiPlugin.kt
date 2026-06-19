package hiiragi283.lib.integration.jei

import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier

/**
 * Hiiragi Seriesで使用される[IModPlugin]の抽象クラスです。
 * @param modId 対象のMOD ID
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTJeiPlugin(protected val modId: String) : IModPlugin {
    final override fun getPluginUid(): Identifier = modId.toId("jei_plugin")

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
        fun <RECIPE : Any> getRecipeType(recipeType: HTRecipeViewerType<RECIPE>): IRecipeType<RECIPE> = recipeTypeCache.computeIfAbsent(recipeType) { recipeTypeIn: HTRecipeViewerType<*> ->
            IRecipeType.create(recipeTypeIn.getId(), recipeTypeIn.recipeClass)
        } as IRecipeType<RECIPE>
    }
}
