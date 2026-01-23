package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.material.property.getStorageBlock
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

class HTMaterialRecipeProvider(modId: String) : HTSubRecipeProvider.Direct(modId) {
    private fun getBlock(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = HTMaterialContentsAccess.INSTANCE
        .getBlock(prefix, material)
        ?.takeIf { it.getNamespace() == modId }
        ?: HTMaterialContentsAccess.INSTANCE.getVanillaTable()[prefix, material.asMaterialKey()]

    private fun getItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = HTMaterialContentsAccess.INSTANCE
        .getItem(prefix, material)
        ?.takeIf { it.getNamespace() == modId }
        ?: HTMaterialContentsAccess.INSTANCE.getVanillaTable()[prefix, material.asMaterialKey()]

    private fun getTool(toolType: HTToolType, material: HTMaterialLike): HTItemHolderLike<*>? = HTMaterialContentsAccess.INSTANCE
        .getTool(toolType, material)
        ?.takeIf { it.getNamespace() == modId }

    override fun buildRecipeInternal() {
        material()
        tool()
    }

    //    Material    //

    private fun material() {
        baseToBlock()
        rawToBlock()

        prefixToBase(CommonTagPrefixes.DUST, 0.35f)
        prefixToBase(CommonTagPrefixes.RAW, 0.7f)

        baseToGear()
        ingotToNugget()
    }

    private fun baseToBlock() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            val blockProperty: HTStorageBlockProperty = propertyMap.getStorageBlock()
            val block: HTItemHolderLike<*> = getBlock(CommonTagPrefixes.BLOCK, key) ?: continue

            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue
            val suffix: String = defaultPart.getSuffix()
            val base: HTItemHolderLike<*> = defaultPart.getItem(key) ?: continue
            if (block.getNamespace() == HTConst.MINECRAFT && base.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, blockProperty.baseCount)
                .addIngredient(CommonTagPrefixes.BLOCK, key)
                .save(output, key.getId().withSuffix("/${suffix}_from_block"))
            // Shaped
            val pattern: List<String> = blockProperty.pattern ?: continue
            HTShapedRecipeBuilder
                .create(block)
                .pattern(pattern)
                .define('A', defaultPart.getTag(key))
                .define('B', base)
                .save(output, key.getId().withSuffix("/block_from_$suffix"))
        }
    }

    private fun rawToBlock() {
        for (key: HTMaterialKey in materialManager.keys) {
            val raw: HTItemHolderLike<*> = getItem(CommonTagPrefixes.RAW, key) ?: continue
            if (raw.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(raw, 9)
                .addIngredient(CommonTagPrefixes.RAW_BLOCK, key)
                .save(output, key.getId().withSuffix("/raw_from_block"))
            // Shaped
            val rawBlock: HTItemHolderLike<*> = getBlock(CommonTagPrefixes.RAW_BLOCK, key) ?: continue
            HTShapedRecipeBuilder
                .create(rawBlock)
                .hollow8()
                .define('A', CommonTagPrefixes.RAW, key)
                .define('B', raw)
                .save(output, key.getId())
        }
    }

    private fun prefixToBase(prefix: HTTagPrefix, exp: Float) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            val smeltingAttribute: HTSmeltingMaterialProperty = propertyMap[HTMaterialPropertyKeys.SMELTING]
                ?: propertyMap.getDefaultPart()?.let { part: HTDefaultPart ->
                    if (part is HTDefaultPart.Prefixed && part.prefix == prefix) return@let null
                    part.getItem(key)?.let(HTSmeltingMaterialProperty::withBlasting)
                } ?: continue
            // 精錬の前後で同じプレフィックスと素材になる場合はパス
            val result: HTItemHolderLike<*> = smeltingAttribute.result ?: continue
            val input: HTItemHolderLike<*> = getItem(prefix, key) ?: continue
            // 精錬の前後がどちらもバニラ由来の場合はパス
            if (result.getNamespace() == HTConst.MINECRAFT && input.getNamespace() == HTConst.MINECRAFT) continue
            // Smelting
            HTCookingRecipeBuilder
                .smelting(result)
                .addIngredient(input)
                .setExp(exp)
                .saveSuffixed(output, "_from_${input.getPath()}")
            // Blasting
            if (smeltingAttribute.isBlasting) {
                HTCookingRecipeBuilder
                    .blasting(result)
                    .addIngredient(input)
                    .setTime(100)
                    .setExp(exp)
                    .saveSuffixed(output, "_from_${input.getPath()}")
            }
            // Smoking
            if (smeltingAttribute.isSmoking) {
                HTCookingRecipeBuilder
                    .smoking(result)
                    .addIngredient(input)
                    .setTime(100)
                    .setExp(exp)
                    .saveSuffixed(output, "_from_${input.getPath()}")
            }
        }
    }

    private fun baseToGear() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: continue
            val gear: HTItemHolderLike<*> = getItem(CommonTagPrefixes.GEAR, key) ?: continue
            // Shaped
            HTShapedRecipeBuilder
                .create(gear)
                .hollow4()
                .define('A', inputTag)
                .define('B', Tags.Items.NUGGETS_IRON)
                .save(output, key.getId().withSuffix("/gear"))
        }
    }

    private fun ingotToNugget() {
        for (key: HTMaterialKey in materialManager.keys) {
            val nugget: HTItemHolderLike<*> = getItem(CommonTagPrefixes.NUGGET, key) ?: continue
            if (nugget.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(nugget, 9)
                .addIngredient(CommonTagPrefixes.INGOT, key)
                .save(output, key.getId().withSuffix("/nugget_from_ingot"))
            // Shaped
            val ingot: HTItemHolderLike<*> = getItem(CommonTagPrefixes.INGOT, key) ?: continue
            HTShapedRecipeBuilder
                .create(ingot)
                .hollow8()
                .define('A', CommonTagPrefixes.NUGGET, key)
                .define('B', nugget)
                .save(output, key.getId().withSuffix("/ingot_from_nugget"))
        }
    }

    //    Tool    //

    private fun tool() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue
            // Sword
            getTool(CommonToolTypes.SWORD, key)?.let { sword ->
                HTShapedRecipeBuilder
                    .create(sword)
                    .pattern(
                        "A",
                        "A",
                        "B",
                    ).define('A', defaultPart.getTag(key))
                    .define('B', Tags.Items.RODS_WOODEN)
                    .save(output)
            }
            // Shovel
            getTool(CommonToolTypes.SHOVEL, key)?.let { shovel ->
                HTShapedRecipeBuilder
                    .create(shovel)
                    .pattern(
                        "A",
                        "B",
                        "B",
                    ).define('A', defaultPart.getTag(key))
                    .define('B', Tags.Items.RODS_WOODEN)
                    .save(output)
            }
            // Pickaxe
            getTool(CommonToolTypes.PICKAXE, key)?.let { pickaxe ->
                HTShapedRecipeBuilder
                    .create(pickaxe)
                    .pattern(
                        "AAA",
                        " B ",
                        " B ",
                    ).define('A', defaultPart.getTag(key))
                    .define('B', Tags.Items.RODS_WOODEN)
                    .save(output)
            }
            // Axe
            getTool(CommonToolTypes.PICKAXE, key)?.let { axe ->
                HTShapedRecipeBuilder
                    .create(axe)
                    .pattern(
                        "AA",
                        "AB",
                        " B",
                    ).define('A', defaultPart.getTag(key))
                    .define('B', Tags.Items.RODS_WOODEN)
                    .save(output)
            }
            // Hoe
            getTool(CommonToolTypes.HOE, key)?.let { hoe ->
                HTShapedRecipeBuilder
                    .create(hoe)
                    .pattern(
                        "AA",
                        " B",
                        " B",
                    ).define('A', defaultPart.getTag(key))
                    .define('B', Tags.Items.RODS_WOODEN)
                    .save(output)
            }
            // Hammer
            getTool(CommonToolTypes.HAMMER, key)?.let { hammer ->
                HTShapedRecipeBuilder
                    .create(hammer)
                    .pattern(
                        "  B",
                        "AB ",
                        "BA ",
                    ).define('A', defaultPart.getTag(key))
                    .define('B', Tags.Items.RODS_WOODEN)
                    .save(output)
            }
        }
    }
}
