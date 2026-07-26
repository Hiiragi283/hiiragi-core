package hiiragi283.core.data.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.modifyPath
import hiiragi283.core.setup.HCItems
import java.util.function.BiConsumer
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

sealed class HCGlobalLootProvider(protected val provider: HolderLookup.Provider) : LootTableSubProvider {
    companion object {
        // entity
        @JvmField
        val ANCIENT_UPGRADE: ResourceKey<LootTable> = create("ancient_upgrade")

        @JvmField
        val ELDER_HEART: ResourceKey<LootTable> = create("elder_heart")

        @JvmField
        val ETERNAL_UPGRADE: ResourceKey<LootTable> = create("eternal_upgrade")

        @JvmField
        val TRADER_CATALOG: ResourceKey<LootTable> = create("trader_catalog")

        @JvmStatic
        private fun create(path: String): ResourceKey<LootTable> = create(HiiragiCoreAPI.id(path))

        @JvmStatic
        private fun create(id: ResourceLocation): ResourceKey<LootTable> = Registries.LOOT_TABLE.createKey(id.modifyPath { "drop_$it" })
    }

    protected val fortune: Holder<Enchantment> by lazy { provider.holderOrThrow(Enchantments.FORTUNE) }

    //    Block    //

    /*class BlockProvider(provider: HolderLookup.Provider) : HCGlobalLootProvider(provider) {
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents
            // Drops Ominous Metal Rod from Spawners
            val ominousMetalRod: ItemLike = contents.getItemOrThrow(CommonTagPrefixes.ROD, HCMaterialKeys.OMINOUS_METAL)
            output.accept(
                OMINOUS_METAL_ROD,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(
                                LootItem
                                    .lootTableItem(ominousMetalRod)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2f, 6f)))
                                    .apply(ApplyBonusCount.addUniformBonusCount(fortune))
                                    .apply(LimitCount.limitCount(IntRange.range(2, 6))),
                            ),
                    ),
            )
            // Drops Ancient Metal Scrap from Reinforced Deepslate
            val ancientMetalScrap: ItemLike = contents.getItemOrThrow(CommonTagPrefixes.SCRAP, HCMaterialKeys.ANCIENT_METAL)
            output.accept(
                DEEP_STEEL_SCRAP,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(
                                LootItem
                                    .lootTableItem(ancientMetalScrap)
                                    .apply(ApplyBonusCount.addUniformBonusCount(fortune))
                                    .apply(LimitCount.limitCount(IntRange.range(1, 4))),
                            ),
                    ),
            )
        }
    }*/

    //    EntityProvider    //

    class EntityProvider(provider: HolderLookup.Provider) : HCGlobalLootProvider(provider) {
        override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
            // Drops Ancient Upgrade from Warden
            output.accept(
                ANCIENT_UPGRADE,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(LootItem.lootTableItem(HCItems.ANCIENT_UPGRADE)),
                    ),
            )
            // Drops Elder Heart from Elder Guardian
            output.accept(
                ELDER_HEART,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
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
            // Drops Eternal Upgrade from Ender Dragon
            output.accept(
                ETERNAL_UPGRADE,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(LootItem.lootTableItem(HCItems.ETERNAL_UPGRADE)),
                    ),
            )
            // Drops Trader Catalog from Wandering Trader
            output.accept(
                TRADER_CATALOG,
                LootTable
                    .lootTable()
                    .withPool(
                        LootPool
                            .lootPool()
                            .add(LootItem.lootTableItem(HCItems.TRADER_CATALOG)),
                    ),
            )
        }
    }
}
