package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.gui.sync.HTBoolSyncPayload
import hiiragi283.core.common.gui.sync.HTFluidSyncPayload
import hiiragi283.core.common.gui.sync.HTFractionSyncPayload
import hiiragi283.core.common.gui.sync.HTIntSyncPayload
import hiiragi283.core.common.gui.sync.HTItemSyncPayload
import hiiragi283.core.common.gui.sync.HTLongSyncPayload
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal object HCMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("blue_print"), HTBluePrintIngredient.TYPE)
        }
        // Fluid Ingredient Type
        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("potion"), HTPotionFluidIngredient.TYPE)
        }

        // Item Result type
        event.register(HCRegistries.Keys.ITEM_RESULT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("simple"), HTItemResult.SimpleEntry.TYPE)
            helper.register(HiiragiCoreAPI.id("tag"), HTItemResult.TagEntry.TYPE)
            helper.register(HiiragiCoreAPI.id("enchanted_book"), HTItemResult.EnchantedBookEntry.TYPE)
            helper.register(HiiragiCoreAPI.id("material_part"), HTItemResult.MaterialPartEntry.TYPE)
        }
        // Slot Sync Type
        event.register(HCRegistries.Keys.SLOT_TYPE) { helper ->
            helper.register(HTConst.COMMON.toId("boolean"), HTBoolSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("fraction"), HTFractionSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("integer"), HTIntSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("long"), HTLongSyncPayload.TYPE)

            helper.register(HTConst.MINECRAFT.toId("fluid"), HTFluidSyncPayload.TYPE)
            helper.register(HTConst.MINECRAFT.toId("item"), HTItemSyncPayload.TYPE)
        }
    }
}
