package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.part.property.getScaledAmount
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmithingRecipeProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.material.property.getDefaultScale
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSmithingRecipeBuilder
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRuntimeRecipeHandler : HTRecipeProviderContext.Delegated() {
    override lateinit var delegated: HTRecipeProviderContext

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context

        for (entry: HTMaterialManager.Entry in materialManager) {
            crushBaseToDust(event, entry)
            crushOreToCrushed(event, entry, CommonParts.ORE)
            crushOreToCrushed(event, entry, CommonParts.RAW)
            crushCrushedToDust(event, entry)
            crushPrefixToDust(event, entry, CommonParts.GEAR)
            crushPrefixToDust(event, entry, CommonParts.NUGGET)
            crushPrefixToDust(event, entry, CommonParts.PLATE)
            crushPrefixToDust(event, entry, CommonParts.ROD)
            crushPrefixToDust(event, entry, CommonParts.WIRE)

            baseToBlock(event, entry)
            ingotToNugget(entry)
            rawToBlock(event, entry)
            tinyToFuel(event, entry)
            tool(entry)

            val hardness: HTMaterialLevel = entry.getOrDefault(HTMaterialPropertyKeys.HARDNESS)
            if (hardness > HTMaterialLevel.NONE && hardness <= HTMaterialLevel.MEDIUM) {
                baseToGear(event, entry)
            }

            smeltDustToIngot(entry)
            smeltOresToBase(entry)
            smeltOreToBase(CommonParts.CRUSHED_ORE, entry)
            smeltOreToBase(CommonParts.RAW, entry)
        }
    }

    @JvmStatic
    fun getTimeFromHardness(map: HTPropertyMap, time: Int = 20 * 10): Int? = (map.getOrDefault(HTMaterialPropertyKeys.HARDNESS) * time)?.toInt()

    @JvmStatic
    fun getTimeFromMelting(map: HTPropertyMap, time: Int = 20 * 10): Int? = (map.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT) * time)?.toInt()

    @JvmStatic
    fun getBlueprint(prefix: HTTagPrefix): Ingredient = when (prefix) {
        CommonTagPrefixes.DUST -> 0
        CommonTagPrefixes.INGOT -> 1
        CommonTagPrefixes.GEM -> 2
        CommonTagPrefixes.PEARL -> 3
        CommonTagPrefixes.GEAR -> 4
        CommonTagPrefixes.PLATE -> 5
        CommonTagPrefixes.ROD -> 6
        CommonTagPrefixes.WIRE -> 7
        else -> error("Cannot define blueprint for prefix: $prefix")
    }.let(::HTBluePrintIngredient).toVanilla()

    //    Crushing    //

    @JvmStatic
    private fun crushPrefixToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 材料が存在するか判定
        if (prefix.itemTagKey(entry) == entry.getDefaultPart(entry)) return
        if (!event.isPresentTag(prefix, entry)) return
        // 素材のプロパティから完成品を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART).tagPrefix ?: return
        val dust: HTItemHolderLike<*> = event.getFirstHolder(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = part.getScaledAmount(entry.getDefaultScale(), entry)
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(prefix, entry, inputCount)
            results += resultCreator.create(dust, outputCount)
            time = getTimeFromHardness(entry, time) ?: return
            recipeId suffix "_from_${part.asPartName()}"
        }
    }

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART).tagPrefix ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        if (!event.isPresentTag(inputTag)) return
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == crushedPrefix.itemTagKey(entry)) return
        // 完成品を取得
        val dust: HTItemHolderLike<*> = event.getFirstHolder(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = entry.getDefaultScale()
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(inputTag, inputCount)
            results += resultCreator.create(dust, outputCount)
            time = getTimeFromHardness(entry, time) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun crushOreToCrushed(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 完成品を取得
        val crushedOre: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.CRUSHED_ORE, entry) ?: return
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            // 材料
            ingredient = inputCreator.create(prefix, entry)
            // 主産物
            results += resultCreator.create(crushedOre, part.getScaledAmount(2, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(resultCreator, HTExtraOreResultMap.Phase.CRUSH_ORE)
                ?.let(results::add)

            recipeId suffix "_from_${part.asPartName()}"
        }
    }

    @JvmStatic
    private fun crushCrushedToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.CRUSHED_ORE, entry)) return
        // 完成品を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART).tagPrefix ?: return
        val dust: ItemLike = event.getFirstHolder(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = CommonParts.CRUSHED_ORE.getScaledAmount(1, entry)
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            // 材料
            ingredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry, inputCount)
            // 主産物
            results += resultCreator.create(dust, outputCount)
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(resultCreator, HTExtraOreResultMap.Phase.CRUSH_CRUSHED)
                ?.let(results::add)

            recipeId suffix "_from_crushed_ore"
        }
    }

    //    Crafting    //

    private fun getItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.ItemEntry? = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, material)

    @JvmStatic
    private fun baseToBlock(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        val blockProperty: HTStorageBlockProperty = entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)
        val (_, block: ItemLike, existingBlock: Boolean) = getItem(CommonParts.BLOCK, entry) ?: return

        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val suffix: String = defaultPart.getSuffix()
        val (_, base: ItemLike, existingBase: Boolean) = defaultPart.getItem(entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (existingBlock && existingBase) return
        // レシピを登録
        if (event.isPresentTag(CommonTagPrefixes.STORAGE_BLOCK, entry)) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += itemCreator.create(CommonTagPrefixes.STORAGE_BLOCK, entry)
                resultStack += base to blockProperty.baseCount
                recipeId replace entry.getId().withSuffix("/${suffix}_from_block")
            }
        }
        // レシピを登録
        val pattern: List<String> = blockProperty.pattern ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        if (event.isPresentTag(inputTag)) {
            HTShapedRecipeBuilder.create(output) {
                pattern(pattern)
                define('A') { itemCreator.create(inputTag) }
                define('B') { itemCreator.create(base) }
                resultStack += block
                recipeId replace entry.getId().withSuffix("/block_from_$suffix")
            }
        }
    }

    @JvmStatic
    private fun baseToGear(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        if (!event.isPresentTag(inputTag)) return

        val gear: ItemLike = event.getFirstHolder(CommonTagPrefixes.GEAR, entry) ?: return

        val smithingProperty: HTSmithingRecipeProperty? = entry[HTMaterialPropertyKeys.SMITHING_RECIPE]
        if (smithingProperty != null) {
            // レシピを登録
            val (template: HTItemHolderLike<*>, base: HTMaterialKey) = smithingProperty
            if (event.isPresentTag(CommonTagPrefixes.GEAR, base)) {
                HTSmithingRecipeBuilder.create(output) {
                    this.template = itemCreator.create(template)
                    this.base = itemCreator.create(CommonTagPrefixes.GEAR, base)
                    this.addition = itemCreator.create(inputTag)
                    this.resultStack += gear
                    recipeId replace entry.getId().withSuffix("/gear")
                }
            }
        }
        if (smithingProperty?.allowCrafting ?: true) {
            // レシピを登録
            HTShapedRecipeBuilder.create(output) {
                hollow4()
                define('A') { itemCreator.create(inputTag) }
                define('B') { itemCreator.create(Tags.Items.NUGGETS_IRON) }
                resultStack += gear
                recipeId replace entry.getId().withSuffix("/gear")
            }
        }
    }

    /*private fun flourToDough(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        val dough: ItemLike = event.getFirstHolder(CommonTagPrefixes.DOUGH, entry) ?: return
        // レシピを登録
        HTShapelessRecipeBuilder.create(output) {
            ingredients += crushedPrefix to entry
            ingredients += inputCreator
                .create(
                    false,
                    Items.POTION,
                ) { expect(DataComponents.POTION_CONTENTS, PotionContents(Potions.WATER)) }
                .unsized
            resultStack += dough
            recipeId suffix "_with_bottle"
        }
        // レシピを登録
        HTShapelessRecipeBuilder.create(output) {
            repeat(3) {
                ingredients += crushedPrefix to entry
            }
            ingredients += Tags.Items.BUCKETS_WATER
            resultStack += dough to 3
            recipeId suffix "_with_bucket"
        }
    }*/

    @JvmStatic
    private fun ingotToNugget(entry: HTMaterialManager.Entry) {
        val (_, nugget: ItemLike, existingNugget: Boolean) = getItem(CommonParts.NUGGET, entry) ?: return
        val (_, ingot: ItemLike, existingIngot: Boolean) = getItem(CommonParts.INGOT, entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (existingNugget && existingIngot) return
        // レシピを登録
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(CommonTagPrefixes.INGOT, entry)
            resultStack += nugget to 9
            recipeId replace entry.getId().withSuffix("/nugget_from_ingot")
        }
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') { itemCreator.create(CommonTagPrefixes.NUGGET, entry) }
            define('B') { itemCreator.create(nugget) }
            resultStack += ingot
            recipeId replace entry.getId().withSuffix("/ingot_from_nugget")
        }
    }

    /*private fun ingotToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        if (!event.isPresentTag(CommonTagPrefixes.INGOT, entry)) return
        val plate: ItemLike = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to entry
            ingredients += CommonToolTypes.HAMMER
            resultStack += plate
            recipeId suffix "_from_ingot"
        }
    }*/

    @JvmStatic
    private fun rawToBlock(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        val (_, raw: ItemLike, existingRaw: Boolean) = getItem(CommonParts.RAW, entry) ?: return
        val (_, rawBlock: ItemLike, existingRawBlock: Boolean) = getItem(CommonParts.RAW_BLOCK, entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (existingRaw && existingRawBlock) return
        // レシピを登録
        if (event.isPresentTag(CommonTagPrefixes.RAW_STORAGE_BLOCK, entry)) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += itemCreator.create(CommonTagPrefixes.RAW_STORAGE_BLOCK, entry)
                resultStack += raw to 9
                recipeId replace entry.getId().withSuffix("/raw_from_block")
            }
        }
        if (event.isPresentTag(CommonTagPrefixes.RAW_MATERIALS, entry)) {
            HTShapedRecipeBuilder.create(output) {
                hollow8()
                define('A') { itemCreator.create(CommonTagPrefixes.RAW_MATERIALS, entry) }
                define('B') { itemCreator.create(raw) }
                resultStack += rawBlock
                recipeId replace entry.getId()
            }
        }
    }

    @JvmStatic
    private fun tinyToFuel(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        val (_, tiny: ItemLike, existingTiny: Boolean) = getItem(CommonParts.TINY, entry) ?: return
        val (_, fuel: ItemLike, existingFuel: Boolean) = getItem(CommonParts.FUEL, entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (existingTiny && existingFuel) return
        // レシピを登録
        if (event.isPresentTag(CommonTagPrefixes.FUEL, entry)) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += itemCreator.create(CommonTagPrefixes.FUEL, entry)
                resultStack += tiny to 8
                recipeId replace entry.getId().withSuffix("/tiny_from_fuel")
            }
        }
        if (event.isPresentTag(CommonTagPrefixes.TINY, entry)) {
            HTShapedRecipeBuilder.create(output) {
                hollow()
                define('A') { itemCreator.create(CommonTagPrefixes.TINY, entry) }
                resultStack += fuel
                recipeId replace entry.getId()
            }
        }
    }

    @JvmStatic
    private fun tool(entry: HTMaterialManager.Entry) {
        val existing: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> =
            HiiragiCoreAccess.INSTANCE.existingContents.tools
        val registered: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> =
            HiiragiCoreAccess.INSTANCE.registeredContents.tools

        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        for ((toolType: HTToolType, tool: HTSimpleItemHolderLike) in registered.column(entry)) {
            val smithingProperty: HTSmithingRecipeProperty? = entry[HTMaterialPropertyKeys.SMITHING_RECIPE]
            if (smithingProperty != null) {
                // Smithing
                val (template: HTItemHolderLike<*>, base: HTMaterialKey) = smithingProperty
                val baseTool: HTSimpleItemHolderLike = existing[toolType, base] ?: registered[toolType, base] ?: continue
                HTSmithingRecipeBuilder.create(output) {
                    this.template = itemCreator.create(template)
                    this.base = itemCreator.create(baseTool.get())
                    this.addition = itemCreator.create(inputTag)
                    this.resultStack += tool.get()
                }
            }
            if (smithingProperty?.allowCrafting ?: true) {
                // Shaped
                HTShapedRecipeBuilder.create(output) {
                    pattern(toolType.recipePattern)
                    define('A') { itemCreator.create(inputTag) }
                    define('B') { itemCreator.create(Tags.Items.RODS_WOODEN) }
                    resultStack += tool.get()
                }
            }
        }
    }

    //    Smelting    //

    @JvmStatic
    private fun smeltDustToIngot(entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val (_, dust: ItemLike, existingDust: Boolean) = getItem(CommonParts.DUST, entry) ?: return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val (_, ingot: ItemLike, existingIngot: Boolean) = getItem(CommonParts.INGOT, smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        if (existingDust && existingIngot) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient = itemCreator.create(dust)
            resultStack += ingot
            exp = 0.35f
            recipeId suffix "_from_${CommonParts.DUST.createId(entry).path}"
        }
    }

    @JvmStatic
    private fun smeltOresToBase(entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val smeltedPropertyMap: HTPropertyMap = materialManager[smeltedMaterial] ?: return
        val (_, base: ItemLike, _) = smeltedPropertyMap.getDefaultPart()?.getItem(smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        val oreEntries: List<HTMaterialContents.ItemEntry> =
            partManager.values
                .filter { HTPartPropertyKeys.IS_ORE in it }
                .mapNotNull { getItem(it, entry) }
                .filterNot(HTMaterialContents.Entry<*>::isBuiltIn)
        if (oreEntries.isEmpty()) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient = itemCreator.create(oreEntries)
            resultStack += base to smeltedPropertyMap.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER).toInt()
            exp = 0.7f
            recipeId suffix "_from_${CommonParts.ORE.asPartName()}"
        }
    }

    @JvmStatic
    private fun smeltOreToBase(part: HTPartLike, entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val (_, ore: ItemLike, existingOre: Boolean) = getItem(part, entry) ?: return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val smeltedPropertyMap: HTPropertyMap = materialManager[smeltedMaterial] ?: return
        val(_, base: ItemLike, existingBase: Boolean) = smeltedPropertyMap.getDefaultPart()?.getItem(smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        if (existingOre && existingBase) return
        // Smelting & Blasting
        when (entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)) {
            HTMaterialLevel.NONE -> return
            HTMaterialLevel.LOW -> HTCookingRecipeBuilder::smeltingAndBlasting
            HTMaterialLevel.MEDIUM -> HTCookingRecipeBuilder::smeltingAndBlasting
            HTMaterialLevel.HIGH -> HTCookingRecipeBuilder::blasting
            HTMaterialLevel.HIGHEST -> return
        }(output) {
            ingredient = itemCreator.create(ore)
            resultStack += base to smeltedPropertyMap.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER).toInt()
            exp = 0.7f
            recipeId suffix "_from_${part.asPartName()}"
        }
    }

    @JvmStatic
    private inline fun registerSmelting(entry: HTMaterialManager.Entry, builderAction: HTCookingRecipeBuilder.() -> Unit) {
        when (entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)) {
            HTMaterialLevel.NONE -> return
            HTMaterialLevel.LOW -> HTCookingRecipeBuilder.smeltingAndBlasting(output, builderAction)
            HTMaterialLevel.MEDIUM -> HTCookingRecipeBuilder.smeltingAndBlasting(output, builderAction)
            HTMaterialLevel.HIGH -> HTCookingRecipeBuilder.blasting(output, builderAction)
            HTMaterialLevel.HIGHEST -> return
        }
    }
}
