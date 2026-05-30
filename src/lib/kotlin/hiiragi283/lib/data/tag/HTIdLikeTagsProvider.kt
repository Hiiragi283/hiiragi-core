package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.resource.HTIdLike
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagAppender
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

abstract class HTIdLikeTagsProvider<T : Any> : TagsProvider<T> {
    companion object {
        /**
         * タグの生成時に使用されるソーター
         */
        @JvmField
        val COMPARATOR: Comparator<TagEntry> = Comparator
            .comparing(TagEntry::isRequired)
            .thenComparing(TagEntry::isTag, Comparator.reverseOrder())
            .thenComparing(TagEntry::getId)
    }

    constructor(output: PackOutput, registryKey: RegistryKey<T>, lookupProvider: CompletableFuture<HolderLookup.Provider>, parentProvider: CompletableFuture<TagLookup<T>>, modId: String) : super(output, registryKey, lookupProvider, parentProvider, modId)

    constructor(output: PackOutput, registryKey: RegistryKey<T>, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : super(output, registryKey, lookupProvider, modId)

    private val entryCache: MutableMap<TagKey<T>, MutableList<TagEntry>> = hashMapOf()

    final override fun addTags(registries: HolderLookup.Provider) {
        appendTags(registries)

        entryCache.forEach { (tagKey: TagKey<T>, entries: List<TagEntry>) ->
            entries
                .sortedWith(COMPARATOR)
                .distinctBy(TagEntry::toString)
                .forEach { entry: TagEntry -> getOrCreateRawBuilder(tagKey).add(entry) }
        }
    }

    protected abstract fun appendTags(registries: HolderLookup.Provider)

    protected fun tag(tagKey: TagKey<T>): IdAppender = IdAppender { entry: TagEntry -> entryCache.computeIfAbsent(tagKey) { mutableListOf() }.add(entry) }

    protected fun tags(tagKey: TagKey<T>, vararg children: TagKey<T>): IdAppender = children.fold(tag(tagKey)) { appender: IdAppender, tagKeyIn: TagKey<T> ->
        appender.addTag(tagKeyIn)
        tag(tagKeyIn)
    }

    protected inner class IdAppender(private val consumer: (TagEntry) -> Unit) : TagAppender<HTIdLike, T> {
        override fun add(element: HTIdLike): IdAppender = add(TagEntry.element(element.getId()))

        override fun addOptional(element: HTIdLike): IdAppender = add(TagEntry.optionalElement(element.getId()))

        override fun addTag(tag: TagKey<T>): IdAppender = add(TagEntry.tag(tag.location()))

        override fun addOptionalTag(tag: TagKey<T>): IdAppender = add(TagEntry.optionalTag(tag.location()))

        override fun add(entry: TagEntry): IdAppender = apply { consumer(entry) }

        @Deprecated("Not implemented", level = DeprecationLevel.ERROR)
        override fun replace(value: Boolean): IdAppender = this

        @Deprecated("Not implemented", level = DeprecationLevel.ERROR)
        override fun remove(element: HTIdLike): IdAppender = this

        @Deprecated("Not implemented", level = DeprecationLevel.ERROR)
        override fun remove(tag: TagKey<T>): IdAppender = this
    }
}
