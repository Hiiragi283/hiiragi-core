package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.resource.HTIdLike
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagAppender
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

abstract class HTIdLikeTagsProvider<T : Any> : TagsProvider<T> {
    constructor(output: PackOutput, registryKey: RegistryKey<T>, lookupProvider: CompletableFuture<HolderLookup.Provider>, parentProvider: CompletableFuture<TagLookup<T>>, modId: String) : super(output, registryKey, lookupProvider, parentProvider, modId)

    constructor(output: PackOutput, registryKey: RegistryKey<T>, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : super(output, registryKey, lookupProvider, modId)

    protected fun tag(tagKey: TagKey<T>): IdAppender<T> = IdAppender(getOrCreateRawBuilder(tagKey))

    protected fun tags(tagKey: TagKey<T>, vararg children: TagKey<T>): IdAppender<T> = children.fold(tag(tagKey)) { appender: IdAppender<T>, tagKeyIn: TagKey<T> ->
        appender.addTag(tagKeyIn)
        tag(tagKeyIn)
    }

    protected class IdAppender<T : Any>(val builder: TagBuilder) : TagAppender<HTIdLike, T> {
        override fun add(element: HTIdLike): IdAppender<T> = apply { builder.addElement(element.getId()) }

        override fun addOptional(element: HTIdLike): IdAppender<T> = apply { builder.addOptionalElement(element.getId()) }

        override fun addTag(tag: TagKey<T>): IdAppender<T> = apply { builder.addTag(tag.location()) }

        override fun addOptionalTag(tag: TagKey<T>): IdAppender<T> = apply { builder.addOptionalTag(tag.location()) }

        override fun add(entry: TagEntry): IdAppender<T> = apply { builder.add(entry) }

        override fun replace(value: Boolean): IdAppender<T> = apply { builder.replace() }

        override fun remove(element: HTIdLike): IdAppender<T> = apply { builder.removeElement(element.getId()) }

        override fun remove(tag: TagKey<T>): IdAppender<T> = apply { builder.removeTag(tag.location()) }
    }
}
