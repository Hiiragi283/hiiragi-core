package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toId
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition

sealed class HTSubRecipeProvider(protected val modId: String) {
    protected lateinit var provider: HolderLookup.Provider
        private set
    protected lateinit var output: RecipeOutput
        private set

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

    protected fun id(path: String): ResourceLocation = modId.toId(path)

    /**
     * 指定された[ResourceLocation]を改変します。
     */
    protected abstract fun modifyId(id: ResourceLocation): ResourceLocation

    /**
     * 指定された[RecipeOutput]を改変します。
     */
    protected abstract fun modifyOutput(output: RecipeOutput): RecipeOutput

    /**
     * レシピを生成します。
     */
    protected abstract fun buildRecipeInternal()

    //    Direct    //

    /**
     * Ragium単体で使用するレシピ向けの拡張クラス
     */
    abstract class Direct(modId: String) : HTSubRecipeProvider(modId) {
        override fun modifyId(id: ResourceLocation): ResourceLocation = modId.toId(id.path)

        override fun modifyOutput(output: RecipeOutput): RecipeOutput = output
    }

    //    Integration    //

    /**
     * 他Modとの連携レシピ向けの拡張クラス
     */
    abstract class Integration(modId: String) : HTSubRecipeProvider(modId) {
        override fun modifyId(id: ResourceLocation): ResourceLocation {
            val namespace: String = id.namespace
            return if (namespace in HTConst.BUILTIN_IDS) {
                val path: List<String> = id.path.split("/", limit = 2)
                HiiragiCoreAPI.id(path[0], modId, path[1])
            } else {
                val path: List<String> = id.path.split("/", limit = 2)
                HiiragiCoreAPI.id(path[0], namespace, path[1])
            }
        }

        override fun modifyOutput(output: RecipeOutput): RecipeOutput = output.withConditions(ModLoadedCondition(modId))
    }

    //    Extensions    //

    protected fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }
}
