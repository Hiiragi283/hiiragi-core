package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.creator.HTFluidResultCreator
import hiiragi283.core.api.data.recipe.creator.HTIngredientCreator
import hiiragi283.core.api.data.recipe.creator.HTItemResultCreator
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import net.minecraft.core.component.DataComponents
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler {
    private lateinit var output: RecipeOutput
    private lateinit var inputCreator: HTIngredientCreator
    private lateinit var fluidResult: HTFluidResultCreator
    private lateinit var itemResult: HTItemResultCreator

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        output = event.output
        inputCreator = event.inputCreator
        fluidResult = event.fluidResult
        itemResult = event.itemResult

        crushBaseToDust(event)

        crushToDust(event, CommonTagPrefixes.ORE)
        crushToDust(event, CommonTagPrefixes.BLOCK)
        crushToDust(event, CommonTagPrefixes.RAW_BLOCK)

        crushToDust(event, CommonTagPrefixes.GEAR)
        crushToDust(event, CommonTagPrefixes.PLATE)

        flourToDough(event)
        ingotToPlate(event)
        plateToWire(event)
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
                .crushing(inputCreator.create(prefix, key), event.itemResult.create(dust, outputCount))
                .saveSuffixed(output, "_from_${prefix.name}")
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
                .crushing(inputCreator.create(inputTag), event.itemResult.create(dust))
                .saveSuffixed(output, "_from_${defaultPart.getSuffix()}")
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
                    inputCreator
                        .create(
                            false,
                            Items.POTION,
                        ) { expect(DataComponents.POTION_CONTENTS, PotionContents(Potions.WATER)) }
                        .ingredient,
                ).saveSuffixed(output, "_with_bottle")

            HTShapelessRecipeBuilder
                .create(dough, 3)
                .addIngredients(crushedPrefix, key, 3)
                .addIngredient(Tags.Items.BUCKETS_WATER)
                .saveSuffixed(output, "_with_bucket")
        }
    }

    @JvmStatic
    private fun ingotToPlate(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, _) in event.getAllMaterials()) {
            if (!event.isPresentTag(CommonTagPrefixes.INGOT, key)) continue
            val plate: Item = event.getFirstHolder(CommonTagPrefixes.PLATE, key)?.value() ?: continue
            // Crafting
            HTShapelessRecipeBuilder
                .create(plate)
                .addIngredient(CommonTagPrefixes.INGOT, key)
                .addIngredient(HiiragiCoreTags.Items.TOOLS_HAMMER)
                .saveSuffixed(output, "_from_ingot")
        }
    }

    @JvmStatic
    private fun plateToWire(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, _) in event.getAllMaterials()) {
            if (!event.isPresentTag(CommonTagPrefixes.PLATE, key)) continue
            val wire: Item = event.getFirstHolder(CommonTagPrefixes.WIRE, key)?.value() ?: continue
            // Stonecutting
            HTStonecuttingRecipeBuilder
                .create(wire)
                .addIngredient(CommonTagPrefixes.PLATE, key)
                .saveSuffixed(output, "_from_plate")
        }
    }
}
