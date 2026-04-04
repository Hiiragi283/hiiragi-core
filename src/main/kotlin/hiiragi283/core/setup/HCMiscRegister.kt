package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.gui.sync.HTBoolSyncPayload
import hiiragi283.core.common.gui.sync.HTDoubleSyncPayload
import hiiragi283.core.common.gui.sync.HTFluidSyncPayload
import hiiragi283.core.common.gui.sync.HTIntSyncPayload
import hiiragi283.core.common.gui.sync.HTLongSyncPayload
import hiiragi283.core.common.item.consumeEffects.HTRemoveRandomStatusEffectConsumeEffect
import hiiragi283.core.impl.recipe.ingredient.HTPotionFluidIngredient
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal object HCMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Consume Effect Type
        event.register(Registries.CONSUME_EFFECT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("remove_random_effect"), HTRemoveRandomStatusEffectConsumeEffect.TYPE)
        }

        // Fluid Ingredient Type
        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("potion"), HTPotionFluidIngredient.TYPE)
        }

        // Slot Sync Type
        event.register(HCRegistries.Keys.SLOT_TYPE) { helper ->
            helper.register(HTConst.COMMON.toId("boolean"), HTBoolSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("double"), HTDoubleSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("integer"), HTIntSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("long"), HTLongSyncPayload.TYPE)

            helper.register(HTConst.MINECRAFT.toId("fluid"), HTFluidSyncPayload.TYPE)
        }
    }
}
