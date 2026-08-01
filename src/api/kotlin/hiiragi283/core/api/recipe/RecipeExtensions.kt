package hiiragi283.core.api.recipe

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeSerializer

//    RecipeSerializer    //

/**
 * 新しい[RecipeSerializer]のインスタンスを作成します。
 * @param codec セーブとロードで使用されるコーデック
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <T : Recipe<*>> RecipeSerializer(codec: MapCodec<T>, streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = ByteBufCodecs.fromCodecWithRegistries(codec.codec())): RecipeSerializer<T> = HTRecipeSerializer(codec, streamCodec)

fun <T : Recipe<*>> RecipeSerializer(recipe: T): RecipeSerializer<T> = HTRecipeSerializer(MapCodec.unit(recipe), StreamCodec.unit(recipe))

/**
 * @suppress
 */
@JvmRecord
private data class HTRecipeSerializer<T : Recipe<*>>(@JvmField val codec: MapCodec<T>, @JvmField val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>) : RecipeSerializer<T> {
    override fun codec(): MapCodec<T> = codec

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
}

//    HTRecipeHolder    //

/**
 * [Recipe]以外も受け付ける[RecipeHolder]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias HTRecipeHolder<R> = Pair<ResourceLocation, R>

/**
 * [RecipeHolder]を[HTRecipeHolder]に変換します。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <T : Recipe<*>> HTRecipeHolder(holder: RecipeHolder<T>): HTRecipeHolder<T> = holder.id() to holder.value()

/**
 * レシピの[ID][ResourceLocation]を取得します。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
val <R> HTRecipeHolder<R>.id: ResourceLocation get() = this.first

/**
 * レシピの値を取得します。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
val <R> HTRecipeHolder<R>.recipe: R get() = this.second

/**
 * [HTRecipeHolder]が保持するレシピの型を変換します。
 * @param T 変換前のレシピのクラス
 * @param U 変換後のレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
inline fun <T, reified U> HTRecipeHolder<T>.castRecipe(): HTRecipeHolder<U>? {
    val (id: ResourceLocation, recipe: T) = this
    return when (recipe) {
        is U -> id to recipe
        else -> null
    }
}
