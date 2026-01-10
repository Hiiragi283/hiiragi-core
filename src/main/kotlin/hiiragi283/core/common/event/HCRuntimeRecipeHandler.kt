package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getStorageAttribute
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterialPrefixes
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        crushToDust(event, HCMaterialPrefixes.ORE) { 2 }
        crushToDust(event, HCMaterialPrefixes.STORAGE_BLOCK) { it.getStorageAttribute().baseCount }
        crushToDust(event, HCMaterialPrefixes.STORAGE_BLOCK_RAW) { 12 }

        crushToDust(event, HCMaterialPrefixes.FUEL) { 1 }
        crushToDust(event, HCMaterialPrefixes.GEAR) { 4 }
        crushToDust(event, HCMaterialPrefixes.GEM) { 1 }
        crushToDust(event, HCMaterialPrefixes.INGOT) { 1 }
        crushToDust(event, HCMaterialPrefixes.PLATE) { 1 }
    }

    @JvmStatic
    private fun crushToDust(event: HTRegisterRuntimeRecipeEvent, prefix: HTPrefixLike, outputCountGetter: (HTMaterialDefinition) -> Int?) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            val outputCount: Int = outputCountGetter(definition) ?: continue
            val dust: Item = event.getFirstHolder(HCMaterialPrefixes.DUST, key)?.value() ?: continue

            if (!event.isPresentTag(prefix, key)) continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(event.itemCreator.fromTagKey(prefix, key), event.itemResult.create(dust, outputCount))
                .saveSuffixed(event.output, "_from_${prefix.asPrefixName()}")
        }
    }
}
