package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.getScaledAmount
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

class HTMaterialRecipeProvider(modId: String) : HTSubRecipeProvider.Direct(modId) {
    private fun getBlock(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = HiiragiCoreAccess.INSTANCE
        .getBlockOrVanilla(prefix, material)
        ?.takeIf { it.namespace == HTConst.MINECRAFT || it.namespace == modId }

    private fun getItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = HiiragiCoreAccess.INSTANCE
        .getItemOrVanilla(prefix, material)
        ?.takeIf { it.namespace == HTConst.MINECRAFT || it.namespace == modId }

    private fun getTool(toolType: HTToolType, material: HTMaterialLike): HTItemHolderLike<*>? = HiiragiCoreAccess.INSTANCE.materialContents
        .getTool(toolType, material)
        ?.takeIf { it.namespace == modId }

    override fun buildRecipeInternal() {
        material()
        tool()
    }

    //    Material    //

    private fun material() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            baseToBlock(key, propertyMap)
            rawToBlock(key, propertyMap)

            prefixToBase(key, propertyMap, CommonTagPrefixes.DUST, 0.35f)
            prefixToBase(key, propertyMap, CommonTagPrefixes.RAW, 0.7f)

            baseToGear(key, propertyMap)
            ingotToNugget(key, propertyMap)
        }
    }

    private fun baseToBlock(key: HTMaterialKey, propertyMap: HTPropertyMap) {
        val blockProperty: HTStorageBlockProperty = propertyMap.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)
        val block: HTItemHolderLike<*> = getBlock(CommonTagPrefixes.BLOCK, key) ?: return

        val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: return
        val suffix: String = defaultPart.getSuffix()
        val base: HTItemHolderLike<*> = defaultPart.getItem(key) ?: return
        if (block.namespace == HTConst.MINECRAFT && base.namespace == HTConst.MINECRAFT) return
        // Shapeless
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.BLOCK to key
            resultStack += base to blockProperty.baseCount
            recipeId replace key.getId().withSuffix("/${suffix}_from_block")
        }
        // Shaped
        val pattern: List<String> = blockProperty.pattern ?: return
        HTShapedRecipeBuilder.create(output) {
            pattern(pattern)
            define('A') += defaultPart.getTag(key)
            define('B') += base
            resultStack += block
            recipeId replace key.getId().withSuffix("/block_from_$suffix")
        }
    }

    private fun rawToBlock(key: HTMaterialKey, propertyMap: HTPropertyMap) {
        val raw: HTItemHolderLike<*> = getItem(CommonTagPrefixes.RAW, key) ?: return
        if (raw.namespace == HTConst.MINECRAFT) return
        // Shapeless
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.RAW_BLOCK to key
            resultStack += raw to 9
            recipeId replace key.getId().withSuffix("/raw_from_block")
        }
        // Shaped
        val rawBlock: HTItemHolderLike<*> = getBlock(CommonTagPrefixes.RAW_BLOCK, key) ?: return
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.RAW to key
            define('B') += raw
            resultStack += rawBlock
            recipeId replace key.getId()
        }
    }

    private fun prefixToBase(
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        prefix: HTTagPrefix,
        exp: Float,
    ) {
        val smeltingAttribute: HTSmeltingMaterialProperty = propertyMap[HTMaterialPropertyKeys.SMELTING]
            ?: propertyMap.getDefaultPart()?.let { part: HTDefaultPart ->
                // 精錬の前後で同じプレフィックスと素材になる場合はパス
                if (part is HTDefaultPart.Prefixed && part.prefix == prefix) return@let null
                part.getItem(key)?.let(HTSmeltingMaterialProperty::withBlasting)
            } ?: return
        val result: HTItemHolderLike<*> = smeltingAttribute.result ?: return
        val resultCount: Int = prefix.getScaledAmount(1, propertyMap).toInt()
        if (resultCount <= 0) return
        val input: HTItemHolderLike<*> = getItem(prefix, key) ?: return
        // 精錬の前後がどちらもバニラ由来の場合はパス
        if (result.namespace == HTConst.MINECRAFT && input.namespace == HTConst.MINECRAFT) return
        // Smelting
        HTCookingRecipeBuilder.smelting(output) {
            ingredient += input
            resultStack += result to resultCount
            this.exp = exp
            recipeId suffix "_from_${input.path}"
        }
        // Blasting
        if (smeltingAttribute.isBlasting) {
            HTCookingRecipeBuilder.blasting(output) {
                ingredient += input
                resultStack += result to resultCount
                this.exp = exp
                time = 100
                recipeId suffix "_from_${input.path}"
            }
        }
        // Smoking
        if (smeltingAttribute.isSmoking) {
            HTCookingRecipeBuilder.smoking(output) {
                ingredient += input
                resultStack += result to resultCount
                this.exp = exp
                time = 100
                recipeId suffix "_from_${input.path}"
            }
        }
    }

    private fun baseToGear(key: HTMaterialKey, propertyMap: HTPropertyMap) {
        val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: return
        val gear: HTItemHolderLike<*> = getItem(CommonTagPrefixes.GEAR, key) ?: return
        // Shaped
        HTShapedRecipeBuilder.create(output) {
            hollow4()
            define('A') += inputTag
            define('B') += Tags.Items.NUGGETS_IRON
            resultStack += gear
            recipeId replace key.getId().withSuffix("/gear")
        }
    }

    private fun ingotToNugget(key: HTMaterialKey, propertyMap: HTPropertyMap) {
        val nugget: HTItemHolderLike<*> = getItem(CommonTagPrefixes.NUGGET, key) ?: return
        if (nugget.namespace == HTConst.MINECRAFT) return
        // Shapeless
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to key
            resultStack += nugget to 9
            recipeId replace key.getId().withSuffix("/nugget_from_ingot")
        }
        // Shaped
        val ingot: HTItemHolderLike<*> = getItem(CommonTagPrefixes.INGOT, key) ?: return
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.NUGGET to key
            define('B') += nugget
            resultStack += ingot
            recipeId replace key.getId().withSuffix("/ingot_from_nugget")
        }
    }

    //    Tool    //

    private fun tool() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue
            // Sword
            getTool(CommonToolTypes.SWORD, key)?.let { sword ->
                HTShapedRecipeBuilder.create(output) {
                    pattern(
                        "A",
                        "A",
                        "B",
                    )
                    define('A') += defaultPart.getTag(key)
                    define('B') += Tags.Items.RODS_WOODEN
                    resultStack += sword
                }
            }
            // Shovel
            getTool(CommonToolTypes.SHOVEL, key)?.let { shovel ->
                HTShapedRecipeBuilder.create(output) {
                    pattern(
                        "A",
                        "B",
                        "B",
                    )
                    define('A') += defaultPart.getTag(key)
                    define('B') += Tags.Items.RODS_WOODEN
                    resultStack += shovel
                }
            }
            // Pickaxe
            getTool(CommonToolTypes.PICKAXE, key)?.let { pickaxe ->
                HTShapedRecipeBuilder.create(output) {
                    pattern(
                        "AAA",
                        " B ",
                        " B ",
                    )
                    define('A') += defaultPart.getTag(key)
                    define('B') += Tags.Items.RODS_WOODEN
                    resultStack += pickaxe
                }
            }
            // Axe
            getTool(CommonToolTypes.AXE, key)?.let { axe ->
                HTShapedRecipeBuilder.create(output) {
                    pattern(
                        "AA",
                        "AB",
                        " B",
                    )
                    define('A') += defaultPart.getTag(key)
                    define('B') += Tags.Items.RODS_WOODEN
                    resultStack += axe
                }
            }
            // Hoe
            getTool(CommonToolTypes.HOE, key)?.let { hoe ->
                HTShapedRecipeBuilder.create(output) {
                    pattern(
                        "AA",
                        " B",
                        " B",
                    )
                    define('A') += defaultPart.getTag(key)
                    define('B') += Tags.Items.RODS_WOODEN
                    resultStack += hoe
                }
            }
            // Hammer
            getTool(CommonToolTypes.HAMMER, key)?.let { hammer ->
                HTShapedRecipeBuilder.create(output) {
                    pattern(
                        " B ",
                        " B ",
                        "ABA",
                    )
                    define('A') += defaultPart.getTag(key)
                    define('B') += Tags.Items.RODS_WOODEN
                    resultStack += hammer
                }
            }
        }
    }
}
