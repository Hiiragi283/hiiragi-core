package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        crushBaseToDust(event)

        crushToDust(event, CommonTagPrefixes.ORE)
        crushToDust(event, CommonTagPrefixes.BLOCK)
        crushToDust(event, CommonTagPrefixes.RAW_BLOCK)

        crushToDust(event, CommonTagPrefixes.GEAR)
        crushToDust(event, CommonTagPrefixes.PLATE)
    }

    @JvmStatic
    private fun crushToDust(event: HTRegisterRuntimeRecipeEvent, prefix: HTTagPrefix) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            if (propertyMap.getDefaultPart()?.getTag(key) == prefix.itemTagKey(key)) continue
            val outputCount: Int = prefix.getOrDefault(HTTagPropertyKeys.ITEM_SCALE)(1, propertyMap)

            if (!event.isPresentTag(prefix, key)) continue
            val dust: Item = event.getFirstHolder(CommonTagPrefixes.DUST, key)?.value() ?: continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(event.itemCreator.fromTagKey(prefix, key), event.itemResult.create(dust, outputCount))
                .saveSuffixed(event.output, "_from_${prefix.name}")
        }
    }

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue
            val inputTag: TagKey<Item> = defaultPart.getTag(key)
            if (inputTag == CommonTagPrefixes.DUST.itemTagKey(key)) continue

            if (!event.isPresentTag(inputTag)) continue
            val dust: Item = event.getFirstHolder(CommonTagPrefixes.DUST, key)?.value() ?: continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(event.itemCreator.fromTagKey(inputTag), event.itemResult.create(dust))
                .saveSuffixed(event.output, "_from_${defaultPart.getSuffix()}")
        }
    }
}
