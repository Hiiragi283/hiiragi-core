package hiiragi283.core.api.data.tag

import hiiragi283.core.api.data.HTServerResourceGenTask
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import java.util.function.Function

/**
 * [HTTagBuilder]に基づいてタグを生成するインターフェースです。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see GenTask
 * @see DataGen
 */
sealed interface HTTagsProvider<T : Any> {
    /**
     * 生成するタグを登録します。
     * @param factory [TagKey]から[HTTagBuilder]を取得するブロック
     */
    fun addTagsInternal(factory: BuilderFactory<T>)

    //    Factory    //

    abstract class BuilderFactory<T : Any>(private val registryKey: RegistryKey<T>) : Function<TagKey<T>, HTTagBuilder<T>> {
        abstract override fun apply(tagKey: TagKey<T>): HTTagBuilder<T>

        fun apply(rawTagKey: RawTagKey): HTTagBuilder<T> = apply(rawTagKey.create(registryKey))

        //    Extensions    //

        /**
         * タグをチェインして登録します。
         * @return 最後の[children]に対する[HTTagBuilder]
         */
        fun addTags(parent: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> {
            check(!children.isEmpty()) { "Empty tag key children" }
            return children.fold(this.apply(parent)) { current: HTTagBuilder<T>, child: TagKey<T> ->
                current.addTag(child)
                this.apply(child)
            }
        }

        /**
         * タグをチェインして登録します。
         * @return [HTTagPrefix.materialTag]に対する[HTTagBuilder]
         */
        fun addMaterial(prefix: HTTagPrefix, material: HTMaterialLike): HTTagBuilder<T> {
            val materialTag: RawTagKey = prefix.materialTag(material)
            this.apply(prefix.rawCommonTag).addTag(materialTag)
            return this.apply(materialTag)
        }

        /**
         * 指定した[rawTagKey]から[タグ][TagKey]を作成します。
         * @since 0.15.3
         */
        fun tag(rawTagKey: RawTagKey): TagKey<T> = rawTagKey.create(registryKey)
    }

    //    GenTask    //

    /**
     * [HTTagsProvider]に基づいて[HTServerResourceGenTask]を実装した抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class GenTask<T : Any>(private val registryKey: RegistryKey<T>) :
        HTTagsProvider<T>,
        HTServerResourceGenTask {
        private val builderCache: MutableMap<TagKey<T>, SimpleTagBuilder> = hashMapOf()

        final override fun accept(sink: ResourceSink) {
            // タグの値を一時的に保存
            addTagsInternal(object : BuilderFactory<T>(registryKey) {
                override fun apply(tagKey: TagKey<T>): HTTagBuilder<T> = HTTagBuilder { entry: TagEntry ->
                    builderCache
                        .computeIfAbsent(tagKey) { SimpleTagBuilder.of(tagKey) }
                        .add(entry)
                }
            })
            // タグの値を実際に登録
            for (builder: SimpleTagBuilder in builderCache.values) {
                sink.addTag(builder, registryKey)
            }
        }
    }

    //    DataGen    //

    /**
     * [HTTagsProvider]に基づいて[TagsProvider]を実装した抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class DataGen<T : Any>(
        fileHelper: ExistingFileHelper,
        output: PackOutput,
        registryKey: RegistryKey<T>,
        lookupProvider: CompletableFuture<HolderLookup.Provider>,
        modId: String,
    ) : TagsProvider<T>(output, registryKey, lookupProvider, modId, fileHelper),
        HTTagsProvider<T> {
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

        private val tagEntryMap: MutableMap<TagKey<T>, List<TagEntry>> = mutableMapOf()
        private val tagKeysToGenerate: MutableSet<TagKey<T>> = mutableSetOf()

        @Suppress("DEPRECATION")
        final override fun addTags(provider: HolderLookup.Provider) {
            // タグの値を一時的に保存
            addTagsInternal(object : BuilderFactory<T>(registryKey) {
                override fun apply(tagKey: TagKey<T>): HTTagBuilder<T> {
                    tagKeysToGenerate += tagKey
                    return createBuilder(tagKey)
                }
            })
            // 空のタグファイルを生成
            tagKeysToGenerate.forEach(::getOrCreateRawBuilder)
            // タグの値を実際に登録
            tagEntryMap.forEach { (tagKey: TagKey<T>, entries: List<TagEntry>) ->
                entries
                    .sortedWith(COMPARATOR)
                    .distinctBy(TagEntry::toString)
                    .forEach { entry: TagEntry -> tag(tagKey).add(entry) }
            }
        }

        private fun createBuilder(tagKey: TagKey<T>): HTTagBuilder<T> {
            tagKeysToGenerate += tagKey
            return HTTagBuilder { entry: TagEntry -> tagEntryMap[tagKey] = (tagEntryMap[tagKey]?.plus(entry) ?: listOf(entry)) }
        }
    }
}
