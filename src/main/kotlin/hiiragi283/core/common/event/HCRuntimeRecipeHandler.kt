package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTResultHelper
import hiiragi283.core.api.data.recipe.ingredient.HTIngredientAccess
import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler {
    private val itemCreator: HTItemIngredientCreator by lazy { HTIngredientAccess.INSTANCE.itemCreator() }
    private val resultHelper: HTResultHelper = HTResultHelper

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        baseToDust(event)
        rawToDust(event)
    }

    @JvmStatic
    private fun baseToDust(event: HTRegisterRuntimeRecipeEvent) {
        for (material: HCMaterial in HCMaterial.entries) {
            val dust: Item = event.getFirstHolder(HCMaterialPrefixes.DUST, material)?.value() ?: continue
            val ingredient: TagKey<Item> = material.getBaseIngredient()
            if (ingredient == HCMaterialPrefixes.DUST.itemTagKey(material)) continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(itemCreator.fromTagKey(ingredient), resultHelper.item(dust))
                .setExp(0.1f)
                .saveSuffixed(event.output, "_from_base")
        }
    }

    @JvmStatic
    fun rawToDust(event: HTRegisterRuntimeRecipeEvent) {
        for (material: HCMaterial in HCMaterial.entries) {
            if (!event.isPresentTag(HCMaterialPrefixes.STORAGE_BLOCK_RAW, material)) continue

            val dust: Item = event.getFirstHolder(HCMaterialPrefixes.DUST, material)?.value() ?: continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(
                    itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK_RAW, material),
                    resultHelper.item(dust, 12),
                ).setExp(1.2f)
                .saveSuffixed(event.output, "_from_raw_block")
        }
    }
}
