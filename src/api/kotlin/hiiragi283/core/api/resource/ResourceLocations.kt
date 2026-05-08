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
 * この[ID][ResourceLocation]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun ResourceLocation.toDescriptionKey(prefix: String): String = Util.makeDescriptionId(prefix, this)

/**
 * この[ID][ResourceLocation]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ResourceLocation.toDescriptionKey(prefix: String, suffix: String): String = "${Util.makeDescriptionId(prefix, this@toDescriptionKey)}.$suffix"

/**
 * この[ResourceKey]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun ResourceKey<*>.toDescriptionKey(prefix: String): String = location().toDescriptionKey(prefix)

/**
 * この[ResourceKey]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ResourceKey<*>.toDescriptionKey(prefix: String, suffix: String): String = location().toDescriptionKey(prefix, suffix)
