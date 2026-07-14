package hiiragi283.core.api.data.tag

import hiiragi283.core.api.collection.SetMultiMap
import hiiragi283.core.api.data.HTServerResourceGenTask
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.data.ExistingFileHelper

interface HTTagsProvider<T : Any> {
    val registryKey: RegistryKey<T>

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param tagKey 生成対象のタグ
     * @since 26.2.0
     */
    fun builder(tagKey: TagKey<T>): HTTagBuilder<T>

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    fun tags(prefix: HTTagPrefix, material: HTMaterialKey): HTTagBuilder<T> = tags(prefix.rawCommonTag.create(registryKey), prefix.materialTag(material).create(registryKey))

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param tagKey 起点となるタグ
     * @param children [tagKey]からチェインして生成するタグ
     * @return [children]の最後の値に対する[HTTagBuilder]
     */
    fun tags(tagKey: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> = children.fold(builder(tagKey)) { builder: HTTagBuilder<T>, tagKeyIn: TagKey<T> ->
        builder.addTag(tagKeyIn)
        builder(tagKeyIn)
    }

    fun createKey(id: ResourceLocation): ResourceKey<T> = registryKey.createKey(id)

    fun createKey(namespace: String, path: String): ResourceKey<T> = registryKey.createKey(namespace, path)

    //    GenTask    //

    abstract class GenTask<T : Any>(override val registryKey: RegistryKey<T>) :
        HTTagsProvider<T>,
        HTServerResourceGenTask {
        private val builderCache: MutableMap<TagKey<T>, SimpleTagBuilder> = hashMapOf()

        final override fun builder(tagKey: TagKey<T>): HTTagBuilder<T> = HTTagBuilder { entry: TagEntry -> builderCache.computeIfAbsent(tagKey, SimpleTagBuilder::of).add(entry) }

        final override fun accept(sink: ResourceSink) {
            // タグの値を一時的に保存
            appendTags()
            // タグの値を実際に登録
            for (builder: SimpleTagBuilder in builderCache.values) {
                sink.addTag(builder, registryKey)
            }
        }

        /**
         * 生成するタグを登録します。
         */
        protected abstract fun appendTags()
    }

    //    DataGen    //

    abstract class DataGen<T : Any>(
        fileHelper: ExistingFileHelper,
        output: PackOutput,
        override val registryKey: RegistryKey<T>,
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

        private val entryCache = SetMultiMap.Builder<TagKey<T>, TagEntry>()

        final override fun builder(tagKey: TagKey<T>): HTTagBuilder<T> = HTTagBuilder { entry: TagEntry -> entryCache.put(tagKey, entry) }

        final override fun addTags(provider: HolderLookup.Provider) {
            createEmptyTags(provider, ::getOrCreateRawBuilder)

            appendTags(provider)

            entryCache.build().asMap().forEach { (tagKey: TagKey<T>, entries: Collection<TagEntry>) ->
                entries
                    .sortedWith(COMPARATOR)
                    .distinctBy(TagEntry::toString)
                    .forEach { entry: TagEntry -> getOrCreateRawBuilder(tagKey).add(entry) }
            }
        }

        protected open fun createEmptyTags(registries: HolderLookup.Provider, consumer: (TagKey<T>) -> Unit) {}

        /**
         * 生成するタグを登録します。
         */
        protected abstract fun appendTags(registries: HolderLookup.Provider)
    }
}
