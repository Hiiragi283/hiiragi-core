package hiiragi283.core.data.server.loot

import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.setup.HCBlocks
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class HCBlockLootTableProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(registries) {
    override fun generate() {
        HCBlocks.REGISTER.asBlockSequence().forEach(::dropSelf)

        registerMaterials()

        registerOres()
        registerCrops()
    }

    private fun registerOres() {
        registerOre(CommonTagPrefixes.RAW, CommonMaterialKeys.ZINC, UniformGenerator.between(2f, 5f))

        registerOre(CommonTagPrefixes.RAW, CommonMaterialKeys.TIN, null)
    }

    private fun registerCrops() {
        add(HCBlocks.WARPED_WART) { block: Block ->
            val stateCondition: LootItemBlockStatePropertyCondition.Builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(block)
                .setProperties(
                    StatePropertiesPredicate.Builder.properties().hasProperty(NetherWartBlock.AGE, 3),
                )

            LootTable
                .lootTable()
                .withPool(
                    applyExplosionDecay(
                        block,
                        LootPool
                            .lootPool()
                            .setRolls(ConstantValue.exactly(1f))
                            .add(
                                LootItem
                                    .lootTableItem(HCBlocks.WARPED_WART)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2f, 4f)).`when`(stateCondition))
                                    .apply(ApplyBonusCount.addUniformBonusCount(fortune).`when`(stateCondition)),
                            ),
                    ),
                )
        }
    }
}
