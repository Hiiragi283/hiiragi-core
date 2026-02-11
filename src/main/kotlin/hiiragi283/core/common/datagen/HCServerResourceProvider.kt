package hiiragi283.core.common.datagen

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.map.HTDataMapGenTask
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.times
import net.mehvahdjukaar.moonlight.api.resources.ResType
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

data object HCServerResourceProvider : HTDynamicResourceProvider.Server(HiiragiCoreAPI.MOD_ID) {
    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        HTDynamicResourceProvider.addMaterialIds(this::addSupportedNamespaces)

        val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents
        val materialManager: HTMaterialManager = HiiragiCoreAccess.INSTANCE.materialManager
        // Data Map
        executor.accept(object : HTDataMapGenTask<FurnaceFuel, Item>(NeoForgeDataMaps.FURNACE_FUELS) {
            override fun gather() {
                for (entry: HTMaterialManager.Entry in materialManager) {
                    val baseTime: Int = entry[HTMaterialPropertyKeys.FUEL_TIME] ?: continue
                    // Block
                    for ((prefix: HTTagPrefix, _) in contents.getBlockMap(entry)) {
                        val fuelScale: Fraction = prefix[HTTagPropertyKeys.FUEL_SCALE] ?: continue
                        val fuelTime: Int = (baseTime * fuelScale).toInt()
                        add(prefix.itemTagKey(entry), FurnaceFuel(fuelTime))
                    }
                    // Item
                    for ((prefix: HTTagPrefix, _) in contents.getItemMap(entry)) {
                        val fuelScale: Fraction = prefix[HTTagPropertyKeys.FUEL_SCALE] ?: continue
                        val fuelTime: Int = (baseTime * fuelScale).toInt()
                        add(prefix.itemTagKey(entry), FurnaceFuel(fuelTime))
                    }
                }
            }
        })
        // Loot Table
        executor.accept { _, sink: ResourceSink ->
            // Moonlightが生成時点でレジストリを参照できないのでこの世の終わりみたいな文字列を書くことになった
            // GTCEu Modernをいい感じに参考にしたらなんとかなるんかなこれ
            // それかJSONビルダー作って真面目に書くか
            contents.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTBlockHolderLike<*, *>) ->
                if (prefix in CommonTagPrefixes.ORES) {
                    val raw: HTIdLike = contents.getItem(CommonTagPrefixes.RAW, key) ?: return@forEach
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
                    sink.addSimpleBlockLootTable(block.asBlock())
                }
            }
        }
        // Tag
        executor.accept(object : HTTagsProvider.GenTask<Block>(Registries.BLOCK) {
            override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
                contents.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTIdLike) ->
                    addMaterial(factory, prefix, key).add(block)
                }
            }
        })
        executor.accept(object : HTTagsProvider.GenTask<Item>(Registries.ITEM) {
            override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
                // Material
                contents.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTIdLike) ->
                    addMaterial(factory, prefix, key).add(block)
                }
                contents.getItemTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: HTIdLike) ->
                    addMaterial(factory, prefix, key).add(item)
                }
                // Tool
                contents.getToolTable().forEach { (toolType: HTToolType, _, tool: HTIdLike) ->
                    toolType.toolTags.map(factory::apply).forEach { it.add(tool) }
                }
            }
        })
    }
}
