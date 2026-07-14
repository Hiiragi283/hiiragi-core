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

@JvmRecord
data class HTRecipeSerializer<T : Recipe<*>>(@JvmField val codec: MapCodec<T>, @JvmField val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>) : RecipeSerializer<T> {
    override fun codec(): MapCodec<T> = codec

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
}

//    HTRecipeHolder    //

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias HTRecipeHolder<R> = Pair<ResourceLocation, R>

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <T : Recipe<*>> HTRecipeHolder(holder: RecipeHolder<T>): HTRecipeHolder<T> = holder.id() to holder.value()

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
val <R> HTRecipeHolder<R>.id: ResourceLocation get() = this.first

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
val <R> HTRecipeHolder<R>.recipe: R get() = this.second
