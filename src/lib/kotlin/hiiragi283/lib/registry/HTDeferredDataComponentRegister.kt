package hiiragi283.lib.registry

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Unit as MCUnit

/**
 * [DataComponentType]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredDataComponentRegister(registryKey: RegistryKey<DataComponentType<*>>, namespace: String) : HTDeferredRegister<DataComponentType<*>>(registryKey, namespace) {
    /**
     * 新しい[DataComponentType]を登録します。
     * @param DATA コンポーネントの値のクラス
     * @param name [DataComponentType]のパスのID
     * @param builderAction [DataComponentType.Builder]を初期化するブロック
     * @return 登録された[DataComponentType]のインスタンス
     */
    fun <DATA : Any> registerType(name: String, builderAction: (DataComponentType.Builder<DATA>) -> Unit): DataComponentType<DATA> {
        val type: DataComponentType<DATA> = DataComponentType
            .builder<DATA>()
            .apply(builderAction)
            .build()
        this.register(name) { _ -> type }
        return type
    }

    /**
     * 新しい[DataComponentType]を登録します。
     * @param DATA コンポーネントの値のクラス
     * @param name [DataComponentType]のパスのID
     * @param codec JSONとの読み書きに使用されるコーデック
     * @param streamCodec クライアントの同期に使用されるコーデック
     * @return 登録された[DataComponentType]のインスタンス
     */
    fun <DATA : Any> registerType(
        name: String,
        codec: Codec<DATA>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, DATA>?,
    ): DataComponentType<DATA> = registerType(name) { builder: DataComponentType.Builder<DATA> ->
        builder.persistent(codec)
        streamCodec?.let(builder::networkSynchronized)
    }

    /**
     * [MCUnit]向けの[DataComponentType]を登録します。
     * @param name [DataComponentType]のパスのID
     * @return 登録された[DataComponentType]のインスタンス
     */
    fun registerFlag(name: String): DataComponentType<MCUnit> = registerType(name, MapCodec.unitCodec(MCUnit.INSTANCE), StreamCodec.unit(MCUnit.INSTANCE))
}
