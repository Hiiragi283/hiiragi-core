package hiiragi283.core.api.recipe.result

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * [HTItemResult.Entry]の種類を表すクラスです。
 * @param codec JSONへの読み書きに使用されるコーデック
 * @param streamCodec パケットへの読み書きに使用されるコーデック
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
@JvmRecord
data class HTItemResultType<E : HTItemResult.Entry>(val codec: MapCodec<E>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, E>) {
    constructor(codec: MapCodec<E>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
}
