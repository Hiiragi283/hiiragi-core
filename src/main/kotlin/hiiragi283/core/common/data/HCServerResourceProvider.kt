package hiiragi283.core.common.data

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.map.HTDataMapGenTask
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.HTSimpleMaterialContents
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.itemTagKey
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.prefixEntries
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.times
import hiiragi283.core.common.material.CommonMaterialKeys
import net.mehvahdjukaar.moonlight.api.resources.ResType
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

data object HCServerResourceProvider : HTDynamicResourceProvider.Server(HiiragiCoreAPI.MOD_ID) {
    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        val existing: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.existingContents
        val registered: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        // Data Map
        executor.accept(object : HTDataMapGenTask<FurnaceFuel, Item>(NeoForgeDataMaps.FURNACE_FUELS) {
            override fun gather() {
                for (entry: HTMaterialManager.Entry in HTMaterialManager.getInstance()) {
                    val baseTime: Int = entry[HTMaterialPropertyKeys.FUEL_TIME] ?: continue
                    // Block
                    for ((part: HTPart, _) in registered.blocks.column(entry)) {
                        val tagKey: TagKey<Item> = part.itemTagKey(entry) ?: continue
                        val fuelScale: Fraction = part[HTPartPropertyKeys.FUEL_SCALE] ?: continue
                        val fuelTime: Int = (baseTime * fuelScale).toInt()
                        add(tagKey, FurnaceFuel(fuelTime))
                    }
                    // Item
                    for ((part: HTPart, _) in registered.items.column(entry)) {
                        val tagKey: TagKey<Item> = part.itemTagKey(entry) ?: continue
                        val fuelScale: Fraction = part[HTPartPropertyKeys.FUEL_SCALE] ?: continue
                        val fuelTime: Int = (baseTime * fuelScale).toInt()
                        add(tagKey, FurnaceFuel(fuelTime))
                    }
                }
            }
        })
        // Loot Table
        executor.accept { _, sink: ResourceSink ->
            // Moonlightが生成時点でレジストリを参照できないのでこの世の終わりみたいな文字列を書くことになった
            // GTCEu Modernをいい感じに参考にしたらなんとかなるんかなこれ
            // それかJSONビルダー作って真面目に書くか
            registered.blocks.forEach { (part: HTPart, key: HTMaterialKey, block: SimpleSupplierWithKey<Block>) ->
                if (HTPartPropertyKeys.IS_ORE in part) {
                    val raw: HTIdLike = registered.items[CommonParts.RAW, key] ?: return@forEach
                    val id: ResourceLocation = block.getId()
                    sink.addBytes(
                        id,
                        """
                                {
                                  "type": "minecraft:block",
                                  "pools": [
                                    {
                                      "bonus_rolls": 0.0,
                                      "entries": [
                                        {
                                          "type": "minecraft:alternatives",
                                          "children": [
                                            {
                                              "type": "minecraft:item",
                                              "conditions": [
                                                {
                                                  "condition": "minecraft:match_tool",
                                                  "predicate": {
                                                    "predicates": {
                                                      "minecraft:enchantments": [
                                                        {
                                                          "enchantments": "minecraft:silk_touch",
                                                          "levels": {
                                                            "min": 1
                                                          }
                                                        }
                                                      ]
                                                    }
                                                  }
                                                }
                                              ],
                                              "name": "$id"
                                            },
                                            {
                                              "type": "minecraft:item",
                                              "functions": [
                                                {
                                                  "enchantment": "minecraft:fortune",
                                                  "formula": "minecraft:ore_drops",
                                                  "function": "minecraft:apply_bonus"
                                                },
                                                {
                                                  "function": "minecraft:explosion_decay"
                                                }
                                              ],
                                              "name": "${raw.getId()}"
                                            }
                                          ]
                                        }
                                      ],
                                      "rolls": 1.0
                                    }
                                  ],
                                  "random_sequence": "${block.blockId}"
                                }
                        """.trimIndent().toByteArray(),
                        ResType.BLOCK_LOOT_TABLES,
                    )
                } else {
                    sink.addSimpleBlockLootTable(block.get())
                }
            }
        }
        // Tag
        executor.accept(object : HTTagsProvider.GenTask<Block>(Registries.BLOCK) {
            override fun appendTags() {
                // Material Block
                existing.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: SimpleSupplierWithKey<Block>) ->
                    tags(prefix, key).add(block)
                }
                registered.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: SimpleSupplierWithKey<Block>) ->
                    tags(prefix, key).add(block)
                    builder(BlockTags.MINEABLE_WITH_PICKAXE).add(block)
                }
            }
        })
        val fluids: HTSimpleMaterialContents<HTFluidPart, Fluid> = HiiragiCoreAccess.INSTANCE.registeredFluids
        executor.accept(object : HTTagsProvider.GenTask<Fluid>(Registries.FLUID) {
            override fun appendTags() {
                fluids.forEach { (part: HTFluidPart, key: HTMaterialKey, fluid: SimpleSupplierWithKey<Fluid>) ->
                    builder(part.createTagKey(key)).add(fluid)
                }
            }
        })
        executor.accept(object : HTTagsProvider.GenTask<Item>(Registries.ITEM) {
            override fun appendTags() {
                // Material Block
                existing.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: SimpleSupplierWithKey<Block>) ->
                    // factory.addMaterial(prefix, key).add(block) TODO
                }
                registered.blocks.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: SimpleSupplierWithKey<Block>) ->
                    // factory.addMaterial(prefix, key).add(block) TODO
                }
                // Material Fluid
                fluids.forEach { (part: HTFluidPart, key: HTMaterialKey, fluid: HTMaterialContents.SimpleEntry<Fluid>) ->
                    tags(Tags.Items.BUCKETS, part.createBucketTag(key)).add(fluid.get().bucket.toLike())
                }
                // Material Item
                existing.items.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: SimpleSupplierWithKey<Item>) ->
                    tags(prefix, key).add(item)
                }
                registered.items.prefixEntries.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: SimpleSupplierWithKey<Item>) ->
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
                registered.tools.forEach { (toolType: HTToolType, _, tool: SimpleSupplierWithKey<Item>) ->
                    toolType.toolTags.map(::builder).forEach { it.add(tool) }
                }
            }
        })
    }
}
