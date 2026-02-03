package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmithingRecipeProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSmithingRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HTMaterialRecipeProvider(modId: String) : HTSubRecipeProvider.Direct(modId) {
    override fun buildRecipeInternal() {
        tool()
    }

    //    Tool    //

    private fun tool() {
        for (entry: HTMaterialManager.Entry in materialManager) {
            val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: continue
            for ((toolType: HTToolType, tool: HTItemHolderLike<*>) in HiiragiCoreAccess.INSTANCE.materialContents.getToolMap(entry)) {
                if (tool.namespace != modId) continue
                val smithingProperty: HTSmithingRecipeProperty? = entry[HTMaterialPropertyKeys.SMITHING_RECIPE]
                if (smithingProperty != null) {
                    // Smithing
                    val (template: HTItemHolderLike<*>, base: HTMaterialKey) = smithingProperty
                    val baseTool: ItemLike = HiiragiCoreAccess.INSTANCE.getToolOrVanilla(toolType, base) ?: continue
                    HTSmithingRecipeBuilder.create(output) {
                        this.template += template
                        this.base += baseTool
                        this.addition += inputTag
                        this.resultStack += tool
                    }
                }
                if (smithingProperty?.allowCrafting ?: true) {
                    // Shaped
                    HTShapedRecipeBuilder.create(output) {
                        pattern(toolType.recipePattern)
                        define('A') += inputTag
                        define('B') += Tags.Items.RODS_WOODEN
                        resultStack += tool
                    }
                }
            }
        }
    }
}
