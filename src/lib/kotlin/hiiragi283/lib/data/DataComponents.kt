@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.component.DataComponentExactPredicate
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType

/**
 * 新しい[DataComponentMap]のインスタンスを作成します。
 * @param builderAction [DataComponentMap]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
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
 * @since 26.1.0
 */
inline fun buildDataPatch(builderAction: DataComponentPatch.Builder.() -> Unit): DataComponentPatch {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return DataComponentPatch.builder().apply(builderAction).build()
}

fun <T : Any> buildDataPatch(type: DataComponentType<T>, value: T): DataComponentPatch = buildDataPatch { set(type, value) }

/**
 * 新しい[DataComponentExactPredicate]のインスタンスを作成します。
 * @param builderAction [DataComponentExactPredicate]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun buildDataPredicate(builderAction: DataComponentExactPredicate.Builder.() -> Unit): DataComponentExactPredicate {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }

    return DataComponentExactPredicate.builder().apply(builderAction).build()
}
