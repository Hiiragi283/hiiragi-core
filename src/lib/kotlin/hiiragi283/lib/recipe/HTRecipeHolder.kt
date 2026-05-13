package hiiragi283.lib.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * レシピの[ID][RecipeKey]とレシピ自身をまとめたクラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
@JvmRecord
data class HTRecipeHolder<RECIPE : Any>(val key: RecipeKey, val recipe: RECIPE) : HTIdLike {
    companion object {
        /**
         * [HTRecipeHolder]の[Codec]を作成します。
         * @since 0.15.1
         */
        @JvmStatic
        fun <RECIPE : Any> codec(recipeCodec: MapCodec<RECIPE>): Codec<HTRecipeHolder<RECIPE>> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTCodecs.resourceKey(Registries.RECIPE).fieldOf(HTConstants.ID).forGetter(HTRecipeHolder<RECIPE>::key),
                    recipeCodec.codec().fieldOf("recipe").forGetter(HTRecipeHolder<RECIPE>::recipe),
                ).apply(instance, ::HTRecipeHolder)
        }

        /**
         * バニラの[RecipeHolder]を[HTRecipeHolder]に変換します。
         */
        @JvmStatic
        fun <RECIPE : Recipe<*>> from(holder: RecipeHolder<out RECIPE>): HTRecipeHolder<RECIPE> = HTRecipeHolder(holder.id(), holder.value())
    }

    constructor(pair: Pair<RecipeKey, RECIPE>) : this(pair.first, pair.second)

    constructor(entry: Map.Entry<RecipeKey, RECIPE>) : this(entry.key, entry.value)

    /**
     * レシピの値を変換し，新しいインスタンスを作成します。
     * @param R 変換後のクラス
     * @param transform [recipe]を[R]に変換するブロック
     */
    inline fun <R : Any> mapRecipe(transform: (RECIPE) -> R): HTRecipeHolder<R> = HTRecipeHolder(this.key, transform(this.recipe))

    /**
     * レシピの値を変換し，新しいインスタンスを作成します。
     * @param R 変換後のクラス
     * @param transform [recipe]を[R]に変換するブロック
     * @return [transform]で変換した値が`null`の場合は`null`
     * @since 0.15.1
     */
    inline fun <R : Any> mapRecipeOrNull(transform: (RECIPE) -> R?): HTRecipeHolder<R>? {
        val recipe: R = transform(this.recipe) ?: return null
        return HTRecipeHolder(this.key, recipe)
    }

    inline fun <reified R : RECIPE> castRecipe(): HTRecipeHolder<R>? = mapRecipeOrNull { it as? R }

    override fun getId(): Identifier = key.identifier()
}

/**
 * 現在の[HTRecipeHolder][this]をバニラの[RecipeHolder]に変換します。
 * @param RECIPE [Recipe]を継承したクラス
 */
fun <RECIPE : Recipe<*>> HTRecipeHolder<RECIPE>.toVanilla(): RecipeHolder<RECIPE> = RecipeHolder(this.key, this.recipe)
