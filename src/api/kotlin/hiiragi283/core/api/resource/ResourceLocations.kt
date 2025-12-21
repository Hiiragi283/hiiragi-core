package hiiragi283.core.api.resource

import net.minecraft.Util
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

//    ResourceLocation    //

/**
 * この[文字列][this]を[名前空間][ResourceLocation.getNamespace]とした[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun String.toId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(this, path)

/**
 * この[文字列][this]を[名前空間][ResourceLocation.getNamespace]とした[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun String.toId(vararg path: String): ResourceLocation = this.toId(path.joinToString(separator = "/"))

/**
 * [名前空間][ResourceLocation.getNamespace]が`minecraft`となる[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun vanillaId(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)

/**
 * [名前空間][ResourceLocation.getNamespace]が`minecraft`となる[ID][ResourceLocation]を作成します。
 * @param path IDの[パス][ResourceLocation.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun vanillaId(vararg path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path.joinToString(separator = "/"))

/**
 * この[ResourceKey]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ResourceKey<*>.toDescriptionKey(prefix: String, suffix: String? = null): String = location().toDescriptionKey(prefix, suffix)

/**
 * この[ID][ResourceLocation]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ResourceLocation.toDescriptionKey(prefix: String, suffix: String? = null): String = buildString {
    append(Util.makeDescriptionId(prefix, this@toDescriptionKey))
    if (suffix != null) {
        append('.')
        append(suffix)
    }
}
