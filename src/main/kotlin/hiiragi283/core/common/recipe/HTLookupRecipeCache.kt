package hiiragi283.core.common.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * [HTRecipeLookup]に基づいた[HTRecipeCache]の実装クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @param HOLDER [ResourceLocation]と[RECIPE]を束ねたクラス
 * @param predicate レシピが一致するかを判定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
class HTLookupRecipeCache<INPUT : RecipeInput, RECIPE : Any, HOLDER : Any>(
    val lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
    private val predicate: (RECIPE, INPUT, Level) -> Boolean,
) : HTRecipeCache<INPUT, RECIPE> {
    companion object {
        /**
         * 指定した[lookup]から，[Recipe.matches]に基づいた[HTLookupRecipeCache]の新しいインスタンスを作成します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> forManager(
            lookup: HTRecipeLookup.Managed<INPUT, RECIPE>,
        ): HTLookupRecipeCache<INPUT, RECIPE, RecipeHolder<RECIPE>> = HTLookupRecipeCache(lookup, Recipe<INPUT>::matches)

        /**
         * 指定した[lookup]から，[HTRecipe.test]に基づいた[HTLookupRecipeCache]の新しいインスタンスを作成します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE [HTRecipe]を実装したクラス
         * @param HOLDER [ResourceLocation]と[RECIPE]を束ねたクラス
         */
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>, HOLDER : Any> forRecipe(
            lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
        ): HTLookupRecipeCache<INPUT, RECIPE, HOLDER> =
            HTLookupRecipeCache(lookup) { recipe: RECIPE, input: INPUT, _ -> recipe.test(input) }
    }

    private var lastRecipe: HOLDER? = null

    override fun getFirstRecipe(input: INPUT, level: Level): RECIPE? {
        val holder: HOLDER = lastRecipe ?: lookup.findFirst(level) { predicate(it, input, level) } ?: run {
            lastRecipe = null
            HiiragiCoreAPI.LOGGER.debug("Clear cached recipe at lookup {}", this)
            return null
        }
        val recipe: RECIPE = lookup.getRecipe(holder)
        if (predicate(recipe, input, level)) {
            if (holder != lastRecipe) {
                lastRecipe = holder
                HiiragiCoreAPI.LOGGER.debug("Updated cached recipe to {} at lookup {}", lookup.getId(holder), this)
            }
            return recipe
        } else {
            lastRecipe = null
            HiiragiCoreAPI.LOGGER.debug("Clear cached recipe at lookup {}", this)
            return null
        }
    }

    //    HTValueSerializable    //

    override fun serialize(output: HTValueOutput) {
        output.write("last_recipe", ResourceLocation.CODEC, lastRecipe?.let(lookup::getId))
    }

    override fun deserialize(input: HTValueInput) {
        input
            .read("last_recipe", ResourceLocation.CODEC)
            ?.let(lookup::getHolder)
            .let(::lastRecipe::set)
    }

    override fun toString(): String = "HTLookupRecipeCache(lookup=$lookup)"
}
