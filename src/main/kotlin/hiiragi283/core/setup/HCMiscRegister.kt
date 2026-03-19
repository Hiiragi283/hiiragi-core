package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.item.consume_effects.HTRemoveRandomStatusEffectConsumeEffect
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.RegisterEvent

internal object HCMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Consume Effect Type
        event.register(Registries.CONSUME_EFFECT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("remove_random_effect"), HTRemoveRandomStatusEffectConsumeEffect.TYPE)
        }
    }
}
