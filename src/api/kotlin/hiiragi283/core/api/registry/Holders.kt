package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder

//    Holder    //

/**
 * [Holder]から[ResourceLocation]を返します。
 * @throws [Holder.unwrapKey]が空の場合
 */
val <T : Any> Holder<T>.idOrThrow: ResourceLocation get() = when (this) {
    is HTIdLike -> this.getId()
    is DeferredHolder<*, *> -> this.id
    else -> unwrapKey().orElseThrow().location()
}

@Suppress("UNCHECKED_CAST")
val <T : Any> Holder<T>.keyOrThrow: ResourceKey<T> get() = (this as? HTKeyLike<T>)?.getResourceKey() ?: unwrapKey().orElseThrow()
