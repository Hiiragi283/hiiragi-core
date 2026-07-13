package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.toId
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * [Registry]で使われる[ResourceKey]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
typealias RegistryKey<T> = ResourceKey<out Registry<T>>

fun <T : Any> RegistryKey<T>.createKey(namespace: String, path: String): ResourceKey<T> = this.createKey(namespace.toId(path))

/**
 * この[レジストリキー][this]に基づいて[ID][id]を[ResourceKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> RegistryKey<T>.createKey(id: ResourceLocation): ResourceKey<T> = ResourceKey.create(this, id)

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <T : Any> ResourceKey<out Registry<out T>>.toRegistryKey(): RegistryKey<T> = ResourceKey.createRegistryKey(this.location())
