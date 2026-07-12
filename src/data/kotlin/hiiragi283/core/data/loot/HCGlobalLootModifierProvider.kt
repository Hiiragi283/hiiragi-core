package hiiragi283.core.data.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.loot.HTGlobalLootModifierProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityTypes

class HCGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : HTGlobalLootModifierProvider(output, registries, HiiragiCoreAPI.MOD_ID) {
    override fun start() {
        // Drops Elder Heart from Elder Guardian
        add(HCGlobalLootTableProvider.ELDER_HEART, condition(EntityTypes.ELDER_GUARDIAN).toList())
        // Drops Eternal Upgrade from Ender Dragon
        add(HCGlobalLootTableProvider.ETERNAL_UPGRADE, condition(EntityTypes.ENDER_DRAGON).toList())
        // Drops Trader Catalog from Wandering Trader
        add(HCGlobalLootTableProvider.TRADER_CATALOG, condition(EntityTypes.WANDERING_TRADER).toList())
    }
}
