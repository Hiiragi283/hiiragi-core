package hiiragi283.core.data.server.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.loot.HTGlobalLootModifierProvider
import net.minecraft.world.entity.EntityType

class HCGlobalLootModifierProvider(context: HTDataGenContext) : HTGlobalLootModifierProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun start() {
        // Drops Elder Heart from Elder Guardian
        add(HCGlobalLootProvider.DROP_ELDER_HEART, builder(EntityType.ELDER_GUARDIAN).build())
        // Drops Trader Catalog from Wandering Trader
        add(HCGlobalLootProvider.DROP_TRADER_CATALOG, builder(EntityType.WANDERING_TRADER).build())
    }
}
