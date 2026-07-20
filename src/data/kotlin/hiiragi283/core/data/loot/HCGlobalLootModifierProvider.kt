package hiiragi283.core.data.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.loot.HTGlobalLootModifierProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType

class HCGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : HTGlobalLootModifierProvider(output, registries, HiiragiCoreAPI.MOD_ID) {
    override fun start() {
        // Drops Ancient Upgrade from Warden
        add(HCGlobalLootProvider.ANCIENT_UPGRADE, builder(EntityType.WARDEN).build())
        // Drops Elder Heart from Elder Guardian
        add(HCGlobalLootProvider.ELDER_HEART, builder(EntityType.ELDER_GUARDIAN).build())
        // Drops Eternal Upgrade from Ender Dragon
        add(HCGlobalLootProvider.ETERNAL_UPGRADE, builder(EntityType.ENDER_DRAGON).build())
        // Drops Trader Catalog from Wandering Trader
        add(HCGlobalLootProvider.TRADER_CATALOG, builder(EntityType.WANDERING_TRADER).build())
    }
}
