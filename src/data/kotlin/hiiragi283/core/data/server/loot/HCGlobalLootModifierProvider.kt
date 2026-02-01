package hiiragi283.core.data.server.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.loot.HTGlobalLootModifierProvider
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition

class HCGlobalLootModifierProvider(context: HTDataGenContext) : HTGlobalLootModifierProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun start() {
        // Drops Ominous Metal rod from Spawners
        add(
            HCGlobalLootProvider.OMINOUS_METAL_ROD,
            AnyOfCondition
                .anyOf(
                    builder(Blocks.SPAWNER),
                    builder(Blocks.TRIAL_SPAWNER),
                    builder(Blocks.VAULT),
                ).build(),
        )
        // Drops Ancient Metal Scrap from Reinforced Deepslate
        add(HCGlobalLootProvider.DEEP_STEEL_SCRAP, builder(Blocks.REINFORCED_DEEPSLATE).build())

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
