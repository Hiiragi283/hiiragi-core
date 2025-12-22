package hiiragi283.core.api.data.tag

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.function.Consumer

/**
 * [HTTagsProvider]で使用されるビルダークラスです。
 * @param T レジストリの要素のクラス
 * @param registryKey レジストリを表すキー
 * @param consumer [TagEntry]を渡すブロック
 */
class HTTagBuilder<T : Any>(private val registryKey: RegistryKey<T>, private val consumer: Consumer<TagEntry>) {
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
        }.let(consumer::accept)
    }

    /**
     * 指定した[プレフィックス][prefix]と[素材][material]を追加します。
     * @param type このエントリの依存関係
     */
    fun addTag(prefix: HTPrefixLike, material: HTMaterialLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> =
        addTag(prefix.createTagKey(registryKey, material), type)

    /**
     * 指定した[タグ][child]を追加します。
     * @param type このエントリの依存関係
     */
    fun addTag(child: TagKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.tag(child.location)
            HTTagDependType.OPTIONAL -> TagEntry.optionalTag(child.location)
        }.let(consumer::accept)
    }
}
