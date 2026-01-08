package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.attribute.HTSmeltingMaterialAttribute
import hiiragi283.core.api.material.attribute.HTStorageBlockMaterialAttribute
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.getDefaultPrefix
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
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
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in manager.entries) {
            val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: continue
            val storageSize: HTStorageBlockMaterialAttribute = 
                definition.get<HTStorageBlockMaterialAttribute>() ?: HTStorageBlockMaterialAttribute.THREE_BY_THREE
            
            val block: ItemLike = blocks[HCMaterialPrefixes.STORAGE_BLOCK, key] ?: continue
            val base: ItemLike = itemGetter(basePrefix, key) ?: continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, storageSize.baseCount)
                .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK, key)
                .save(output, HiiragiCoreAPI.id(key.name, "${basePrefix.name}_from_block"))
            // Shaped
            HTShapedRecipeBuilder
                .create(block)
                .pattern(storageSize.pattern)
                .define('A', basePrefix, key)
                .define('B', base)
                .save(output, HiiragiCoreAPI.id(key.name, "block_from_${basePrefix.name}"))
        }
    }

    private fun prefixToBase(prefix: HTPrefixLike, exp: Float) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in manager.entries) {
            val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: continue
            if (basePrefix == prefix) continue

            val smeltingAttribute: HTSmeltingMaterialAttribute =
                definition.get<HTSmeltingMaterialAttribute>() ?: HTSmeltingMaterialAttribute.withBlasting(key)
            val smelted: HTMaterialKey = smeltingAttribute.key ?: continue
            val base: HTItemHolderLike<*> = itemGetter(basePrefix, smelted) ?: continue
            val input: HTItemHolderLike<*> = itemGetter(prefix, key) ?: continue
            if (base.getNamespace() == HTConst.MINECRAFT && input.getNamespace() == HTConst.MINECRAFT) continue

            // Smelting
            val id: ResourceLocation = HiiragiCoreAPI.id(
                smelted.name,
                "${basePrefix.asPrefixName()}_from_${prefix.asPrefixName()}",
            )
            HTCookingRecipeBuilder
                .smelting(base)
                .addIngredient(input)
                .setExp(exp)
                .save(output, id)
            // Blasting
            if (smeltingAttribute.isBlasting) {
                HTCookingRecipeBuilder
                    .blasting(base)
                    .addIngredient(input)
                    .setTime(100)
                    .setExp(exp)
                    .save(output, id)
            }
            // Smoking
            if (smeltingAttribute.isSmoking) {
                HTCookingRecipeBuilder
                    .smoking(base)
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
