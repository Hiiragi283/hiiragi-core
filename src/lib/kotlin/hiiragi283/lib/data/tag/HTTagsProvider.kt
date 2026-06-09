package hiiragi283.lib.data.tag

import hiiragi283.lib.collection.SetMultiMap
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

abstract class HTTagsProvider<T : Any> : TagsProvider<T> {
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

    private val entryCache = SetMultiMap.Builder<TagKey<T>, TagEntry>()

    final override fun addTags(registries: HolderLookup.Provider) {
        appendTags(registries)

        entryCache.build().asMap().forEach { (tagKey: TagKey<T>, entries: Collection<TagEntry>) ->
            entries
                .sortedWith(COMPARATOR)
                .distinctBy(TagEntry::toString)
                .forEach { entry: TagEntry -> getOrCreateRawBuilder(tagKey).add(entry) }
        }
    }

    protected abstract fun appendTags(registries: HolderLookup.Provider)

    protected fun tag(tagKey: TagKey<T>): HTTagBuilder<T> = HTTagBuilder { entry: TagEntry -> entryCache.put(tagKey, entry) }

    protected fun tags(prefix: HTTagPrefix, material: HTMaterialKey): HTTagBuilder<T> = tags(prefix.rawCommonTag.create(registryKey), prefix.createTagKey(registryKey, material))

    protected fun tags(tagKey: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> = children.fold(tag(tagKey)) { builder: HTTagBuilder<T>, tagKeyIn: TagKey<T> ->
        builder.addTag(tagKeyIn)
        tag(tagKeyIn)
    }
}
