package hiiragi283.core.data.server.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.function.BiConsumer

sealed class HCGlobalLootProvider(protected val provider: HolderLookup.Provider) : LootTableSubProvider {
    companion object {
        @JvmField
        val DROP_ELDER_HEART: ResourceKey<LootTable> = create("drop_elder_heart")

        @JvmField
        val DROP_TRADER_CATALOG: ResourceKey<LootTable> = create("drop_trader_catalog")

        @JvmStatic
        private fun create(path: String): ResourceKey<LootTable> = Registries.LOOT_TABLE.createKey(HiiragiCoreAPI.id(path))
    }

    //    Entity    //

    class Entity(provider: HolderLookup.Provider) : HCGlobalLootProvider(provider) {
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            // Drops Elder Heart from Elder Guardian
            output.accept(
                DROP_ELDER_HEART,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .setRolls(ConstantValue.exactly(1f))
                            .add(
                                LootItem
                                    .lootTableItem(HCItems.ELDER_HEART)
                                    .apply(
                                        EnchantedCountIncreaseFunction.lootingMultiplier(
                                            provider,
                                            UniformGenerator.between(0f, 1f),
                                        ),
                                    ),
                            ),
                    ),
            )
            // Drops Trader Catalog from Wandering Trader
            output.accept(
                DROP_TRADER_CATALOG,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .setRolls(ConstantValue.exactly(1f))
                            .add(LootItem.lootTableItem(HCItems.TRADER_CATALOG)),
                    ),
            )
        }
    }
}
