package hiiragi283.core.api.data.tag

import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.concurrent.CompletableFuture
import java.util.function.Function

/**
 * [HTTagBuilder]に基づいてタグを生成するインターフェースです。
 * @param T レジストリの要素のクラス
 * @param registryKey レジストリを表すキー
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see DataGen
 */
sealed interface HTTagsProvider<T : Any> {
    val registryKey: RegistryKey<T>

    /**
     * 生成するタグを登録します。
     * @param factory [TagKey]から[HTTagBuilder]を取得するブロック
     */
    fun addTagsInternal(factory: BuilderFactory<T>)

    //    Extensions    //

    /**
     * タグをチェインして登録します。
     * @return 最後の[children]に対する[HTTagBuilder]
     */
    fun addTags(factory: BuilderFactory<T>, parent: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> {
        check(!children.isEmpty()) { "Empty tag key children" }
        return children.fold(factory.apply(parent)) { current: HTTagBuilder<T>, child: TagKey<T> ->
            current.addTag(child)
            factory.apply(child)
        }
    }

    /**
     * 指定した[ID][id]から[タグ][TagKey]を作成します。
     * @since 0.12.0
     */
    fun tag(id: Identifier): TagKey<T> = registryKey.createTagKey(id)

    /**
     * 指定した[パス][path]から[共通タグ][TagKey]を作成します。
     * @since 0.12.0
     */
    fun commonTag(path: String): TagKey<T> = registryKey.createCommonTag(path)

    /**
     * 指定した[パス][path]から[共通タグ][TagKey]を作成します。
     * @since 0.12.0
     */
    fun commonTag(vararg path: String): TagKey<T> = registryKey.createCommonTag(*path)

    //    Factory    //

    fun interface BuilderFactory<T : Any> : Function<TagKey<T>, HTTagBuilder<T>>

    //    DataGen    //

    /**
     * [HTTagsProvider]に基づいて[TagsProvider]を実装した抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class DataGen<T : Any>(
        output: PackOutput,
        override val registryKey: RegistryKey<T>,
        lookupProvider: CompletableFuture<HolderLookup.Provider>,
        modId: String,
    ) : TagsProvider<T>(output, registryKey, lookupProvider, modId),
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

        @Suppress("DEPRECATION")
        final override fun addTags(provider: HolderLookup.Provider) {
            buildMap { addTagsInternal { tagKey: TagKey<T> -> createBuilder(this, tagKey) } }
                .forEach { (tagKey: TagKey<T>, entries: List<TagEntry>) ->
                    entries
                        .sortedWith(COMPARATOR)
                        .distinctBy(TagEntry::toString)
                        .forEach { entry: TagEntry -> getOrCreateRawBuilder(tagKey).add(entry) }
                }
        }

        private fun createBuilder(map: MutableMap<TagKey<T>, List<TagEntry>>, tagKey: TagKey<T>): HTTagBuilder<T> =
            HTTagBuilder(registryKey) { entry: TagEntry ->
                map[tagKey] = (map[tagKey]?.plus(entry) ?: listOf(entry))
            }
    }
}
