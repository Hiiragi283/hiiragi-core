package hiiragi283.lib.registry

import com.mojang.serialization.MapCodec
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

/**
 * [RecipeSerializer]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredRecipeSerializerRegister(namespace: String) : HTDeferredRegister<RecipeSerializer<*>>(Registries.RECIPE_SERIALIZER, namespace) {
    /**
     * 新しい[RecipeSerializer]を登録します。
     * @param SERIALIZER シリアライザのクラス
     * @param name [RecipeSerializer]のIDのパス
     * @return 登録された[RecipeSerializer]のインスタンス
     */
    fun <SERIALIZER : RecipeSerializer<*>> registerSerializer(name: String, serializer: SERIALIZER): SERIALIZER {
        this.register(name) { _ -> serializer }
        return serializer
    }

    /**
     * 新しい[RecipeSerializer]を登録します。
     * @param RECIPE レシピのクラス
     * @param name [RecipeSerializer]のIDのパス
     * @param codec JSONとの読み書きに使用されるコーデック
     * @param streamCodec クライアントの同期に使用されるコーデック
     * @return 登録された[RecipeSerializer]のインスタンス
     */
    fun <RECIPE : Recipe<*>> registerSerializer(
        name: String,
        codec: MapCodec<RECIPE>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, RECIPE> = ByteBufCodecs.fromCodecWithRegistries(codec.codec()),
    ): RecipeSerializer<RECIPE> = registerSerializer(name, RecipeSerializer(codec, streamCodec))
}
