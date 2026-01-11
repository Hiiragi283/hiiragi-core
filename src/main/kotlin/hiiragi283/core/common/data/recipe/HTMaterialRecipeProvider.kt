package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTDeferredBlock
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HTMaterialRecipeProvider(
    modId: String,
    private val blocks: HTMaterialTable<HTMaterialPrefix, out HTDeferredBlock<*, *>>,
    private val items: HTMaterialTable<HTMaterialPrefix, out HTItemHolderLike<*>>,
    private val itemGetter: (HTPrefixLike, HTMaterialLike) -> HTItemHolderLike<*>?,
) : HTSubRecipeProvider.Direct(modId) {
    private val manager: HTMaterialManager = HTMaterialManager.INSTANCE

    override fun buildRecipeInternal() {
        baseToBlock()

        prefixToBase(HCMaterialPrefixes.DUST, 0.35f)
        prefixToBase(HCMaterialPrefixes.RAW_MATERIAL, 0.7f)

        ingotToGear()
        ingotToNugget()
    }

    private fun baseToBlock() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in manager.entries) {
            val basePrefix: HTMaterialPrefix = propertyMap.getDefaultPart() ?: continue
            val blockProperty: HTStorageBlockProperty = propertyMap.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)

            val block: ItemLike = blocks[HCMaterialPrefixes.STORAGE_BLOCK, key] ?: continue
            val base: ItemLike = itemGetter(basePrefix, key) ?: continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, blockProperty.baseCount)
                .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK, key)
                .save(output, HiiragiCoreAPI.id(key.name, "${basePrefix.name}_from_block"))
            // Shaped
            HTShapedRecipeBuilder
                .create(block)
                .pattern(blockProperty.pattern)
                .define('A', basePrefix, key)
                .define('B', base)
                .save(output, HiiragiCoreAPI.id(key.name, "block_from_${basePrefix.name}"))
        }
    }

    private fun prefixToBase(prefix: HTPrefixLike, exp: Float) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in manager.entries) {
            val smeltingAttribute: HTSmeltingMaterialProperty = propertyMap[HTMaterialPropertyKeys.SMELTING]
                ?: propertyMap.getDefaultPart()?.let { HTSmeltingMaterialProperty.withBlasting(it, key) } ?: continue
            val smeltedPrefix: HTMaterialPrefix = smeltingAttribute.prefix ?: continue
            val smeltedKey: HTMaterialKey = smeltingAttribute.key ?: continue
            // 精錬の前後で同じプレフィックスと素材になる場合はパス
            if (prefix.isOf(smeltedPrefix) && key == smeltedKey) continue
            val result: HTItemHolderLike<*> = itemGetter(smeltedPrefix, smeltedKey) ?: continue
            val input: HTItemHolderLike<*> = itemGetter(prefix, key) ?: continue
            // 精錬の前後がどちらもバニラ由来の場合はパス
            if (result.getNamespace() == HTConst.MINECRAFT && input.getNamespace() == HTConst.MINECRAFT) continue
            // Smelting
            val path: String = when {
                key == smeltedKey -> "${smeltedPrefix.name}_from_${prefix.asPrefixName()}"
                else -> "${smeltedPrefix.name}_from_${prefix.createPath(key)}"
            }
            val id: ResourceLocation = HiiragiCoreAPI.id(smeltedKey.name, path)
            HTCookingRecipeBuilder
                .smelting(result)
                .addIngredient(input)
                .setExp(exp)
                .save(output, id)
            // Blasting
            if (smeltingAttribute.isBlasting) {
                HTCookingRecipeBuilder
                    .blasting(result)
                    .addIngredient(input)
                    .setTime(100)
                    .setExp(exp)
                    .save(output, id)
            }
            // Smoking
            if (smeltingAttribute.isSmoking) {
                HTCookingRecipeBuilder
                    .smoking(result)
                    .addIngredient(input)
                    .setTime(100)
                    .setExp(exp)
                    .save(output, id)
            }
        }
    }

    private fun ingotToGear() {
        for ((key: HTMaterialKey, gear: HTItemHolderLike<*>) in items.row(HCMaterialPrefixes.GEAR)) {
            // Shaped
            HTShapedRecipeBuilder
                .create(gear)
                .hollow4()
                .define('A', HCMaterialPrefixes.INGOT, key)
                .define('B', Tags.Items.NUGGETS_IRON)
                .save(output, HiiragiCoreAPI.id(key.name, "gear"))
        }
    }

    fun ingotToNugget() {
        for ((key: HTMaterialKey, nugget: HTItemHolderLike<*>) in items.row(HCMaterialPrefixes.NUGGET)) {
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
