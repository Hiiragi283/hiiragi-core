package hiiragi283.core.common.data

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.pack.HTDynamicDataRegister
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartKey
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.part.property.getScaledAmount
import hiiragi283.core.api.material.part.property.tagPrefix
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmithingRecipeProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.material.property.getDefaultScale
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.recipe.HCRecipeBuilders
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

data object HCDynamicRecipeProvider : HTRecipeProviderContext.Delegated() {
    @JvmStatic
    internal fun initialize() {
        delegate = HTDynamicDataRegister
        for (entry: HTMaterial in materialManager) {
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
    fun getTimeFromHardness(material: HTMaterial, time: Int = 20 * 10): Int? = (material.getOrDefault(HTMaterialPropertyKeys.HARDNESS) * time)?.toInt()

    @JvmStatic
    fun getTimeFromMelting(material: HTMaterial, time: Int = 20 * 10): Int? = (material.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT) * time)?.toInt()

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
    private fun crushPrefixToDust(entry: HTMaterial, partKey: HTPartKey) {
        val key: HTMaterialKey = entry.key
        val part: HTPart = partManager[partKey] ?: return
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 材料が存在するか判定
        if (prefix.itemTagKey(key) == entry.getDefaultPart(key)) return
        // 素材のプロパティから完成品を取得
        val crushedPrefix: HTPartKey = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = part.getScaledAmount(entry.getDefaultScale(), entry)
        // レシピを登録
        HCRecipeBuilders.crushing {
            ingredient {
                +tag(prefix, key)
                count = inputCount
            }
            result { +HTItemResult.MaterialPart(crushedPrefix, key, outputCount) }
            time = getTimeFromHardness(entry, time) ?: return
            recipeId suffix "_from_${partKey.name}"
        }.save(exporter)
    }

    @JvmStatic
    private fun crushBaseToDust(entry: HTMaterial) {
        val key: HTMaterialKey = entry.key
        // 素材のプロパティから材料を取得
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val crushedPart: HTPart = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART).let(partManager::get) ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(key)
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == crushedPart.tagPrefix?.itemTagKey(key)) return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = entry.getDefaultScale()
        // レシピを登録
        HCRecipeBuilders.crushing {
            ingredient {
                +inputTag
                count = inputCount
            }
            result { +HTItemResult.MaterialPart(crushedPart, key, outputCount) }
            time = getTimeFromHardness(entry, time) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }.save(exporter)
    }

    @JvmStatic
    private fun crushOreToCrushed(entry: HTMaterial, partKey: HTPartKey) {
        val key: HTMaterialKey = entry.key
        val part: HTPart = partManager[partKey] ?: return
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // レシピを登録
        HCRecipeBuilders.crushing {
            // 材料
            ingredient { +tag(prefix, key) }
            // 主産物
            result { +HTItemResult.MaterialPart(CommonParts.CRUSHED_ORE, key, part.getScaledAmount(2, entry).toInt()) }
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]?.getResult(HTExtraOreResultMap.Phase.CRUSH_ORE)?.let { +it }

            recipeId suffix "_from_${partKey.name}"
        }.save(exporter)
    }

    @JvmStatic
    private fun crushCrushedToDust(entry: HTMaterial) {
        val key: HTMaterialKey = entry.key
        // 完成品を取得
        val crushedPrefix: HTPartKey = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        // プレフィックスのスケールから個数を算出
        val crushedPart: HTPart = partManager[CommonParts.CRUSHED_ORE] ?: return
        val (outputCount: Int, inputCount: Int) = crushedPart.getScaledAmount(1, entry)
        // レシピを登録
        HCRecipeBuilders.crushing {
            // 材料
            ingredient {
                +tag(CommonTagPrefixes.CRUSHED_ORE, key)
                count = inputCount
            }
            // 主産物
            result { +HTItemResult.MaterialPart(crushedPrefix, key, outputCount) }
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]?.getResult(HTExtraOreResultMap.Phase.CRUSH_CRUSHED)?.let { +it }

            recipeId suffix "_from_crushed_ore"
        }.save(exporter)
    }

    //    Crafting    //

    @JvmStatic
    private fun getItem(part: HTPartKey, key: HTMaterialKey): HTMaterialContents.ItemEntry? = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, key)

    @JvmStatic
    private fun baseToBlock(entry: HTMaterial) {
        val key: HTMaterialKey = entry.key
        val blockProperty: HTStorageBlockProperty = entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)
        val block: HTMaterialContents.ItemEntry = getItem(CommonParts.BLOCK, key) ?: return

        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val suffix: String = defaultPart.getSuffix()
        val base: HTMaterialContents.ItemEntry = defaultPart.getItem(key) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (block.isBuiltIn && base.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, key) }
            +base.toStack(blockProperty.baseCount)
            recipeId suffix "/${suffix}_from_block"
        }.save(exporter)
        // レシピを登録
        val patterns: List<String> = blockProperty.pattern ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(key)
        HTShapedRecipeBuilder.create {
            pattern(patterns)
            define('A') { +inputTag }
            define('B') { +base }
            +block.toStack()
            recipeId suffix "/block_from_$suffix"
        }.save(exporter)
    }

    @JvmStatic
    private fun baseToGear(entry: HTMaterial) {
        val key: HTMaterialKey = entry.key
        val inputTag: TagKey<Item> = entry.getDefaultPart(key) ?: return
        val gear: HTMaterialContents.ItemEntry = getItem(CommonParts.GEAR, key) ?: return

        val smithingProperty: HTSmithingRecipeProperty? = entry[HTMaterialPropertyKeys.SMITHING_RECIPE]
        if (smithingProperty != null) {
            // レシピを登録
            val (template: SimpleSupplierWithKey<Item>, base: HTMaterialKey) = smithingProperty
            HTSmithingRecipeBuilder.create {
                template { +template.get() }
                base { +tag(CommonTagPrefixes.GEAR, base) }
                addition { +inputTag }
                +gear.toStack()
                recipeId suffix "/gear"
            }.save(exporter)
        }
        if (smithingProperty?.allowCrafting ?: true) {
            // レシピを登録
            HTShapedRecipeBuilder.create {
                hollow4()
                define('A') { +inputTag }
                define('B') { +tag(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON) }
                +gear.toStack()
                recipeId suffix "/gear"
            }.save(exporter)
        }
    }

    @JvmStatic
    private fun ingotToNugget(entry: HTMaterial) {
        val key: HTMaterialKey = entry.key
        val nugget: HTMaterialContents.ItemEntry = getItem(CommonParts.NUGGET, key) ?: return
        val ingot: HTMaterialContents.ItemEntry = getItem(CommonParts.INGOT, key) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (nugget.isBuiltIn && ingot.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.INGOT, key) }
            +nugget.toStack(9)
            recipeId suffix "/nugget_from_ingot"
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.NUGGET, key) }
            define('B') { +nugget }
            +ingot.toStack()
            recipeId suffix "/ingot_from_nugget"
        }.save(exporter)
    }

    @JvmStatic
    private fun rawToBlock(entry: HTMaterial) {
        val key: HTMaterialKey = entry.key
        val raw: HTMaterialContents.ItemEntry = getItem(CommonParts.RAW, key) ?: return
        val rawBlock: HTMaterialContents.ItemEntry = getItem(CommonParts.RAW_BLOCK, key) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (raw.isBuiltIn && rawBlock.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.RAW_STORAGE_BLOCK, key) }
            +raw.toStack(9)
            recipeId suffix "/raw_from_block"
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.RAW_MATERIALS, key) }
            define('B') { +raw }
            +rawBlock.toStack()
        }.save(exporter)
    }

    @JvmStatic
    private fun tinyToFuel(entry: HTMaterial) {
        val key: HTMaterialKey = entry.key
        val tiny: HTMaterialContents.ItemEntry = getItem(CommonParts.TINY, key) ?: return
        val fuel: HTMaterialContents.ItemEntry = getItem(CommonParts.FUEL, key) ?: return
        // クラフトの前後がどちらも既存アイテムの場合はパス
        if (tiny.isBuiltIn && fuel.isBuiltIn) return
        // レシピを登録
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.FUEL, key) }
            +tiny.toStack(8)
            recipeId suffix "/tiny_from_fuel"
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow()
            define('A') { +tag(CommonTagPrefixes.TINY, key) }
            +fuel.toStack()
            recipeId replace entry.getId()
        }.save(exporter)
    }

    @JvmStatic
    private fun tool(entry: HTMaterial) {
        val existing: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> =
            HiiragiCoreAccess.INSTANCE.existingContents.tools
        val registered: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry> =
            HiiragiCoreAccess.INSTANCE.registeredContents.tools

        val key: HTMaterialKey = entry.key
        val inputTag: TagKey<Item> = entry.getDefaultPart(key) ?: return
        for ((toolType: HTToolType, tool: HTMaterialContents.ItemEntry) in registered.column(key)) {
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
    private fun smeltDustToIngot(entry: HTMaterial) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val key: HTMaterialKey = entry.key
        val dust: HTMaterialContents.ItemEntry = getItem(CommonParts.DUST, key) ?: return
        val smeltedMaterial: HTMaterialKey = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: key
        val ingot: HTMaterialContents.ItemEntry = getItem(CommonParts.INGOT, smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        if (dust.isBuiltIn && ingot.isBuiltIn) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient { +dust }
            +ingot.toStack()
            exp = 0.35f
            recipeId suffix "_from_${CommonParts.DUST.name}"
        }
    }

    @JvmStatic
    private fun smeltOresToBase(entry: HTMaterial) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val key: HTMaterialKey = entry.key
        val smeltedMaterial: HTMaterialKey = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: key
        val smeltedPropertyMap: HTMaterial = materialManager[smeltedMaterial] ?: return
        val base: HTMaterialContents.ItemEntry = smeltedPropertyMap.getDefaultPart()?.getItem(smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        val oreEntries: List<HTMaterialContents.ItemEntry> = partManager
            .filter { HTPartPropertyKeys.IS_ORE in it }
            .mapNotNull { getItem(it.key, key) }
            .filterNot(HTMaterialContents.ItemEntry::isBuiltIn)
        if (oreEntries.isEmpty()) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient { +oreEntries }
            +base.toStack(smeltedPropertyMap.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER).toInt())
            exp = 0.7f
            recipeId suffix "_from_${CommonParts.ORE.name}"
        }
    }

    @JvmStatic
    private fun smeltOreToBase(partKey: HTPartKey, entry: HTMaterial) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val key: HTMaterialKey = entry.key
        val ore: HTMaterialContents.ItemEntry = getItem(partKey, key) ?: return
        val smeltedMaterial: HTMaterialKey = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: key
        val smeltedPropertyMap: HTMaterial = materialManager[smeltedMaterial] ?: return
        val base: HTMaterialContents.ItemEntry = smeltedPropertyMap.getDefaultPart()?.getItem(smeltedMaterial) ?: return
        // 精錬の前後がどちらも既存アイテムの場合はパス
        if (ore.isBuiltIn && base.isBuiltIn) return
        // Smelting & Blasting
        registerSmelting(entry) {
            ingredient { +ore }
            +base.toStack(smeltedPropertyMap.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER).toInt())
            exp = 0.7f
            recipeId suffix "_from_${partKey.name}"
        }
    }

    @JvmStatic
    private inline fun registerSmelting(entry: HTMaterial, builderAction: HTCookingRecipeBuilder.() -> Unit) {
        when (entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)) {
            HTMaterialLevel.NONE -> emptySequence()
            HTMaterialLevel.LOW -> HTCookingRecipeBuilder.smeltingAndBlasting(builderAction)
            HTMaterialLevel.MEDIUM -> HTCookingRecipeBuilder.smeltingAndBlasting(builderAction)
            HTMaterialLevel.HIGH -> sequenceOf(HTCookingRecipeBuilder.blasting(builderAction))
            HTMaterialLevel.HIGHEST -> emptySequence()
        }.forEach { it.save(exporter) }
    }
}
