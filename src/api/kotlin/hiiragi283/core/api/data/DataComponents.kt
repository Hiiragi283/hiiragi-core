package hiiragi283.core.api.data

import hiiragi283.core.api.HTBuilderMarker
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentPredicate

/**
 * 新しい[DataComponentMap]のインスタンスを作成します。
 * @param builderAction [DataComponentMap]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@HTBuilderMarker
inline fun buildDataMap(builderAction: DataComponentMap.Builder.() -> Unit): DataComponentMap =
    DataComponentMap.builder().apply(builderAction).build()

/**
 * 新しい[DataComponentPatch]のインスタンスを作成します。
 * @param builderAction [DataComponentPatch]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@HTBuilderMarker
inline fun buildDataPatch(builderAction: DataComponentPatch.Builder.() -> Unit): DataComponentPatch =
    DataComponentPatch.builder().apply(builderAction).build()

/**
 * 新しい[DataComponentPredicate]のインスタンスを作成します。
 * @param builderAction [DataComponentPredicate]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@HTBuilderMarker
inline fun buildDataPredicate(builderAction: DataComponentPredicate.Builder.() -> Unit): DataComponentPredicate =
    DataComponentPredicate.builder().apply(builderAction).build()
