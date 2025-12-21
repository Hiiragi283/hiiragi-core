package hiiragi283.core.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.Tags

/**
 * 指定した[レジストリキー][RegistryKey]と[ID][ResourceLocation]から[TagKey]を作成します。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> RegistryKey<T>.createTagKey(id: ResourceLocation): TagKey<T> = TagKey.create(this, id)

/**
 * 指定した[レジストリキー][RegistryKey]と[path]から[TagKey]を作成します。
 * @param T レジストリの要素のクラス
 * @return 名前空間が["c"][HTConst.COMMON]となる[TagKey]のインスタンス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> RegistryKey<T>.createCommonTag(path: String): TagKey<T> = createTagKey(HTConst.COMMON.toId(path))

/**
 * 指定した[レジストリキー][RegistryKey]と[path]から[TagKey]を作成します。
 * @param T レジストリの要素のクラス
 * @return 名前空間が["c"][HTConst.COMMON]となる[TagKey]のインスタンス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> RegistryKey<T>.createCommonTag(vararg path: String): TagKey<T> = createTagKey(HTConst.COMMON.toId(*path))

/**
 * この[TagKey]の名前を取得します。
 * @return 翻訳がない場合は`#`を先頭につけた[ID][TagKey.location]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun TagKey<*>.getName(): MutableComponent = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
