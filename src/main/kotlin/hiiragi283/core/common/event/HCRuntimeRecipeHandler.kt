package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.attribute.HTBaseIngredientMaterialAttribute
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.getDefaultPrefix
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterialPrefixes
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            baseToDust(event, key, definition)
            rawToDust(event, key, definition)
        }
    }

    @JvmStatic
    private fun baseToDust(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, definition: HTMaterialDefinition) {
        val dust: Item = event.getFirstHolder(HCMaterialPrefixes.DUST, key)?.value() ?: return
        val ingredient: TagKey<Item> = definition
            .get<HTBaseIngredientMaterialAttribute>()
            ?.ingredient
            ?: definition.getDefaultPrefix()?.itemTagKey(key)
            ?: return
        if (ingredient == HCMaterialPrefixes.DUST.itemTagKey(key)) return
        // Crushing
        HTSingleItemRecipeBuilder
            .crushing(event.itemCreator.fromTagKey(ingredient), event.itemResult.create(dust))
            .saveSuffixed(event.output, "_from_base")
    }

    @JvmStatic
    fun rawToDust(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, definition: HTMaterialDefinition) {
        if (!event.isPresentTag(HCMaterialPrefixes.STORAGE_BLOCK_RAW, key)) return

        val dust: Item = event.getFirstHolder(HCMaterialPrefixes.DUST, key)?.value() ?: return
        // Crushing
        HTSingleItemRecipeBuilder
            .crushing(
                event.itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK_RAW, key),
                event.itemResult.create(dust, 12),
            ).saveSuffixed(event.output, "_from_raw_block")
    }
}
