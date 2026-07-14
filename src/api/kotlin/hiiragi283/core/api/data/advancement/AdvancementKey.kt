package hiiragi283.core.api.data.advancement

import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.toLanguageKey
import net.minecraft.advancements.Advancement
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias AdvancementKey = ResourceKey<Advancement>

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun AdvancementKey(namespace: String, path: String): AdvancementKey = Registries.ADVANCEMENT.createKey(namespace, path)

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun AdvancementKey(id: ResourceLocation): AdvancementKey = Registries.ADVANCEMENT.createKey(id)

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
