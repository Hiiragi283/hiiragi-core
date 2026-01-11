package hiiragi283.core.data.server.loot

import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.core.api.function.partially2
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.ItemLike
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

        registerOres()
        registerCrops()
    }

    private fun registerOres() {
        registerOre(HCMaterialPrefixes.DUST, CommonMaterialKeys.CINNABAR, UniformGenerator.between(2f, 5f))
        registerOre(HCMaterialPrefixes.DUST, CommonMaterialKeys.SULFUR, UniformGenerator.between(4f, 5f))
    }

    private fun registerOre(basePrefix: HTMaterialPrefix, key: HTMaterialKey, range: UniformGenerator?) {
        val ores: Array<HTMaterialPrefix> = arrayOf(
            HCMaterialPrefixes.ORE,
            HCMaterialPrefixes.ORE_DEEPSLATE,
            HCMaterialPrefixes.ORE_NETHER,
            HCMaterialPrefixes.ORE_END,
        )
        for (prefix: HTMaterialPrefix in ores) {
            val ore: HTSimpleDeferredBlock = HCBlocks.MATERIALS[prefix, key] ?: continue
            val drop: ItemLike = HCItems.MATERIALS[basePrefix, key] ?: continue
            add(ore, ::createOreDrops.partially2(drop, range))
        }
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
