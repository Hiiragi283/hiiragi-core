package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.gui.sync.HTBoolSyncPayload
import hiiragi283.core.common.gui.sync.HTFluidSyncPayload
import hiiragi283.core.common.gui.sync.HTFractionSyncPayload
import hiiragi283.core.common.gui.sync.HTIntSyncPayload
import hiiragi283.core.common.gui.sync.HTItemSyncPayload
import hiiragi283.core.common.gui.sync.HTLongSyncPayload
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal object HCMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Data Component Type
        event.register(Registries.DATA_COMPONENT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("blueprint_number"), HCDataComponents.BLUEPRINT_NUMBER)
            helper.register(HiiragiCoreAPI.id("bottle_type"), HCDataComponents.BOTTLE_TYPE)
            helper.register(HiiragiCoreAPI.id("color"), HCDataComponents.COLOR)
            helper.register(HiiragiCoreAPI.id("description"), HCDataComponents.DESCRIPTION)
            helper.register(HiiragiCoreAPI.id("location"), HCDataComponents.LOCATION)
            helper.register(HiiragiCoreAPI.id("experience"), HCDataComponents.EXPERIENCE)

            helper.register(HiiragiCoreAPI.id(HTConst.ENERGY), HCDataComponents.ENERGY)
            helper.register(HiiragiCoreAPI.id(HTConst.FLUID), HCDataComponents.FLUID)
        }

        // Attachment Type
        event.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("in_world_recipe_caches"), HCAttachmentTypes.IN_WORLD_RECIPE_CACHES)
        }
        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("blue_print"), HTBluePrintIngredient.TYPE)
        }
        // Fluid Ingredient Type
        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("potion"), HTPotionFluidIngredient.TYPE)
        }

        // Item Result type
        event.register(HCRegistries.Keys.ITEM_RESULT_SERIALIZER) { helper ->
            helper.register(HiiragiCoreAPI.id("simple"), HTItemResult.Simple.SERIALIZER)
            helper.register(HiiragiCoreAPI.id("tag"), HTItemResult.Tagged.SERIALIZER)
            helper.register(HiiragiCoreAPI.id("material_part"), HTItemResult.MaterialPart.SERIALIZER)
        }
        // Slot Sync Type
        event.register(HCRegistries.Keys.SLOT_TYPE) { helper ->
            helper.register(HTConst.COMMON.toId("boolean"), HTBoolSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("fraction"), HTFractionSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("integer"), HTIntSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("long"), HTLongSyncPayload.TYPE)

            helper.register(vanillaId("fluid"), HTFluidSyncPayload.TYPE)
            helper.register(vanillaId("item"), HTItemSyncPayload.TYPE)
        }
    }
}
