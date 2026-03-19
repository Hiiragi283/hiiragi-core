package hiiragi283.core.api.resource

import net.minecraft.resources.Identifier
import net.minecraft.util.Util

//    Identifier    //

/**
 * この[文字列][this]を[名前空間][Identifier.getNamespace]とした[ID][Identifier]を作成します。
 * @param path IDの[パス][Identifier.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun String.toId(path: String): Identifier = Identifier.fromNamespaceAndPath(this, path)

/**
 * この[文字列][this]を[名前空間][Identifier.getNamespace]とした[ID][Identifier]を作成します。
 * @param path IDの[パス][Identifier.getPath]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun String.toId(vararg path: String): Identifier = this.toId(path.joinToString(separator = "/"))

/**
 * この[ID][Identifier]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun Identifier.toDescriptionKey(prefix: String): String = Util.makeDescriptionId(prefix, this)

/**
 * この[ID][Identifier]を翻訳キーに変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun Identifier.toDescriptionKey(prefix: String, suffix: String): String = "${Util.makeDescriptionId(prefix, this@toDescriptionKey)}.$suffix"
