package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmithingRecipeProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.getScaledAmount
import hiiragi283.core.common.data.recipe.builder.HCAnvilCrushingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSmithingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
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
            crushCrushedToDust(event, entry)
            crushOreToCrushed(event, entry, CommonTagPrefixes.ORE)
            crushOreToCrushed(event, entry, CommonTagPrefixes.RAW)

            baseToBlock(event, entry)
            ingotToNugget(entry)
            rawToBlock(event, entry)
            tool(entry)

            flourToDough(event, entry)

            val hardness: HTMaterialLevel = entry.getOrDefault(HTMaterialPropertyKeys.HARDNESS)
            if (hardness > HTMaterialLevel.NONE && hardness <= HTMaterialLevel.MEDIUM) {
                baseToGear(event, entry)
                ingotToPlate(event, entry)

                plateToWire(event, entry)
            }

            smeltDustToIngot(entry)
            for (prefix: HTTagPrefix in CommonTagPrefixes.ORES) {
                smeltOreToBase(prefix, entry)
            }
            smeltOreToBase(CommonTagPrefixes.CRUSHED_ORE, entry)
            smeltOreToBase(CommonTagPrefixes.RAW, entry)
        }
    }

    //    Anvil Crushing    //

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        // 加工の前後でタグが一致する場合はパス
        if (!event.isPresentTag(inputTag)) return
        if (inputTag == crushedPrefix.itemTagKey(entry)) return
        // 完成品を取得
        val dust: ItemLike = event.getFirstHolder(crushedPrefix, entry) ?: return
        // レシピを登録
        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(inputTag)
            result = resultCreator.create(dust)
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun crushOreToCrushed(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 完成品を取得
        val crushedOre: ItemLike = event.getFirstHolder(CommonTagPrefixes.CRUSHED_ORE, entry) ?: return
        // レシピを登録
        HCAnvilCrushingRecipeBuilder.create(output) {
            // 材料
            ingredient = inputCreator.create(prefix, entry)
            // 主産物
            result = resultCreator.create(crushedOre, prefix.getScaledAmount(2, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.CRUSH_ORE)
                ?.let(::extraResult::set)

            recipeId suffix "_from_${prefix.name}"
        }
    }

    @JvmStatic
    private fun crushCrushedToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.CRUSHED_ORE, entry)) return
        // 完成品を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val dust: ItemLike = event.getFirstHolder(crushedPrefix, entry) ?: return
        // レシピを登録
        HCAnvilCrushingRecipeBuilder.create(output) {
            // 材料
            ingredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry)
            // 主産物
            result = resultCreator.create(dust, CommonTagPrefixes.CRUSHED_ORE.getScaledAmount(1, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.CRUSH_CRUSHED)
                ?.let(::extraResult::set)

            recipeId suffix "_from_crushed_ore"
        }
    }

    //    Crafting    //

    private fun getBlock(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? =
        HiiragiCoreAccess.INSTANCE.getBlockOrVanilla(prefix, material)

    private fun getItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? =
        HiiragiCoreAccess.INSTANCE.getItemOrVanilla(prefix, material)

    @JvmStatic
    private fun baseToBlock(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        val blockProperty: HTStorageBlockProperty = entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)
        val block: HTItemHolderLike<*> = getBlock(CommonTagPrefixes.BLOCK, entry) ?: return

        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val suffix: String = defaultPart.getSuffix()
        val base: HTItemHolderLike<*> = defaultPart.getItem(entry) ?: return
        if (block.namespace == HTConst.MINECRAFT && base.namespace == HTConst.MINECRAFT) return
        // レシピを登録
        if (event.isPresentTag(CommonTagPrefixes.BLOCK, entry)) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += CommonTagPrefixes.BLOCK to entry
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
                define('A') += inputTag
                define('B') += base
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
                    this.template += template
                    this.base += CommonTagPrefixes.GEAR.itemTagKey(base)
                    this.addition += inputTag
                    this.resultStack += gear
                    recipeId replace entry.getId().withSuffix("/gear")
                }
            }
        }
        if (smithingProperty?.allowCrafting ?: true) {
            // レシピを登録
            HTShapedRecipeBuilder.create(output) {
                hollow4()
                define('A') += inputTag
                define('B') += Tags.Items.NUGGETS_IRON
                resultStack += gear
                recipeId replace entry.getId().withSuffix("/gear")
            }
        }
    }

    @JvmStatic
    private fun flourToDough(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
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
                .ingredient
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
    }

    @JvmStatic
    private fun ingotToNugget(entry: HTMaterialManager.Entry) {
        val nugget: HTItemHolderLike<*> = getItem(CommonTagPrefixes.NUGGET, entry) ?: return
        if (nugget.namespace == HTConst.MINECRAFT) return
        // レシピを登録
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to entry
            resultStack += nugget to 9
            recipeId replace entry.getId().withSuffix("/nugget_from_ingot")
        }
        // レシピを登録
        val ingot: HTItemHolderLike<*> = getItem(CommonTagPrefixes.INGOT, entry) ?: return
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.NUGGET to entry
            define('B') += nugget
            resultStack += ingot
            recipeId replace entry.getId().withSuffix("/ingot_from_nugget")
        }
    }

    @JvmStatic
    private fun ingotToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        if (!event.isPresentTag(CommonTagPrefixes.INGOT, entry)) return
        val plate: ItemLike = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to entry
            ingredients += CommonToolTypes.HAMMER
            resultStack += plate
            recipeId suffix "_from_ingot"
        }
    }

    @JvmStatic
    private fun rawToBlock(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        val raw: HTItemHolderLike<*> = getItem(CommonTagPrefixes.RAW, entry) ?: return
        if (raw.namespace == HTConst.MINECRAFT) return
        // レシピを登録
        if (event.isPresentTag(CommonTagPrefixes.RAW_BLOCK, entry)) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += CommonTagPrefixes.RAW_BLOCK to entry
                resultStack += raw to 9
                recipeId replace entry.getId().withSuffix("/raw_from_block")
            }
        }
        // レシピを登録
        val rawBlock: HTItemHolderLike<*> = getBlock(CommonTagPrefixes.RAW_BLOCK, entry) ?: return
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.RAW to entry
            define('B') += raw
            resultStack += rawBlock
            recipeId replace entry.getId()
        }
    }

    @JvmStatic
    private fun tool(entry: HTMaterialManager.Entry) {
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        for ((toolType: HTToolType, tool: HTItemHolderLike<*>) in HiiragiCoreAccess.INSTANCE.materialContents.getToolMap(entry)) {
            val smithingProperty: HTSmithingRecipeProperty? = entry[HTMaterialPropertyKeys.SMITHING_RECIPE]
            if (smithingProperty != null) {
                // Smithing
                val (template: HTItemHolderLike<*>, base: HTMaterialKey) = smithingProperty
                val baseTool: ItemLike = HiiragiCoreAccess.INSTANCE.getToolOrVanilla(toolType, base) ?: continue
                HTSmithingRecipeBuilder.create(output) {
                    this.template += template
                    this.base += baseTool
                    this.addition += inputTag
                    this.resultStack += tool
                }
            }
            if (smithingProperty?.allowCrafting ?: true) {
                // Shaped
                HTShapedRecipeBuilder.create(output) {
                    pattern(toolType.recipePattern)
                    define('A') += inputTag
                    define('B') += Tags.Items.RODS_WOODEN
                    resultStack += tool
                }
            }
        }
    }

    //    Smelting    //

    @JvmStatic
    private fun smeltDustToIngot(entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val dust: HTItemHolderLike<*> = getItem(CommonTagPrefixes.DUST, entry) ?: return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val ingot: HTItemHolderLike<*> = getItem(CommonTagPrefixes.INGOT, smeltedMaterial) ?: return
        // 精錬の前後がどちらもバニラ由来の場合はパス
        if (dust.namespace == HTConst.MINECRAFT && ingot.namespace == HTConst.MINECRAFT) return
        // Smelting & Blasting
        when (entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)) {
            HTMaterialLevel.NONE -> return
            HTMaterialLevel.LOW -> HTCookingRecipeBuilder::smeltingAndBlasting
            HTMaterialLevel.MEDIUM -> HTCookingRecipeBuilder::smeltingAndBlasting
            HTMaterialLevel.HIGH -> HTCookingRecipeBuilder::blasting
            HTMaterialLevel.HIGHEST -> return
        }(output) {
            ingredient += dust
            resultStack += ingot
            exp = 0.35f
            recipeId suffix "_from_dust"
        }
    }

    @JvmStatic
    private fun smeltOreToBase(prefix: HTTagPrefix, entry: HTMaterialManager.Entry) {
        if (HTMaterialPropertyKeys.DISABLE_SMELTING in entry) return
        val ore: HTItemHolderLike<*> = getBlock(prefix, entry) ?: getItem(prefix, entry) ?: return
        val smeltedMaterial: HTMaterialLike = entry[HTMaterialPropertyKeys.SMELTED_TO] ?: entry
        val smeltedPropertyMap: HTPropertyMap = materialManager[smeltedMaterial] ?: return
        val base: HTItemHolderLike<*> = smeltedPropertyMap
            .getDefaultPart()
            ?.getItem(smeltedMaterial)
            ?: return
        // 精錬の前後がどちらもバニラ由来の場合はパス
        if (ore.namespace == HTConst.MINECRAFT && base.namespace == HTConst.MINECRAFT) return
        // Smelting & Blasting
        when (entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)) {
            HTMaterialLevel.NONE -> return
            HTMaterialLevel.LOW -> HTCookingRecipeBuilder::smeltingAndBlasting
            HTMaterialLevel.MEDIUM -> HTCookingRecipeBuilder::blasting
            HTMaterialLevel.HIGH -> HTCookingRecipeBuilder::blasting
            HTMaterialLevel.HIGHEST -> return
        }(output) {
            ingredient += ore
            resultStack += base to smeltedPropertyMap.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER).toInt()
            exp = 0.7f
            recipeId suffix "_from_${prefix.name}"
        }
    }

    //    Stonecutting    //

    @JvmStatic
    private fun plateToWire(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        if (!event.isPresentTag(CommonTagPrefixes.PLATE, entry)) return
        val wire: ItemLike = event.getFirstHolder(CommonTagPrefixes.WIRE, entry) ?: return
        // Stonecutting
        HTStonecuttingRecipeBuilder.create(output) {
            ingredient += CommonTagPrefixes.PLATE to entry
            resultStack += wire
            recipeId suffix "_from_plate"
        }
    }
}
