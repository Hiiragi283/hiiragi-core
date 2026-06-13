package hiiragi283.lib.data.tag

import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.lib.tag.RawTagKey
import hiiragi283.lib.util.HTBuilderMarker
import java.util.function.Consumer
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

/**
 * [HTTagsProvider]で使用されるタグのビルダークラスです。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
fun interface HTTagBuilder<T : Any> : Consumer<TagEntry> {
    /**
     * 指定した要素をタグに追加します。
     * @param key 要素のIDを提供する[ResourceKey]
     * @param type このエントリの依存関係
     */
    fun add(key: ResourceKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(key.identifier(), type)

    /**
     * 指定した要素をタグに追加します。
     * @param like 要素のIDを提供する[HTIdLike]
     * @param type このエントリの依存関係
     */
    fun add(like: HTIdLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(like.getId(), type)

    /**
     * 指定した要素をタグに追加します。
     * @param id 要素のID
     * @param type このエントリの依存関係
     */
    fun add(id: Identifier, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.element(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalElement(id)
        }.let(this::accept)
    }

    /**
     * 指定した子タグをタグに追加します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     * @param type このエントリの依存関係
     */
    fun addTag(prefix: HTTagPrefix, material: HTMaterialKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = addTag(prefix.materialTag(material), type)

    /**
     * 指定した子タグをタグに追加します。
     * @param child 子タグ
     * @param type このエントリの依存関係
     */
    fun addTag(child: TagKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = addTag(child.location, type)

    /**
     * 指定した子タグをタグに追加します。
     * @param child 子タグとなる[RawTagKey]
     * @param type このエントリの依存関係
     */
    fun addTag(child: RawTagKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = addTag(child.location, type)

    /**
     * 指定した子タグをタグに追加します。
     * @param id 子タグのID
     * @param type このエントリの依存関係
     */
    fun addTag(id: Identifier, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.tag(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalTag(id)
        }.let(this::accept)
    }
}
