package hiiragi283.core.api.recipe

import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * レシピの[ID][ResourceLocation]とレシピ自身をまとめたクラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
@JvmRecord
data class HTRecipeHolder<RECIPE : Any>(
    @JvmField val id: ResourceLocation,
    @JvmField val recipe: RECIPE,
) : HTIdLike {
    companion object {
        /**
         * バニラの[RecipeHolder]を[HTRecipeHolder]に変換します。
         */
        @JvmStatic
        fun <RECIPE : Recipe<*>> from(holder: RecipeHolder<RECIPE>): HTRecipeHolder<RECIPE> = HTRecipeHolder(holder.id(), holder.value())
    }

    constructor(pair: Pair<ResourceLocation, RECIPE>) : this(pair.first, pair.second)

    constructor(entry: Map.Entry<ResourceLocation, RECIPE>) : this(entry.key, entry.value)

    /**
     * レシピの値を変換し，新しいインスタンスを作成します。
     * @param R 変換後のクラス
     * @param transform [recipe]を[R]に変換するブロック
     */
    inline fun <R : Any> mapRecipe(transform: (RECIPE) -> R): HTRecipeHolder<R> = HTRecipeHolder(this.id, transform(this.recipe))

    override fun getId(): ResourceLocation = id
}

/**
 * 現在の[HTRecipeHolder][this]をバニラの[RecipeHolder]に変換します。
 * @param RECIPE [Recipe]を継承したクラス
 */
fun <RECIPE : Recipe<*>> HTRecipeHolder<RECIPE>.toVanilla(): RecipeHolder<RECIPE> = RecipeHolder(this.id, this.recipe)
