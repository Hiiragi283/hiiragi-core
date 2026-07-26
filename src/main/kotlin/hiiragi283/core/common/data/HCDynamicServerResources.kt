package hiiragi283.core.common.data

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.map.HTDynamicDataMap
import hiiragi283.core.api.data.pack.HTDynamicDataRegister
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.itemTagKey
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.prefixEntries
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.times
import hiiragi283.core.common.material.CommonMaterialKeys
import kotlin.sequences.forEach
import kotlin.system.measureTimeMillis
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.apache.commons.lang3.math.Fraction

internal data object HCDynamicServerResources {
    @JvmStatic
    fun initialize() {
        HTDynamicDataRegister.LOGGER.info("HiiragiCore Data loading took {} ms", measureTimeMillis(::initializeInternal))
    }

    @JvmStatic
    private fun initializeInternal() {
        val existing: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.existingContents
        val registered: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        // Data Map
        HTDynamicDataMap(NeoForgeDataMaps.FURNACE_FUELS) {
            for (entry: HTMaterialManager.Entry in HTMaterialManager.getInstance()) {
                val baseTime: Int = entry[HTMaterialPropertyKeys.FUEL_TIME] ?: continue
                val key: HTMaterialKey = entry.key
                // Block
                setOf(registered.blocks.column(key), registered.items.column(key)).forEach { map: Map<HTPart, *> ->
                    for ((part: HTPart, _) in map) {
                        val tagKey: TagKey<Item> = part.itemTagKey(key) ?: continue
                        val fuelScale: Fraction = part[HTPartPropertyKeys.FUEL_SCALE] ?: continue
                        val fuelTime: Int = (baseTime * fuelScale).toInt()
                        add(tagKey, FurnaceFuel(fuelTime))
                    }
                }
            }
        }
        // Loot Table
        registered.blocks.forEach { (part: HTPart, key: HTMaterialKey, block: HTMaterialContents.BlockEntry) ->
            if (HTPartPropertyKeys.IS_ORE in part) {
                val raw: HTMaterialContents.ItemEntry? = registered.items[CommonParts.RAW, key]
                // 暫定的に幸運は適応しない
                HTDynamicDataRegister.addLootTable(block.get()) {
                    LootTable.lootTable()
                        .withPool(
                            LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1f))
                                .add(LootItem.lootTableItem(raw ?: it)),
                        ).setParamSet(LootContextParamSets.BLOCK)
                }
            } else {
                HTDynamicDataRegister.addLootTable(block.get()) {
                    LootTable.lootTable()
                        .withPool(
                            LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1f))
                                .add(LootItem.lootTableItem(it)),
                        ).setParamSet(LootContextParamSets.BLOCK)
                }
            }
        }
        // Tag
        HTTagsProvider.Dynamic(Registries.BLOCK) {
            // Material Block
            existing.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTMaterialContents.BlockEntry) ->
                tags(prefix, key).add(block)
            }
            registered.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTMaterialContents.BlockEntry) ->
                tags(prefix, key).add(block)
                builder(BlockTags.MINEABLE_WITH_PICKAXE).add(block)
            }
        }
        /*HTTagsProvider.Dynamic(Registries.FLUID) {
            HiiragiCoreAccess.INSTANCE.registeredFluids.forEach { (part: HTFluidPart, key: HTMaterialKey, fluid: HTMaterialContents.FluidEntry) ->
                builder(part.createTagKey(key)).add(fluid)
            }
        }*/
        HTTagsProvider.Dynamic(Registries.ITEM) {
            // Material Block
            existing.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: SimpleBlockItemSupplierWithKey) ->
                tags(prefix, key).add(block.getItemSupplier())
            }
            registered.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: SimpleBlockItemSupplierWithKey) ->
                tags(prefix, key).add(block.getItemSupplier())
            }
            // Material Fluid
            /*HiiragiCoreAccess.INSTANCE.registeredFluids.forEach { (part: HTFluidPart, key: HTMaterialKey, fluid: HTMaterialContents.FluidEntry) ->
                tags(Tags.Items.BUCKETS, part.createBucketTag(key)).add(fluid.get().bucket.toLike())
            }*/
            // Material Item
            existing.items.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: HTMaterialContents.ItemEntry) ->
                tags(prefix, key).add(item)
            }
            registered.items.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: HTMaterialContents.ItemEntry) ->
                tags(prefix, key).add(item)
                if (prefix == CommonTagPrefixes.GEM || prefix == CommonTagPrefixes.INGOT) {
                    builder(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
                }
                if (key == CommonMaterialKeys.PLASTIC) {
                    when (prefix) {
                        CommonTagPrefixes.PLATE -> HiiragiCoreTags.Items.PLASTICS
                        else -> return@forEach
                    }.let(::builder).add(item)
                }
            }
            // Material Tool
            registered.tools.forEach { (toolType: HTToolType, _, item: HTMaterialContents.ItemEntry) ->
                toolType.toolTags.map(::builder).forEach { it.add(item) }
            }
        }
        // Recipe
        HCDynamicRecipeProvider.initialize()
    }
}
