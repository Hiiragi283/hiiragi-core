package hiiragi283.core.api.data.tag

import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.function.Consumer

/**
 * [HTTagsProvider]で使用されるビルダークラスです。
 * @param T レジストリの要素のクラス
 * @param registryKey レジストリを表すキー
 * @param consumer [TagEntry]を渡すブロック
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class HTTagBuilder<T : Any>(private val registryKey: RegistryKey<T>, private val consumer: Consumer<TagEntry>) {
    /**
     * 指定した[key]から[ID][Identifier]を追加します。
     * @param type このエントリの依存関係
     */
    fun add(key: ResourceKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(key.identifier(), type)

    /**
     * 指定した[like]から[ID][Identifier]を追加します。
     * @param type このエントリの依存関係
     */
    fun add(like: HTIdLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = add(like.getId(), type)

    /**
     * 指定した[ID][Identifier]を追加します。
     * @param type このエントリの依存関係
     */
    fun add(id: Identifier, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.element(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalElement(id)
        }.let(consumer::accept)
    }

    /**
     * 指定した[タグ][child]を追加します。
     * @param type このエントリの依存関係
     */
    fun addTag(child: TagKey<T>, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = addTag(child.location, type)

    /**
     * 指定した[タグ][id]を追加します。
     * @param type このエントリの依存関係
     * @since 0.13.0
     */
    fun addTag(id: Identifier, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<T> = apply {
        when (type) {
            HTTagDependType.REQUIRED -> TagEntry.tag(id)
            HTTagDependType.OPTIONAL -> TagEntry.optionalTag(id)
        }.let(consumer::accept)
    }
}
