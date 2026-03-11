package hiiragi283.core.api.data.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.HTServerResourceGenTask
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.api.tag.createTagKey
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import java.util.function.Function

/**
 * [HTTagBuilder]に基づいてタグを生成するインターフェースです。
 * @param T レジストリの要素のクラス
 * @param registryKey レジストリを表すキー
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see GenTask
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
     * タグをチェインして登録します。
     * @return [HTTagPrefix.createTagKey]に対する[HTTagBuilder]
     */
    fun addMaterial(factory: BuilderFactory<T>, prefix: HTTagPrefix, material: HTMaterialLike): HTTagBuilder<T> =
        addTags(factory, prefix.createCommonTagKey(registryKey), prefix.createTagKey(registryKey, material))

    /**
     * 指定した[ID][id]から[タグ][TagKey]を作成します。
     * @since 0.12.0
     */
    fun tag(id: ResourceLocation): TagKey<T> = registryKey.createTagKey(id)

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

    //    GenTask    //

    /**
     * [HTTagsProvider]に基づいて[HTServerResourceGenTask]を実装した抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class GenTask<T : Any>(override val registryKey: RegistryKey<T>) :
        HTTagsProvider<T>,
        HTServerResourceGenTask {
        private val builderCache: MutableMap<TagKey<T>, SimpleTagBuilder> = hashMapOf()

        final override fun accept(sink: ResourceSink) {
            addTagsInternal { tagKey: TagKey<T> ->
                HTTagBuilder(registryKey) { entry: TagEntry ->
                    builderCache
                        .computeIfAbsent(tagKey) { SimpleTagBuilder.of(tagKey) }
                        .add(entry)
                }
            }
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
        output: PackOutput,
        override val registryKey: RegistryKey<T>,
        lookupProvider: CompletableFuture<HolderLookup.Provider>,
        modId: String,
        existingFileHelper: ExistingFileHelper?,
    ) : TagsProvider<T>(output, registryKey, lookupProvider, modId, existingFileHelper),
        HTTagsProvider<T> {
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
                        .forEach { entry: TagEntry -> tag(tagKey).add(entry) }
                }
        }

        private fun createBuilder(map: MutableMap<TagKey<T>, List<TagEntry>>, tagKey: TagKey<T>): HTTagBuilder<T> =
            HTTagBuilder(registryKey) { entry: TagEntry ->
                map[tagKey] = (map[tagKey]?.plus(entry) ?: listOf(entry))
            }
    }
}
