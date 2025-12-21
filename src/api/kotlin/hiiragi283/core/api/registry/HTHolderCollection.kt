package hiiragi283.core.api.registry

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.monad.unwrapEither
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

sealed interface HTHolderCollection<T : Any> : Collection<Holder<T>> {
    companion object {
        @JvmStatic
        fun <T : Any> codec(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, HTHolderCollection<T>> = BiCodecs
            .either(Direct.codec(registryKey), Reference.codec(registryKey))
            .xmap(::unwrapEither) { collection: HTHolderCollection<T> ->
                when (collection) {
                    is Direct<T> -> Either.left(collection)
                    is Reference<T> -> Either.right(collection)
                }
            }

        @JvmStatic
        fun <T : Any> direct(vararg holders: Holder<T>): HTHolderCollection<T> = direct(HolderSet.direct(Holder<T>::getDelegate, *holders))

        @JvmStatic
        fun <T : Any> direct(holders: List<Holder<T>>): HTHolderCollection<T> = direct(HolderSet.direct(Holder<T>::getDelegate, holders))

        @JvmStatic
        fun <T : Any> direct(holderSet: HolderSet<T>): HTHolderCollection<T> = Direct(holderSet)

        @JvmStatic
        fun <T : Any> reference(tagKey: TagKey<T>): HTHolderCollection<T> = Reference(Either.left(tagKey))

        @JvmStatic
        fun <T : Any> reference(vararg keys: ResourceKey<T>): HTHolderCollection<T> = reference(keys.toList())

        @JvmStatic
        fun <T : Any> reference(keys: List<ResourceKey<T>>): HTHolderCollection<T> = Reference(Either.right(keys))
    }

    override fun containsAll(elements: Collection<Holder<T>>): Boolean = elements.all { contains(it) }

    fun unwrap(): Either<TagKey<T>, List<ResourceKey<T>>>

    @JvmInline
    private value class Direct<T : Any>(private val holderSet: HolderSet<T>) : HTHolderCollection<T> {
        companion object {
            @JvmStatic
            fun <T : Any> codec(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, Direct<T>> =
                VanillaBiCodecs.holderSet(registryKey).xmap(::Direct, Direct<T>::holderSet)
        }

        override val size: Int get() = holderSet.size()

        override fun isEmpty(): Boolean = holderSet.size() == 0

        override fun contains(element: Holder<T>): Boolean = holderSet.contains(element)

        override fun iterator(): Iterator<Holder<T>> = holderSet.iterator()

        override fun unwrap(): Either<TagKey<T>, List<ResourceKey<T>>> = holderSet.unwrap().mapRight { holders: List<Holder<T>> ->
            holders.map(Holder<T>::toLike).map(HTHolderLike<T, *>::getResourceKey)
        }
    }

    @JvmInline
    private value class Reference<T : Any>(private val either: Either<TagKey<T>, List<ResourceKey<T>>>) : HTHolderCollection<T> {
        companion object {
            @JvmStatic
            fun <T : Any> codec(registryKey: RegistryKey<T>): BiCodec<ByteBuf, Reference<T>> = BiCodecs
                .either(
                    VanillaBiCodecs.tagKey(registryKey, true),
                    VanillaBiCodecs.resourceKey(registryKey).listOrElement(),
                ).xmap(::Reference, Reference<T>::either)
        }

        private fun getDelegate(): Iterable<Holder<T>> = either.map(
            { tagKey: TagKey<T> -> HiiragiCoreAPI.getActiveAccess()?.holderSetOrNull(tagKey) },
            { keys: List<ResourceKey<T>> -> keys.mapNotNull { HiiragiCoreAPI.getActiveAccess()?.holderOrNull(it) } },
        ) ?: listOf()

        override val size: Int
            get() = getDelegate().count()

        override fun isEmpty(): Boolean = getDelegate().none()

        override fun contains(element: Holder<T>): Boolean = getDelegate().contains(element)

        override fun iterator(): Iterator<Holder<T>> = getDelegate().iterator()

        override fun unwrap(): Either<TagKey<T>, List<ResourceKey<T>>> = either
    }
}
