package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        crushToDust(event, CommonTagPrefixes.ORE)
        crushToDust(event, CommonTagPrefixes.BLOCK)
        crushToDust(event, CommonTagPrefixes.RAW_BLOCK)

        crushToDust(event, CommonTagPrefixes.FUEL)
        crushToDust(event, CommonTagPrefixes.GEAR)
        crushToDust(event, CommonTagPrefixes.GEM)
        crushToDust(event, CommonTagPrefixes.INGOT)
        crushToDust(event, CommonTagPrefixes.PLATE)
    }

    @JvmStatic
    private fun crushToDust(event: HTRegisterRuntimeRecipeEvent, prefix: HTTagPrefix) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val outputCount: Int = prefix.getOrDefault(HTTagPropertyKeys.ITEM_SCALE)(1, propertyMap)
            val dust: Item = event.getFirstHolder(CommonTagPrefixes.DUST, key)?.value() ?: continue

            if (!event.isPresentTag(prefix, key)) continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(event.itemCreator.fromTagKey(prefix, key), event.itemResult.create(dust, outputCount))
                .saveSuffixed(event.output, "_from_${prefix.name}")
        }
    }
}
