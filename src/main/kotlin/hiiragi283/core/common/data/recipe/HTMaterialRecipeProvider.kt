package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.material.property.getStorageBlock
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterialPrefixes
import net.neoforged.neoforge.common.Tags

class HTMaterialRecipeProvider(
    modId: String,
    private val blockGetter: (HTPrefixLike, HTMaterialLike) -> HTItemHolderLike<*>?,
    private val itemGetter: (HTPrefixLike, HTMaterialLike) -> HTItemHolderLike<*>?,
) : HTSubRecipeProvider.Direct(modId) {
    private val manager: HTMaterialManager = HTMaterialManager.INSTANCE

    override fun buildRecipeInternal() {
        baseToBlock()
        rawToBlock()

        prefixToBase(HCMaterialPrefixes.DUST, 0.35f)
        prefixToBase(HCMaterialPrefixes.RAW_MATERIAL, 0.7f)

        prefixToGear(HCMaterialPrefixes.GEM)
        prefixToGear(HCMaterialPrefixes.INGOT)

        ingotToNugget()
    }

    private fun baseToBlock() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in manager.entries) {
            val basePrefix: HTMaterialPrefix = propertyMap.getDefaultPart() ?: continue
            val blockProperty: HTStorageBlockProperty = propertyMap.getStorageBlock()

            val block: HTItemHolderLike<*> = blockGetter(HCMaterialPrefixes.STORAGE_BLOCK, key) ?: continue
            val base: HTItemHolderLike<*> = itemGetter(basePrefix, key) ?: continue
            if (block.getNamespace() == HTConst.MINECRAFT && base.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, blockProperty.baseCount)
                .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK, key)
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
            val raw: HTItemHolderLike<*> = itemGetter(HCMaterialPrefixes.RAW_MATERIAL, key) ?: continue
            if (raw.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(raw, 9)
                .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK_RAW, key)
                .save(output, HiiragiCoreAPI.id(key.name, "raw_from_block"))
            // Shaped
            val rawBlock: HTItemHolderLike<*> = blockGetter(HCMaterialPrefixes.STORAGE_BLOCK_RAW, key) ?: continue
            HTShapedRecipeBuilder
                .create(rawBlock)
                .hollow8()
                .define('A', HCMaterialPrefixes.RAW_MATERIAL, key)
                .define('B', raw)
                .save(output, HiiragiCoreAPI.id(key.name))
        }
    }

    private fun prefixToBase(prefix: HTPrefixLike, exp: Float) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in manager.entries) {
            val smeltingAttribute: HTSmeltingMaterialProperty = propertyMap[HTMaterialPropertyKeys.SMELTING]
                ?: propertyMap
                    .getDefaultPart()
                    ?.let {
                        // 精錬の前後で同じプレフィックスになる場合はパス
                        if (prefix.isOf(it)) return@let null
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

    private fun prefixToGear(prefix: HTPrefixLike) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE.entries) {
            if (propertyMap.getDefaultPart()?.isOf(prefix) ?: false) {
                val gear: HTItemHolderLike<*> = itemGetter(HCMaterialPrefixes.GEAR, key) ?: continue
                // Shaped
                HTShapedRecipeBuilder
                    .create(gear)
                    .hollow4()
                    .define('A', prefix, key)
                    .define('B', Tags.Items.NUGGETS_IRON)
                    .save(output, HiiragiCoreAPI.id(key.name, "gear"))
            }
        }
    }

    fun ingotToNugget() {
        for (key: HTMaterialKey in HTMaterialManager.INSTANCE.keys) {
            val nugget: HTItemHolderLike<*> = itemGetter(HCMaterialPrefixes.NUGGET, key) ?: continue
            if (nugget.getNamespace() == HTConst.MINECRAFT) continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(nugget, 9)
                .addIngredient(HCMaterialPrefixes.INGOT, key)
                .save(output, HiiragiCoreAPI.id(key.name, "nugget_from_ingot"))
            // Shaped
            val ingot: HTItemHolderLike<*> = itemGetter(HCMaterialPrefixes.INGOT, key) ?: continue
            HTShapedRecipeBuilder
                .create(ingot)
                .hollow8()
                .define('A', HCMaterialPrefixes.NUGGET, key)
                .define('B', nugget)
                .save(output, HiiragiCoreAPI.id(key.name, "ingot_from_nugget"))
        }
    }
}
