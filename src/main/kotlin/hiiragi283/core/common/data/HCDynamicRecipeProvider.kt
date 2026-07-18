package hiiragi283.core.common.data

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.pack.HTDynamicDataRegister
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
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
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTItemToMultiItemRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

internal data object HCDynamicRecipeProvider : HTRecipeProviderContext.Delegated() {
    @JvmStatic
    fun initialize() {
        delegate = HTDynamicDataRegister
        for (entry: HTMaterialManager.Entry in materialManager) {
            crushBaseToDust(entry)
            crushOreToCrushed(entry, CommonParts.ORE)
            crushOreToCrushed(entry, CommonParts.RAW)
            crushCrushedToDust(entry)
            crushPrefixToDust(entry, CommonParts.GEAR)
            crushPrefixToDust(entry, CommonParts.NUGGET)
            crushPrefixToDust(entry, CommonParts.PLATE)
            crushPrefixToDust(entry, CommonParts.ROD)
            crushPrefixToDust(entry, CommonParts.WIRE)

            baseToBlock(entry)
            ingotToNugget(entry)
            rawToBlock(entry)
            tinyToFuel(entry)
            tool(entry)

            val hardness: HTMaterialLevel = entry.getOrDefault(HTMaterialPropertyKeys.HARDNESS)
            if (hardness > HTMaterialLevel.NONE && hardness <= HTMaterialLevel.MEDIUM) {
                baseToGear(entry)
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
    private fun crushPrefixToDust(entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 材料が存在するか判定
        if (prefix.itemTagKey(entry) == entry.getDefaultPart(entry)) return
        // 素材のプロパティから完成品を取得
        val crushedPrefix: HTPartLike = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        val dust: HTMaterialContents.ItemEntry = getItem(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = part.getScaledAmount(entry.getDefaultScale(), entry)
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing {
            ingredient {
                +tag(prefix, entry)
                count = inputCount
            }
            result {
                +dust
                count = outputCount
            }
            time = getTimeFromHardness(entry, time) ?: return
            recipeId suffix "_from_${part.asPartName()}"
        }.save(exporter)
    }

    @JvmStatic
    private fun crushBaseToDust(entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val crushedPrefix: HTPartLike = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == crushedPrefix.tagPrefix?.itemTagKey(entry)) return
        // 完成品を取得
        val dust: HTMaterialContents.ItemEntry = getItem(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = entry.getDefaultScale()
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing {
            ingredient {
                +inputTag
                count = inputCount
            }
            result {
                +dust
                count = outputCount
            }
            time = getTimeFromHardness(entry, time) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }.save(exporter)
    }

    @JvmStatic
    private fun crushOreToCrushed(entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 完成品を取得
        val crushedOre: HTMaterialContents.ItemEntry = getItem(CommonParts.CRUSHED_ORE, entry) ?: return
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing {
            // 材料
            ingredient { +tag(prefix, entry) }
            // 主産物
            result {
                +crushedOre
                count = part.getScaledAmount(2, entry).toInt()
            }
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]?.getResult(HTExtraOreResultMap.Phase.CRUSH_ORE)?.let { +it }

            recipeId suffix "_from_${part.asPartName()}"
        }.save(exporter)
    }

    @JvmStatic
    private fun crushCrushedToDust(entry: HTMaterialManager.Entry) {
        // 完成品を取得
        val crushedPrefix: HTPartLike = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        val dust: HTMaterialContents.ItemEntry = getItem(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = CommonParts.CRUSHED_ORE.getScaledAmount(1, entry)
        // レシピを登録
        HTItemToMultiItemRecipeBuilder.crushing {
            // 材料
            ingredient {
                +tag(CommonTagPrefixes.CRUSHED_ORE, entry)
                count = inputCount
            }
            // 主産物
            result {
                +dust
                count = outputCount
            }
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]?.getResult(HTExtraOreResultMap.Phase.CRUSH_CRUSHED)?.let { +it }

            recipeId suffix "_from_crushed_ore"
        }.save(exporter)
    }

    //    Crafting    //

    @JvmStatic
    private fun getItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.ItemEntry? = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, material)

    @JvmStatic
    private fun baseToBlock(entry: HTMaterialManager.Entry) {
        val blockProperty: HTStorageBlockProperty = entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)
        val block: HTMaterialContents.ItemEntry = getItem(CommonParts.BLOCK, entry) ?: return

        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val suffix: String = defaultPart.getSuffix()
        val base: HTMaterialContents.ItemEntry = defaultPart.getItem(entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (block.isBuiltIn && base.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, entry) }
            +base.toStack(blockProperty.baseCount)
            recipeId replace entry.getId().withSuffix("/${suffix}_from_block")
        }.save(exporter)
        // レシピを登録
        val patterns: List<String> = blockProperty.pattern ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        HTShapedRecipeBuilder.create {
            pattern(patterns)
            define('A') { +inputTag }
            define('B') { +base }
            +block.toStack()
            recipeId replace entry.getId().withSuffix("/block_from_$suffix")
        }.save(exporter)
    }

