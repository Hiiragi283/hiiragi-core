package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.getScaledAmount
import hiiragi283.core.common.data.recipe.builder.HCSingleItemRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler : HTRecipeProviderContext.Delegated() {
    override lateinit var delegated: HTRecipeProviderContext

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context

        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            crushBaseToDust(event, key, propertyMap)

            crushToDust(event, key, propertyMap, CommonTagPrefixes.ORE, 2)
            crushToDust(event, key, propertyMap, CommonTagPrefixes.BLOCK)
            crushToDust(event, key, propertyMap, CommonTagPrefixes.RAW_BLOCK)

            crushToDust(event, key, propertyMap, CommonTagPrefixes.GEAR)
            crushToDust(event, key, propertyMap, CommonTagPrefixes.PLATE)
            crushToDust(event, key, propertyMap, CommonTagPrefixes.RAW, 2)

            flourToDough(event, key, propertyMap)

            if (propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) {
                ingotToPlate(event, key, propertyMap)
                plateToWire(event, key, propertyMap)
            }
        }
    }

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)

        val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(key)
        if (inputTag == crushedPrefix.itemTagKey(key)) return

        if (!event.isPresentTag(inputTag)) return
        val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: return
        // Crushing
        HCSingleItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(inputTag)
            result = resultCreator.create(dust)
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun crushToDust(
        event: HTRegisterRuntimeRecipeEvent,
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        prefix: HTTagPrefix,
        baseScale: Int = 1,
    ) {
        val outputCount: Int = prefix.getScaledAmount(baseScale, propertyMap).toInt()

        if (!event.isPresentTag(prefix, key)) return
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: return
        // Crushing
        HCSingleItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(prefix, key)
            result = resultCreator.create(dust, outputCount)
            recipeId suffix "_from_${prefix.name}"
        }
    }

    @JvmStatic
    private fun flourToDough(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, key)) return
        val dough: Item = event.getFirstHolder(CommonTagPrefixes.DOUGH, key)?.value() ?: return
        // Shapeless
        HTShapelessRecipeBuilder.create(output) {
            ingredients += crushedPrefix to key
            ingredients += inputCreator
                .create(
                    false,
                    Items.POTION,
                ) { expect(DataComponents.POTION_CONTENTS, PotionContents(Potions.WATER)) }
                .ingredient
            resultStack += dough
            recipeId suffix "_with_bottle"
        }

        HTShapelessRecipeBuilder.create(output) {
            repeat(3) {
                ingredients += crushedPrefix to key
            }
            ingredients += Tags.Items.BUCKETS_WATER
            resultStack += dough to 3
            recipeId suffix "_with_bucket"
        }
    }

    @JvmStatic
    private fun ingotToPlate(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        if (!event.isPresentTag(CommonTagPrefixes.INGOT, key)) return
        val plate: Item = event.getFirstHolder(CommonTagPrefixes.PLATE, key)?.value() ?: return
        // Crafting
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to key
            ingredients += CommonToolTypes.HAMMER.toolTags
            resultStack += plate
            recipeId suffix "_from_ingot"
        }
    }

    @JvmStatic
    private fun plateToWire(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        if (!event.isPresentTag(CommonTagPrefixes.PLATE, key)) return
        val wire: Item = event.getFirstHolder(CommonTagPrefixes.WIRE, key)?.value() ?: return
        // Stonecutting
        HTStonecuttingRecipeBuilder.create(output) {
            ingredient += CommonTagPrefixes.PLATE to key
            resultStack += wire
            recipeId suffix "_from_plate"
        }
    }
}
