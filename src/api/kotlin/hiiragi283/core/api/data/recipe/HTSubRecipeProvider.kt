package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.core.api.data.recipe.ingredient.HTIngredientAccess
import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.core.api.data.recipe.result.HTFluidResultCreator
import hiiragi283.core.api.data.recipe.result.HTItemResultCreator
import hiiragi283.core.api.resource.toId
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition

/**
 * レシピを登録するクラスです。
 * @param modId 生成対象のMOD ID
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
sealed class HTSubRecipeProvider(protected val modId: String) {
    /**
     * [レジストリ][HolderLookup.Provider]のインスタンス
     */
    protected lateinit var provider: HolderLookup.Provider
        private set

    /**
     * [レシピの出力先][RecipeOutput]のインスタンス
     */
    protected lateinit var output: RecipeOutput
        private set

    /**
     * 液体の材料を作成するヘルパーのインスタンス
     */
    protected val fluidCreator: HTFluidIngredientCreator by lazy { HTIngredientAccess.INSTANCE.fluidCreator() }

    /**
     * アイテムの材料を作成するヘルパーのインスタンス
     */
    protected val itemCreator: HTItemIngredientCreator by lazy { HTIngredientAccess.INSTANCE.itemCreator() }

    /**
     * 液体の完成品を作成するヘルパーのインスタンス
     */
    protected val fluidResult: HTFluidResultCreator = HTFluidResultCreator

    /**
     * アイテムの完成品を作成するヘルパーのインスタンス
     */
    protected val itemResult: HTItemResultCreator = HTItemResultCreator

    /**
     * [HTRecipeProvider.buildRecipes]内で呼び出されるメソッドです。
     */
    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        provider = holderLookup
        this.output = object : RecipeOutput {
            override fun accept(
                id: ResourceLocation,
                recipe: Recipe<*>,
                advancement: AdvancementHolder?,
                vararg conditions: ICondition,
            ) {
                output.accept(modifyId(id), recipe, advancement, *conditions)
            }

            override fun advancement(): Advancement.Builder = output.advancement()
        }.let(::modifyOutput)
        buildRecipeInternal()
    }

    /**
     * 指定した[パス][path]から[ID][ResourceLocation]を作成します。
     * @return [modId]を[名前空間][ResourceLocation.getNamespace]とする[ID][ResourceLocation]
     */
    protected fun id(path: String): ResourceLocation = modId.toId(path)

    /**
     * 指定した[パス][path]から[ID][ResourceLocation]を作成します。
     * @return [modId]を[名前空間][ResourceLocation.getNamespace]とする[ID][ResourceLocation]
     */
    protected fun id(vararg path: String): ResourceLocation = modId.toId(*path)

    /**
     * 指定した[id]を別の[ID][ResourceLocation]に変換します。
     */
    protected abstract fun modifyId(id: ResourceLocation): ResourceLocation

    /**
     * 指定した[output]別の[RecipeOutput]に変換します。
     */
    protected abstract fun modifyOutput(output: RecipeOutput): RecipeOutput

    /**
     * レシピを生成します。
     */
    protected abstract fun buildRecipeInternal()

    //    Direct    //

    /**
     * mod本体で使用される[HTSubRecipeProvider]の拡張クラスです。
     */
    abstract class Direct(modId: String) : HTSubRecipeProvider(modId) {
        final override fun modifyId(id: ResourceLocation): ResourceLocation = modId.toId(id.path)

        final override fun modifyOutput(output: RecipeOutput): RecipeOutput = output
    }

    //    Integration    //

    /**
     * 他Modとの連携レシピ向けの[HTSubRecipeProvider]の拡張クラスです。
     * @param mainModId 本体のmodId
     * @param integrationModId 連携先のmodId
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    abstract class Integration(mainModId: String, private val integrationModId: String) : HTSubRecipeProvider(mainModId) {
        private val builtInIds: Set<String> = HTConst.getBuiltInIdSet(mainModId)

        final override fun modifyId(id: ResourceLocation): ResourceLocation {
            val namespace: String = id.namespace
            return if (namespace in builtInIds) {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], modId, path[1])
            } else {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], namespace, path[1])
            }
        }

        final override fun modifyOutput(output: RecipeOutput): RecipeOutput = output.withConditions(ModLoadedCondition(integrationModId))
    }

    //    Extensions    //

    protected fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }
}
