package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.tuple.Ior
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

@JvmInline
value class HTHolderOrTagKey<T : Any> private constructor(private val entry: Ior<Holder<T>, TagKey<T>>) : HTIdLike {
    companion object {
        @JvmStatic
        fun <T : Any> codec(registryKey: RegistryKey<T>): MapBiCodec<RegistryFriendlyByteBuf, HTHolderOrTagKey<T>> = MapBiCodecs
            .ior(
                VanillaBiCodecs.holder(registryKey).optionalFieldOf(HTConst.ID),
                VanillaBiCodecs.tagKey(registryKey).optionalFieldOf(HTConst.TAG),
            ).xmap(::HTHolderOrTagKey, HTHolderOrTagKey<T>::entry)
    }

    constructor(holder: Holder<T>) : this(Ior.Left(holder.delegate))

    constructor(tagKey: TagKey<T>, holder: Holder<T>?) : this(
        when (holder) {
            null -> Ior.Right(tagKey)
            else -> Ior.Both(holder.delegate, tagKey)
        },
    )
    
    fun getHolder(provider: HolderLookup.Provider?): HTTextResult<Holder<T>> {
        val provider1: HolderLookup.Provider = (provider ?: HiiragiCoreAPI.getActiveAccess())
            ?: return HTTextResult.failure(HTCommonTranslation.MISSING_SERVER)
        return entry.getRight()
            ?.let(provider1::holderSetOrNull)
            ?.firstOrNull()
            ?.let(HTTextResult.Companion::success)
            ?: entry.getLeft()
                ?.let(HTTextResult.Companion::success)
                ?: HTTextResult.failure(HTCommonTranslation.EMPTY_TAG_KEY)
    }
    
    override fun getId(): ResourceLocation = entry.map(
        Holder<T>::toLike.andThen(HTHolderLike<*, *>::getId),
        TagKey<T>::location,
    )
}
