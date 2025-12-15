package hiiragi283.core.api.data.tag

import hiiragi283.core.api.collection.buildMultiMap
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.RegistryKey
import net.minecraft.core.HolderLookup
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.function.Function

/**
 * [HTTagBuilder]に基づいた[TagsProvider]の拡張クラス
 */
abstract class HTTagsProvider<T : Any>(modId: String, registryKey: RegistryKey<T>, context: HTDataGenContext) :
    TagsProvider<T>(
        context.output,
        registryKey,
        context.registries,
        modId,
        context.fileHelper,
    ) {
    companion object {
        @JvmField
        val COMPARATOR: Comparator<TagEntry> = Comparator
            .comparing(TagEntry::isTag, Comparator.reverseOrder())
            .thenComparing(TagEntry::isRequired)
            .thenComparing(TagEntry::getId)
    }

    @Suppress("DEPRECATION")
    final override fun addTags(provider: HolderLookup.Provider) {
        buildMultiMap {
            addTagsInternal { tagKey: TagKey<T> -> HTTagBuilder(registryKey) { this.put(tagKey, it) } }
        }.map.forEach { (tagKey: TagKey<T>, entries: Collection<TagEntry>) ->
            entries
                .sortedWith(COMPARATOR)
                .toSet()
                .forEach { entry: TagEntry -> tag(tagKey).add(entry) }
        }
    }

    /**
     * タグを登録します。
     */
    protected abstract fun addTagsInternal(factory: BuilderFactory<T>)

    protected fun addTags(factory: BuilderFactory<T>, parent: TagKey<T>, child: TagKey<T>): HTTagBuilder<T> {
        factory.apply(parent).addTag(child)
        return factory.apply(child)
    }

    protected fun addMaterial(factory: BuilderFactory<T>, prefix: HTPrefixLike, material: HTMaterialLike): HTTagBuilder<T> =
        addTags(factory, prefix.createCommonTagKey(registryKey), prefix.createTagKey(registryKey, material))

    @Deprecated("Use `addTagsInternal(HolderLookup.Provider, TagKey<T>)` instead")
    override fun tag(tag: TagKey<T>): TagAppender<T> = super.tag(tag)

    //    Factory    //

    fun interface BuilderFactory<T : Any> : Function<TagKey<T>, HTTagBuilder<T>>
}
