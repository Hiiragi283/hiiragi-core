package hiiragi283.core.api.data.tag

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.function.Consumer

/**
 * [HTTagsProvider]で使用されるビルダークラスです。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTTagBuilder<T : Any> : Consumer<TagEntry> {
    /**
     * 指定した[key]から[ID][ResourceLocation]を追加します。
     * @param type このエントリの依存関係
     */
    fun add(key: ResourceKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(key.location(), type)

    /**
     * 指定した[like]から[ID][ResourceLocation]を追加します。
     * @param type このエントリの依存関係
     */
    fun add(like: HTIdLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(like.getId(), type)

    /**
     * 指定した[ID][ResourceLocation]を追加します。
     * @param type このエントリの依存関係
     */
    fun add(id: ResourceLocation, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.element(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalElement(id)
        }.let(this::accept)
    }

    /**
     * 指定した[プレフィックス][prefix]と[素材][material]を追加します。
     * @param type このエントリの依存関係
     */
    fun addTag(prefix: HTTagPrefix, material: HTMaterialLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> =
        addTag(prefix.materialTag(material), type)

    /**
     * 指定した[タグ][child]を追加します。
     * @param type このエントリの依存関係
     */
    fun addTag(child: TagKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = addTag(child.location, type)

    /**
     * 指定した[タグ][child]を追加します。
     * @param type このエントリの依存関係
     * @since 0.16.0
     */
    fun addTag(child: RawTagKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = addTag(child.location, type)

    /**
     * 指定した[タグ][id]を追加します。
     * @param type このエントリの依存関係
     * @since 0.13.0
     */
    fun addTag(id: ResourceLocation, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.tag(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalTag(id)
        }.let(this::accept)
    }
}
