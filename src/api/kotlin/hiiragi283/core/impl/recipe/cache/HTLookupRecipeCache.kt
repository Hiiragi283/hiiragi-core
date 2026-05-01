package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipePredicate
import hiiragi283.core.api.recipe.cache.HTRecipeCache
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * [HTRecipeLookup]に基づいた[HTRecipeCache]の実装クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @param predicate レシピが一致するかを判定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
class HTLookupRecipeCache<INPUT : RecipeInput, RECIPE : Any>(
    val lookup: HTRecipeLookup<RECIPE>,
    private val predicate: (RECIPE, INPUT, Level) -> Boolean,
) : HTRecipeCache<INPUT, RECIPE> {
    companion object {
        /**
         * 指定した[lookup]から，[HTRecipePredicate.matches]に基づいた[HTLookupRecipeCache]の新しいインスタンスを作成します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE [HTRecipePredicate]を実装したクラス
         */
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : HTRecipePredicate<INPUT>> forRecipe(
            lookup: HTRecipeLookup<RECIPE>,
        ): HTLookupRecipeCache<INPUT, RECIPE> = HTLookupRecipeCache(lookup) { recipe: RECIPE, input: INPUT, _ -> recipe.matches(input) }
    }

    private var lastRecipe: Either<ResourceLocation, HTRecipeHolder<RECIPE>>? = null

    override fun getFirstRecipe(input: INPUT, level: Level): RECIPE? {
        val holder: HTRecipeHolder<RECIPE> =
            lastRecipe
                ?.mapLeft { id: ResourceLocation -> lookup.getAllRecipes(level).firstOrNull { it.id == id } }
                ?.unwrap()
                ?: lookup.findFirst(level) { predicate(it, input, level) } ?: run {
                lastRecipe = null
                HiiragiCoreAPI.LOGGER.debug("Clear cached recipe at lookup {}", this)
                return null
            }
        val (id: ResourceLocation, recipe: RECIPE) = holder
        if (predicate(recipe, input, level)) {
            if (holder != lastRecipe?.getRight()) {
                lastRecipe = Either.Right(holder)
                HiiragiCoreAPI.LOGGER.debug("Updated cached recipe to {} at lookup {}", id, this)
            }
            return recipe
        } else {
            lastRecipe = null
            HiiragiCoreAPI.LOGGER.debug("Clear cached recipe at lookup {}", this)
            return null
        }
    }

    //    HTValueSerializable    //

    private fun getRecipeId(): ResourceLocation? = lastRecipe?.mapRight(HTRecipeHolder<RECIPE>::getId)?.unwrap()

    override fun serialize(output: HTValueOutput) {
        output.write("last_recipe", ResourceLocation.CODEC, getRecipeId())
    }

    override fun deserialize(input: HTValueInput) {
        lastRecipe = input.read("last_recipe", ResourceLocation.CODEC)?.let { Either.Left(it) }
    }

    override fun toString(): String = "HTLookupRecipeCache(lookup=$lookup)"
}
