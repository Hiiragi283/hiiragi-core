package hiiragi283.core.api.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * [Registry]で使われる[ResourceKey]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
typealias RegistryKey<T> = ResourceKey<out Registry<T>>

/**
 * この[レジストリキー][this]と[ID][id]を[ResourceKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> RegistryKey<T>.createKey(id: ResourceLocation): ResourceKey<T> = ResourceKey.create(this, id)
