package hiiragi283.core.api.item.component

import com.mojang.serialization.Codec
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent

@ConsistentCopyVisibility
@JvmRecord
data class HTItemSoundEvent private constructor(val holder: Holder<SoundEvent>) {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemSoundEvent> = HTCodecs.holder(Registries.SOUND_EVENT).xmap(::HTItemSoundEvent, HTItemSoundEvent::holder)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemSoundEvent> = HTStreamCodecs.holder(Registries.SOUND_EVENT).map(::HTItemSoundEvent, HTItemSoundEvent::holder)

        @JvmStatic
        fun create(sound: SoundEvent): HTItemSoundEvent = create(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound))

        @JvmStatic
        fun create(holder: Holder<SoundEvent>): HTItemSoundEvent = HTItemSoundEvent(holder.delegate)
    }

    val sound: SoundEvent get() = holder.value()
}
