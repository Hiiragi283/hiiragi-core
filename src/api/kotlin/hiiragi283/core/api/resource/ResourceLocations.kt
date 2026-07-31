@file:Suppress("NOTHING_TO_INLINE")

package hiiragi283.core.api.resource

import hiiragi283.core.api.util.Identity
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

//    ResourceLocation    //

/**
 * この[文字列][this]を[名前空間][ResourceLocation.getNamespace]とした[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
inline infix fun String.toId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(this, path)

/**
 * この[文字列][this]を[名前空間][ResourceLocation.getNamespace]とした[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun String.toId(vararg path: String): ResourceLocation = this.toId(path.joinToString(separator = "/"))

/**
 * 名前空間が`minecraft`となる[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
inline fun vanillaId(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)

/**
 * 名前空間が`minecraft`となる[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun vanillaId(vararg path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path.joinToString(separator = "/"))

inline fun ResourceLocation.modifyPath(operator: Identity<String>): ResourceLocation = this.namespace.toId(operator(this.path))

//    ResourceKey    //

/**
 * この[ResourceKey]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
inline fun ResourceKey<*>.toLanguageKey(): String = this.location().toLanguageKey(this.registryKey().location().path)

/**
 * この[ResourceKey]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
inline fun ResourceKey<*>.toLanguageKey(suffix: String): String = this.location().toLanguageKey(this.registryKey().location().path, suffix)

//    HTIdLike    //

/**
 * この[HTIdLike]から，`block/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.blockId: ResourceLocation get() = when {
    this.path.startsWith("block/") -> getId()
    else -> getId().withPrefix("block/")
}

/**
 * この[HTIdLike]から，`item/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.itemId: ResourceLocation get() = when {
    this.path.startsWith("item/") -> getId()
    else -> getId().withPrefix("item/")
}
