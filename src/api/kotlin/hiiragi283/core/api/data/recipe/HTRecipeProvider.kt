package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.data.HTServerResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * [レシピ][Recipe]を生成する[HTServerResourceGenTask]の抽象クラスです。。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
abstract class HTRecipeProvider :
    HTRecipeProviderContext(),
    HTServerResourceGenTask {
    @Deprecated("Do not use", level = DeprecationLevel.ERROR)
    final override val provider: HolderLookup.Provider get() = error("Cannot access registry from runtime-datapack")
    final override lateinit var output: RecipeOutput
        private set

    override fun accept(sink: ResourceSink) {
        this.output = SinkRecipeOutput(sink)
        buildRecipes()
    }

    protected abstract fun buildRecipes()

    private class SinkRecipeOutput(private val sink: ResourceSink) : RecipeOutput {
        override fun accept(
            id: ResourceLocation,
            recipe: Recipe<*>,
            advancement: AdvancementHolder?,
            vararg conditions: ICondition,
        ) {
            sink.addRecipe(recipe, id)
        }

        override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
    }
}
