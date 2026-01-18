package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
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

class HTMaterialRecipeProvider(
    modId: String,
    private val blockGetter: (HTTagPrefix, HTMaterialLike) -> HTItemHolderLike<*>?,
    private val itemGetter: (HTTagPrefix, HTMaterialLike) -> HTItemHolderLike<*>?,
) : HTSubRecipeProvider.Direct(modId) {
    private val manager: HTMaterialManager = HTMaterialManager.INSTANCE

    override fun buildRecipeInternal() {
        baseToBlock()
        rawToBlock()

        prefixToBase(CommonTagPrefixes.DUST, 0.35f)
        prefixToBase(CommonTagPrefixes.RAW, 0.7f)

        baseToGear()
        ingotToNugget()
    }

    private fun baseToBlock() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in manager.entries) {
            val basePrefix: HTTagPrefix = propertyMap.getDefaultPart()?.getLeft() ?: continue
            val blockProperty: HTStorageBlockProperty = propertyMap.getStorageBlock()

            val block: HTItemHolderLike<*> = blockGetter(CommonTagPrefixes.BLOCK, key) ?: continue
            val base: HTItemHolderLike<*> = itemGetter(basePrefix, key) ?: continue
            if (block.getNamespace() == HTConst.MINECRAFT && base.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, blockProperty.baseCount)
                .addIngredient(CommonTagPrefixes.BLOCK, key)
                .save(output, HiiragiCoreAPI.id(key.name, "${basePrefix.name}_from_block"))
            // Shaped
            val pattern: List<String> = blockProperty.pattern ?: continue
            HTShapedRecipeBuilder
                .create(block)
                .pattern(pattern)
                .define('A', basePrefix, key)
                .define('B', base)
                .save(output, HiiragiCoreAPI.id(key.name, "block_from_${basePrefix.name}"))
        }
    }

    private fun rawToBlock() {
        for (key: HTMaterialKey in HTMaterialManager.INSTANCE.keys) {
            val raw: HTItemHolderLike<*> = itemGetter(CommonTagPrefixes.RAW, key) ?: continue
            if (raw.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(raw, 9)
                .addIngredient(CommonTagPrefixes.RAW_BLOCK, key)
                .save(output, HiiragiCoreAPI.id(key.name, "raw_from_block"))
            // Shaped
            val rawBlock: HTItemHolderLike<*> = blockGetter(CommonTagPrefixes.RAW_BLOCK, key) ?: continue
            HTShapedRecipeBuilder
                .create(rawBlock)
                .hollow8()
                .define('A', CommonTagPrefixes.RAW, key)
                .define('B', raw)
                .save(output, HiiragiCoreAPI.id(key.name))
        }
    }

    private fun prefixToBase(prefix: HTTagPrefix, exp: Float) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in manager.entries) {
            val smeltingAttribute: HTSmeltingMaterialProperty = propertyMap[HTMaterialPropertyKeys.SMELTING]
                ?: propertyMap
                    .getDefaultPart()
                    ?.getLeft()
                    ?.let {
                        // 精錬の前後で同じプレフィックスになる場合はパス
                        if (prefix == it) return@let null
                        val result: HTItemHolderLike<*> = itemGetter(it, key) ?: return@let null
                        HTSmeltingMaterialProperty.withBlasting(result)
                    }
                ?: continue
            // 精錬の前後で同じプレフィックスと素材になる場合はパス
            val result: HTItemHolderLike<*> = smeltingAttribute.result ?: continue
            val input: HTItemHolderLike<*> = itemGetter(prefix, key) ?: continue
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
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE.entries) {
            val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: continue
            val gear: HTItemHolderLike<*> = itemGetter(CommonTagPrefixes.GEAR, key) ?: continue
            // Shaped
            HTShapedRecipeBuilder
                .create(gear)
                .hollow4()
                .define('A', inputTag)
                .define('B', Tags.Items.NUGGETS_IRON)
                .save(output, HiiragiCoreAPI.id(key.name, "gear"))
        }
    }

    fun ingotToNugget() {
        for (key: HTMaterialKey in HTMaterialManager.INSTANCE.keys) {
            val nugget: HTItemHolderLike<*> = itemGetter(CommonTagPrefixes.NUGGET, key) ?: continue
            if (nugget.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(nugget, 9)
                .addIngredient(CommonTagPrefixes.INGOT, key)
                .save(output, HiiragiCoreAPI.id(key.name, "nugget_from_ingot"))
            // Shaped
            val ingot: HTItemHolderLike<*> = itemGetter(CommonTagPrefixes.INGOT, key) ?: continue
            HTShapedRecipeBuilder
                .create(ingot)
                .hollow8()
                .define('A', CommonTagPrefixes.NUGGET, key)
                .define('B', nugget)
                .save(output, HiiragiCoreAPI.id(key.name, "ingot_from_nugget"))
        }
    }
}
