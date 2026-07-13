package hiiragi283.core.api.data.advancement

import hiiragi283.core.api.resource.toLanguageKey
import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceKey

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias AdvancementKey = ResourceKey<Advancement>

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
val AdvancementKey.titleKey: String get() = this.toLanguageKey("title")

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
val AdvancementKey.descKey: String get() = this.toLanguageKey("desc")
