package hiiragi283.core.api.registry

import com.mojang.serialization.Codec
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Unit as MCUnit

/**
 * @see DataComponents
 */
class HTDeferredDataComponentRegister(registryKey: RegistryKey<DataComponentType<*>>, namespace: String) :
    HTDeferredRegister<DataComponentType<*>>(
        registryKey,
        namespace,
    ) {
    fun <DATA : Any> registerType(name: String, builderAction: (DataComponentType.Builder<DATA>) -> Unit): DataComponentType<DATA> {
        val type: DataComponentType<DATA> = DataComponentType
            .builder<DATA>()
            .apply(builderAction)
            .build()
        this.register(name) { _ -> type }
        return type
    }

    fun <DATA : Any> registerType(
        name: String,
        codec: Codec<DATA>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, DATA>?,
    ): DataComponentType<DATA> = registerType(name) { builder: DataComponentType.Builder<DATA> ->
        builder.persistent(codec)
        streamCodec?.let(builder::networkSynchronized)
    }

    fun registerFlag(name: String): DataComponentType<MCUnit> = registerType(name, Codec.unit(MCUnit.INSTANCE), StreamCodec.unit(MCUnit.INSTANCE))
}
