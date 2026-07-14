@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data

import com.mojang.serialization.Codec
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentPredicate
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * 新しい[DataComponentType]のインスタンスを作成します。
 * @param codec セーブとロードで使用されるコーデック
 * @param streamCodec クライアント側との同期に使用されるコーデック
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <T : Any> DataComponentType(codec: Codec<T>, streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>?): DataComponentType<T> {
    val builder: DataComponentType.Builder<T> = DataComponentType.builder<T>().persistent(codec)
    if (streamCodec != null) builder.networkSynchronized(streamCodec)
    return builder.build()
}

/**
 * 新しい[DataComponentMap]のインスタンスを作成します。
 * @param builderAction [DataComponentMap]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
inline fun buildDataMap(builderAction: DataComponentMap.Builder.() -> Unit): DataComponentMap {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return DataComponentMap.builder().apply(builderAction).build()
}

/**
 * 新しい[DataComponentPatch]のインスタンスを作成します。
 * @param builderAction [DataComponentPatch]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
inline fun buildDataPatch(builderAction: DataComponentPatch.Builder.() -> Unit): DataComponentPatch {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return DataComponentPatch.builder().apply(builderAction).build()
}

/**
 * 新しい[DataComponentPredicate]のインスタンスを作成します。
 * @param builderAction [DataComponentPredicate]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
inline fun buildDataPredicate(builderAction: DataComponentPredicate.Builder.() -> Unit): DataComponentPredicate {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return DataComponentPredicate.builder().apply(builderAction).build()
}
