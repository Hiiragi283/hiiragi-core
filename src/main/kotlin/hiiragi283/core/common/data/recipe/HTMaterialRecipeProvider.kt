package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTAbstractMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTDeferredBlock
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class HTMaterialRecipeProvider(
    modId: String,
    private val materials: Iterable<HTAbstractMaterial>,
    private val blocks: HTMaterialTable<HTMaterialPrefix, out HTDeferredBlock<*, *>>,
    private val items: HTMaterialTable<HTMaterialPrefix, out HTItemHolderLike<*>>,
    private val itemGetter: (HTPrefixLike, HTMaterialLike) -> HTItemHolderLike<*>?,
) : HTSubRecipeProvider.Direct(modId) {
    override fun buildRecipeInternal() {
        baseToBlock()

        prefixToBase(HCMaterialPrefixes.DUST, 0.35f)
        prefixToBase(HCMaterialPrefixes.RAW_MATERIAL, 0.7f)

        ingotToGear()
        ingotToNugget()
    }

    private fun baseToBlock() {
        for (material: HTAbstractMaterial in materials) {
            val basePrefix: HTMaterialPrefix = material.basePrefix
            val block: ItemLike = blocks[HCMaterialPrefixes.STORAGE_BLOCK, material] ?: continue
            val base: ItemLike = itemGetter(basePrefix, material) ?: continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, 9)
                .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK, material)
                .save(output, HiiragiCoreAPI.id(material.asMaterialName(), "${basePrefix.name}_from_block"))
            // Shaped
            HTShapedRecipeBuilder
                .create(block)
                .hollow8()
                .define('A', basePrefix, material)
                .define('B', base)
                .save(output, HiiragiCoreAPI.id(material.asMaterialName(), "block_from_${basePrefix.name}"))
        }
    }

    private fun prefixToBase(prefix: HTPrefixLike, exp: Float) {
        for (material: HTAbstractMaterial in materials) {
            val basePrefix: HTMaterialPrefix = material.basePrefix
            if (material.basePrefix == HCMaterialPrefixes.FUEL || material.basePrefix == HCMaterialPrefixes.DUST) continue

            val smelted: HTAbstractMaterial = material.getSmeltedMaterial() ?: continue
            val base: HTItemHolderLike<*> = itemGetter(basePrefix, smelted) ?: continue
            val input: HTItemHolderLike<*> = itemGetter(prefix, material) ?: continue
            if (base.getNamespace() == HTConst.MINECRAFT && input.getNamespace() == HTConst.MINECRAFT) continue

            // Smelting & Blasting
            HTCookingRecipeBuilder.smeltingAndBlasting(base) {
                addIngredient(input)
                setExp(exp)
                save(
                    output,
                    HiiragiCoreAPI.id(
                        smelted.asMaterialName(),
                        "${basePrefix.asPrefixName()}_from_${prefix.asPrefixName()}",
                    ),
                )
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
