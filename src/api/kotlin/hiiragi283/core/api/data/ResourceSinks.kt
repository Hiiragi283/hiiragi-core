package hiiragi283.core.api.data

import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import hiiragi283.core.api.registry.RegistryKey
import net.mehvahdjukaar.moonlight.api.resources.ResType
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * この[ResourceSink][this]に対して，[codec]に基づいて[value]を保存します。
 * @param T [value]のクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun <T : Any> ResourceSink.addJson(id: ResourceLocation, codec: Codec<T>, value: T): DataResult<JsonElement> = codec.encodeStart(JsonOps.INSTANCE, value).ifSuccess { this.addJson(id, it, ResType.JSON) }

/**
 * この[ResourceSink][this]に対して，[codec]に基づいて[value]を保存します。
 * @param T [value]のクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun <T : Any> ResourceSink.register(key: ResourceKey<T>, codec: Codec<T>, value: T): DataResult<JsonElement> {
    val registryKey: RegistryKey<T> = key.registryKey()
    return addJson(key.location().withPrefix(Registries.elementsDirPath(registryKey) + "/"), codec, value)
}
