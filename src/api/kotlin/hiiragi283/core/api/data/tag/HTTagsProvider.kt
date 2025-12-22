package hiiragi283.core.api.data.tag

import hiiragi283.core.api.collection.buildMultiMap
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.RegistryKey
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import java.util.function.Function

/**
 * [HTTagBuilder]を使用する[TagsProvider]の拡張クラスです。
 * @param T レジストリの要素のクラス
 * @param registryKey レジストリを表すキー
 */
abstract class HTTagsProvider<T : Any>(
    output: PackOutput,
    registryKey: RegistryKey<T>,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    modId: String,
    existingFileHelper: ExistingFileHelper?,
) : TagsProvider<T>(output, registryKey, lookupProvider, modId, existingFileHelper) {
    constructor(modId: String, registryKey: RegistryKey<T>, context: HTDataGenContext) : this(
        context.output,
        registryKey,
        context.registries,
        modId,
        context.fileHelper,
    )

    companion object {
        /**
         * タグの生成時に使用されるソーター
         */
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
                .distinctBy(TagEntry::toString)
                .forEach { entry: TagEntry -> tag(tagKey).add(entry) }
        }
    }

    /**
     * 生成するタグを登録します。
     * @param factory [TagKey]から[HTTagBuilder]を取得するブロック
     */
    protected abstract fun addTagsInternal(factory: BuilderFactory<T>)

    /**
     * タグをチェインして登録します。
     * @return 最後の[children]に対する[HTTagBuilder]
     */
    protected fun addTags(factory: BuilderFactory<T>, parent: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> {
        check(children.size > 1) { "Empty tag key children" }
        return children.fold(factory.apply(parent)) { current: HTTagBuilder<T>, child: TagKey<T> ->
            current.addTag(child)
            factory.apply(child)
        }
    }

    /**
     * タグをチェインして登録します。
     * @return [HTPrefixLike.createTagKey]に対する[HTTagBuilder]
     */
    protected fun addMaterial(factory: BuilderFactory<T>, prefix: HTPrefixLike, material: HTMaterialLike): HTTagBuilder<T> =
        addTags(factory, prefix.createCommonTagKey(registryKey), prefix.createTagKey(registryKey, material))

    @Deprecated("Use `addTagsInternal(HolderLookup.Provider, TagKey<T>)` instead")
    override fun tag(tag: TagKey<T>): TagAppender<T> = super.tag(tag)

    //    Factory    //

    fun interface BuilderFactory<T : Any> : Function<TagKey<T>, HTTagBuilder<T>>
}
