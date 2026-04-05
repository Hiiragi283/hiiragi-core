package hiiragi283.core.api.integration.jade

import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.IJadeProvider

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[IJadeProvider]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
interface HTJadeProvider :
    IJadeProvider,
    HTHasTranslationKey {
    override val translationKey: String
        get() {
            val id: ResourceLocation = this.uid
            return "config.jade.plugin_${id.namespace}.${id.path}"
        }
}
