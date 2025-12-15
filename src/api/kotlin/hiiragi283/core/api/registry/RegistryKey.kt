package hiiragi283.core.api.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * [Registry]で使われる[ResourceKey]のエイリアス
 */
typealias RegistryKey<T> = ResourceKey<out Registry<T>>

/**
 * [RegistryKey]に基づいて[ResourceLocation]を[ResourceKey]に変換します。
 */
fun <T : Any> RegistryKey<T>.createKey(id: ResourceLocation): ResourceKey<T> = ResourceKey.create(this, id)
