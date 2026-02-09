package hiiragi283.core.common.datagen.loot

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTServerResourceGenTask
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import net.mehvahdjukaar.moonlight.api.resources.ResType
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.resources.ResourceLocation

object HCMaterialBlockLootProvider : HTServerResourceGenTask {
    override fun accept(sink: ResourceSink) {
        // Moonlightが生成時点でレジストリを参照できないのでこの世の終わりみたいな文字列を書くことになった
        // GTCEu Modernをいい感じに参考にしたらなんとかなるんかなこれ
        // それかJSONビルダー作って真面目に書くか
        val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents
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
}
