package hiiragi283.core.api.data.tag

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.HTTagPrefix
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.function.Function

/**
 * [HTTagBuilder]に基づいて[TagKey]を生成する[ResourceGenTask]の抽象クラスです。
 * @param T レジストリの要素のクラス
 * @param registryKey レジストリを表すキー
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
abstract class HTTagsProvider<T : Any>(private val registryKey: RegistryKey<T>) : ResourceGenTask {
    private val builderCache: MutableMap<TagKey<T>, SimpleTagBuilder> = hashMapOf()

    override fun accept(manager: ResourceManager, sink: ResourceSink) {
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

    /**
     * 生成するタグを登録します。
     * @param factory [TagKey]から[HTTagBuilder]を取得するブロック
     */
    protected abstract fun addTagsInternal(factory: BuilderFactory<T>)

    //    Extensions    //

    protected val contents: HTMaterialContents get() = HiiragiCoreAccess.INSTANCE.materialContents

    /**
     * タグをチェインして登録します。
     * @return 最後の[children]に対する[HTTagBuilder]
     */
    protected fun addTags(factory: BuilderFactory<T>, parent: TagKey<T>, vararg children: TagKey<T>): HTTagBuilder<T> {
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
    protected fun addMaterial(factory: BuilderFactory<T>, prefix: HTTagPrefix, material: HTMaterialLike): HTTagBuilder<T> =
        addTags(factory, prefix.createCommonTagKey(registryKey), prefix.createTagKey(registryKey, material))

    //    Factory    //

    fun interface BuilderFactory<T : Any> : Function<TagKey<T>, HTTagBuilder<T>>
}
