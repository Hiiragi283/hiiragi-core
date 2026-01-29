package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialManager
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * レシピ生成で使用される抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see HTSubRecipeProvider
 * @see HTRegisterRuntimeRecipeEvent
 */
abstract class HTRecipeProviderContext {
    /**
     * レジストリへのアクセスを取得します。
     */
    abstract val provider: HolderLookup.Provider

    /**
     * レシピの出力先を取得します。
     */
    protected abstract val output: RecipeOutput

    /**
     * 指定した[recipe]を進捗なしで登録します。
     */
    fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }

    /**
     * 材料を作成するヘルパーを取得します。
     */
    protected val inputCreator: HTIngredientCreator get() = HTIngredientCreator

    /**
     * 完成品を作成するヘルパーを取得します。
     */
    protected val resultCreator: HTResultCreator get() = HTResultCreator

    /**
     * 素材を管理するマネージャを取得します。
     */
    protected val materialManager: HTMaterialManager by lazy(HiiragiCoreAccess.INSTANCE::materialManager)

    /**
     * ほかの[HTRecipeProviderContext]に実装を依存した[HTRecipeProviderContext]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    abstract class Delegated : HTRecipeProviderContext() {
        protected abstract val delegated: HTRecipeProviderContext

        final override val provider: HolderLookup.Provider
            get() = delegated.provider
        final override val output: RecipeOutput
            get() = delegated.output
    }
}
