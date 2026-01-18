package hiiragi283.core.data.server.loot

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.core.api.function.partially2
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCMiscRegister
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

        HCMiscRegister.materialBlocks
            .values
            .filter { it.getNamespace() == HiiragiCoreAPI.MOD_ID }
            .forEach(::dropSelf)

        registerOres()
        registerCrops()
    }

    private fun registerOres() {
        registerOre(CommonTagPrefixes.RAW, CommonMaterialKeys.ZINC, UniformGenerator.between(2f, 5f))
    }

    private fun registerOre(basePrefix: HTTagPrefix, key: HTMaterialKey, range: UniformGenerator?) {
        for (prefix: HTTagPrefix in CommonTagPrefixes.ORES) {
            val ore: HTSimpleDeferredBlock = HCMiscRegister.materialBlocks[prefix, key] ?: continue
            val drop: ItemLike = HCMiscRegister.materialItems[basePrefix, key] ?: continue
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
