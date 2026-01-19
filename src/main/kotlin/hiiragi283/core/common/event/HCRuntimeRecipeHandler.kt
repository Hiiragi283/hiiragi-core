package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.buildDataPredicate
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

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

        flourToDough(event)
    }

    @JvmStatic
    private fun crushToDust(event: HTRegisterRuntimeRecipeEvent, prefix: HTTagPrefix) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val outputCount: Int = prefix.getOrDefault(HTTagPropertyKeys.ITEM_SCALE)(1, propertyMap)

            if (!event.isPresentTag(prefix, key)) continue
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(event.itemCreator.fromTagKey(prefix, key), event.itemResult.create(dust, outputCount))
                .saveSuffixed(event.output, "_from_${prefix.name}")
        }
    }

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)

            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue
            val inputTag: TagKey<Item> = defaultPart.getTag(key)
            if (inputTag == crushedPrefix.itemTagKey(key)) continue

            if (!event.isPresentTag(inputTag)) continue
            val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(event.itemCreator.fromTagKey(inputTag), event.itemResult.create(dust))
                .saveSuffixed(event.output, "_from_${defaultPart.getSuffix()}")
        }
    }

    @JvmStatic
    private fun flourToDough(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            if (!event.isPresentTag(crushedPrefix, key)) continue
            val dough: Item = event.getFirstHolder(CommonTagPrefixes.DOUGH, key)?.value() ?: continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(dough)
                .addIngredient(crushedPrefix, key)
                .addIngredient(
                    DataComponentIngredient.of(
                        false,
                        buildDataPredicate {
                            expect(DataComponents.POTION_CONTENTS, PotionContents(Potions.WATER))
                        },
                        Items.POTION,
                    ),
                ).saveSuffixed(event.output, "_with_bottle")

            HTShapelessRecipeBuilder
                .create(dough, 3)
                .addIngredients(crushedPrefix, key, 3)
                .addIngredient(Tags.Items.BUCKETS_WATER)
                .saveSuffixed(event.output, "_with_bucket")
        }
    }
}
