package hiiragi283.core.data.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.loot.HTGlobalLootModifierProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType

class HCGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : HTGlobalLootModifierProvider(output, registries, HiiragiCoreAPI.MOD_ID) {
    override fun start() {
        // Drops Elder Heart from Elder Guardian
        add(HCGlobalLootTableProvider.ELDER_HEART, builder(EntityType.ELDER_GUARDIAN).toList())
        // Drops Eternal Upgrade from Ender Dragon
        add(HCGlobalLootTableProvider.ETERNAL_UPGRADE, builder(EntityType.ENDER_DRAGON).toList())
        // Drops Trader Catalog from Wandering Trader
        add(HCGlobalLootTableProvider.TRADER_CATALOG, builder(EntityType.WANDERING_TRADER).toList())
    }
}