    @JvmStatic
    private fun baseToGear(entry: HTMaterialManager.Entry) {
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        val gear: HTMaterialContents.ItemEntry = getItem(CommonParts.GEAR, entry) ?: return

        val smithingProperty: HTSmithingRecipeProperty? = entry[HTMaterialPropertyKeys.SMITHING_RECIPE]
        if (smithingProperty != null) {
            // レシピを登録
            val (template: SimpleSupplierWithKey<Item>, base: HTMaterialKey) = smithingProperty
            HTSmithingRecipeBuilder.create {
                template { +template.get() }
                base { +tag(CommonTagPrefixes.GEAR, base) }
                addition { +inputTag }
                +gear.toStack()
                recipeId replace entry.getId().withSuffix("/gear")
            }
        }
        if (smithingProperty?.allowCrafting ?: true) {
            // レシピを登録
            HTShapedRecipeBuilder.create {
                hollow4()
                define('A') { +inputTag }
                define('B') { +Tags.Items.NUGGETS_IRON }
                +gear.toStack()
                recipeId replace entry.getId().withSuffix("/gear")
            }
        }
    }

    @JvmStatic
    private fun ingotToNugget(entry: HTMaterialManager.Entry) {
        val nugget: HTMaterialContents.ItemEntry = getItem(CommonParts.NUGGET, entry) ?: return
        val ingot: HTMaterialContents.ItemEntry = getItem(CommonParts.INGOT, entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (nugget.isBuiltIn && ingot.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.INGOT, entry) }
            +nugget.toStack(9)
            recipeId replace entry.getId().withSuffix("/nugget_from_ingot")
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.NUGGET, entry) }
            define('B') { +nugget }
            +ingot.toStack()
            recipeId replace entry.getId().withSuffix("/ingot_from_nugget")
        }.save(exporter)
    }

    @JvmStatic
    private fun rawToBlock(entry: HTMaterialManager.Entry) {
        val raw: HTMaterialContents.ItemEntry = getItem(CommonParts.RAW, entry) ?: return
        val rawBlock: HTMaterialContents.ItemEntry = getItem(CommonParts.RAW_BLOCK, entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (raw.isBuiltIn && rawBlock.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.RAW_STORAGE_BLOCK, entry) }
            +raw.toStack(9)
            recipeId replace entry.getId().withSuffix("/raw_from_block")
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.RAW_MATERIALS, entry) }
            define('B') { +raw }
            +rawBlock.toStack()
            recipeId replace entry.getId()
        }.save(exporter)
    }

    @JvmStatic
    private fun tinyToFuel(entry: HTMaterialManager.Entry) {
        val tiny: HTMaterialContents.ItemEntry = getItem(CommonParts.TINY, entry) ?: return
        val fuel: HTMaterialContents.ItemEntry = getItem(CommonParts.FUEL, entry) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (tiny.isBuiltIn && fuel.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.FUEL, entry) }
            +tiny.toStack(8)
            recipeId replace entry.getId().withSuffix("/tiny_from_fuel")
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow()
            define('A') { +tag(CommonTagPrefixes.TINY, entry) }
            +fuel.toStack()
            recipeId replace entry.getId()
        }.save(exporter)
    }

    @JvmStatic
    private fun tool(entry: HTMaterialManager.Entry) {
        val existing: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> =
            HiiragiCoreAccess.INSTANCE.existingContents.tools
        val registered: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> =
            HiiragiCoreAccess.INSTANCE.registeredContents.tools

        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        for ((toolType: HTToolType, tool: HTMaterialContents.ItemEntry) in registered.column(entry)) {
            val smithingProperty: HTSmithingRecipeProperty? = entry[HTMaterialPropertyKeys.SMITHING_RECIPE]
            if (smithingProperty != null) {
                // Smithing
                val (template: SimpleSupplierWithKey<Item>, base: HTMaterialKey) = smithingProperty
                val baseTool: HTMaterialContents.ItemEntry = existing[toolType, base] ?: registered[toolType, base] ?: continue
                HTSmithingRecipeBuilder.create {
                    template { +template.get() }
                    base { +baseTool }
                    addition { +inputTag }
                    +tool.toStack()
                }.save(exporter)
            }
            if (smithingProperty?.allowCrafting ?: true) {
                // Shaped
                HTShapedRecipeBuilder.create {
                    pattern(toolType.recipePattern)
                    define('A') { +inputTag }
                    define('B') { +Tags.Items.RODS_WOODEN }
                    +tool.toStack()
                }.save(exporter)
            }
        }
    }

    //    Smelting    //

    @JvmStatic
    private fun smeltDustToIngot(entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val dust: HTMaterialContents.ItemEntry = getItem(CommonParts.DUST, entry) ?: return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val ingot: HTMaterialContents.ItemEntry = getItem(CommonParts.INGOT, smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        if (dust.isBuiltIn && ingot.isBuiltIn) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient { +dust }
            +ingot.toStack()
            exp = 0.35f
            recipeId suffix "_from_${CommonParts.DUST.createId(entry).path}"
        }
    }

    @JvmStatic
    private fun smeltOresToBase(entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val smeltedPropertyMap: HTPropertyMap = materialManager[smeltedMaterial] ?: return
        val base: HTMaterialContents.ItemEntry = smeltedPropertyMap.getDefaultPart()?.getItem(smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        val oreEntries: List<HTMaterialContents.ItemEntry> =
            partManager.values
                .filter { HTPartPropertyKeys.IS_ORE in it }
                .mapNotNull { getItem(it, entry) }
                .filterNot(HTMaterialContents.ItemEntry::isBuiltIn)
        if (oreEntries.isEmpty()) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient { +oreEntries }
            +base.toStack(smeltedPropertyMap.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER).toInt())
            exp = 0.7f
            recipeId suffix "_from_${CommonParts.ORE.asPartName()}"
        }
    }

    @JvmStatic
    private fun smeltOreToBase(part: HTPartLike, entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val ore: HTMaterialContents.ItemEntry = getItem(part, entry) ?: return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val smeltedPropertyMap: HTPropertyMap = materialManager[smeltedMaterial] ?: return
        val base: HTMaterialContents.ItemEntry = smeltedPropertyMap.getDefaultPart()?.getItem(smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        if (ore.isBuiltIn && base.isBuiltIn) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient { +ore }
            +base.toStack(smeltedPropertyMap.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER).toInt())
            exp = 0.7f
            recipeId suffix "_from_${part.asPartName()}"
        }
    }

    @JvmStatic
    private inline fun registerSmelting(entry: HTMaterialManager.Entry, builderAction: HTCookingRecipeBuilder.() -> Unit) {
        when (entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)) {
            HTMaterialLevel.NONE -> emptySequence()
            HTMaterialLevel.LOW -> HTCookingRecipeBuilder.smeltingAndBlasting(builderAction)
            HTMaterialLevel.MEDIUM -> HTCookingRecipeBuilder.smeltingAndBlasting(builderAction)
            HTMaterialLevel.HIGH -> sequenceOf(HTCookingRecipeBuilder.blasting(builderAction))
            HTMaterialLevel.HIGHEST -> emptySequence()
        }.forEach { it.save(exporter) }
    }
}
