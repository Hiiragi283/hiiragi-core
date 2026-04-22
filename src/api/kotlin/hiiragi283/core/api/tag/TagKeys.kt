package hiiragi283.core.api.tag

import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.text.MutableText
import net.minecraft.network.chat.Component
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
 * この[TagKey]の名前を取得します。
 * @return 翻訳がない場合は`#`を先頭につけた[ID][TagKey.location]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun TagKey<*>.getName(): MutableText = Component.translatableWithFallback(Tags.getTagTranslationKey(this), "#${this.location}")
