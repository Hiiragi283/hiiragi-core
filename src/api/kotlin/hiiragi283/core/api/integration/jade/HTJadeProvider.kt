package hiiragi283.core.api.integration.jade

import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.resources.Identifier
import snownee.jade.api.IJadeProvider

interface HTJadeProvider :
    IJadeProvider,
    HTHasTranslationKey {
    override val translationKey: String
        get() {
            val id: Identifier = this.uid
            return "config.jade.plugin_${id.namespace}.${id.path}"
        }
}
