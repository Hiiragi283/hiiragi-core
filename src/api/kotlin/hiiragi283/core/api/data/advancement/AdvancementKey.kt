package hiiragi283.core.api.data.advancement

import hiiragi283.core.api.resource.toLanguageKey
import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceKey

typealias AdvancementKey = ResourceKey<Advancement>

val AdvancementKey.titleKey: String get() = this.toLanguageKey("title")

val AdvancementKey.descKey: String get() = this.toLanguageKey("desc")
