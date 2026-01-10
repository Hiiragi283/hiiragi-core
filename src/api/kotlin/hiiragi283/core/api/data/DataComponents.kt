package hiiragi283.core.api.data

import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentPredicate

inline fun buildDataMap(builderAction: DataComponentMap.Builder.() -> Unit): DataComponentMap =
    DataComponentMap.builder().apply(builderAction).build()

inline fun buildDataPatch(builderAction: DataComponentPatch.Builder.() -> Unit): DataComponentPatch =
    DataComponentPatch.builder().apply(builderAction).build()

inline fun buildDataPredicate(builderAction: DataComponentPredicate.Builder.() -> Unit): DataComponentPredicate =
    DataComponentPredicate.builder().apply(builderAction).build()
