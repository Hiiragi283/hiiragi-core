package hiiragi283.core.api.data.advancement

import hiiragi283.core.api.resource.toDescriptionKey
import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceKey

typealias HTAdvancementKey = ResourceKey<Advancement>

/**
 * 進捗のタイトルの翻訳キーを取得します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTAdvancementKey.titleKey: String get() = this.toDescriptionKey("advancements", "title")

/**
 * 進捗の説明の翻訳キーを取得します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTAdvancementKey.descKey: String get() = this.toDescriptionKey("advancements", "desc")
