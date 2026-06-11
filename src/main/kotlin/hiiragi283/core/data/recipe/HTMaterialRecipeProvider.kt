package hiiragi283.core.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.data.recipe.save
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTMaterialRawEntry
import hiiragi283.lib.math.component1
import hiiragi283.lib.math.component2
import hiiragi283.lib.recipe.ingredient.withSize
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import org.apache.commons.lang3.math.Fraction

abstract class HTMaterialRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    protected fun getContents(material: HTMaterialKey): HTMaterialContents = registries.getOrThrow(material).value()

    protected fun metals(material: HTMaterialKey, exp: Float) {
        nineStorageBlock(material)
        nineNugget(material)

        rawStorageBlock(material)
        smeltDustToBase(material)
        smeltRawToBase(material, exp)
        crushBaseToDust(material)
    }

    //    Base <-> Storage Block    //

    protected fun nineStorageBlock(material: HTMaterialKey, baseToBlock: Boolean = true, blockToBase: Boolean = true) {
        val primalKey: HTMaterialPartKey = getContents(material).primalKey
        if (baseToBlock) {
            baseToBlock(material, primalKey, CommonPartKeys.STORAGE_BLOCK, "AAA", "ABA", "AAA")
        }
        if (blockToBase) {
            blockToBase(material, primalKey, CommonPartKeys.STORAGE_BLOCK, 9)
        }
    }

    protected fun fourStorageBlock(material: HTMaterialKey, baseToBlock: Boolean = true, blockToBase: Boolean = true) {
        val primalKey: HTMaterialPartKey = getContents(material).primalKey
        if (baseToBlock) {
            baseToBlock(material, primalKey, CommonPartKeys.STORAGE_BLOCK, "AA", "BA")
        }
        if (blockToBase) {
            blockToBase(material, primalKey, CommonPartKeys.STORAGE_BLOCK, 4)
        }
    }

    protected fun baseToBlock(material: HTMaterialKey, baseKey: HTMaterialPartKey, blockKey: HTMaterialPartKey, vararg pattern: String) {
        // ブロックと基本アイテムは必須
        val contents: HTMaterialContents = getContents(material)
        val block: HTMaterialItemEntry = contents.getEntry(blockKey) ?: return
        val base: HTMaterialItemEntry = contents.getEntry(baseKey) ?: return

        val builder: ShapedRecipeBuilder = shaped(RecipeCategory.BUILDING_BLOCKS, block)
        pattern.forEach(builder::pattern)
        builder.define('B', base)
        // 基本タグの有無で分岐
        val baseTag: TagKey<Item>? = contents.getTagKey(baseKey)
        if (baseTag == null) {
            builder
                .define('A', base)
                .unlockedBy(getHasName(base), has(base))
        } else {
            builder
                .define('A', baseTag)
                .unlockedBy(getHasName(base), has(baseTag))
        }.save(output, idFrom(material, blockKey, baseKey))
    }

    protected fun blockToBase(material: HTMaterialKey, baseKey: HTMaterialPartKey, blockKey: HTMaterialPartKey, count: Int) {
        // 基本アイテムは必須
        val contents: HTMaterialContents = getContents(material)
        val base: HTMaterialItemEntry = contents.getEntry(baseKey) ?: return
        val input: HTMaterialRawEntry = contents.getRawEntry(blockKey) ?: return
        HTShapelessRecipeBuilder.create {
            ingredient {
                input.map(
                    { block: HTMaterialItemEntry -> items { +block } },
                    { blockTag: TagKey<Item> -> +holderSet(blockTag) },
                )
            }
            +ItemStackTemplate(base.asItem(), count)
            recipeId replace idFrom(material, baseKey, blockKey)
        }.save(output)
    }

    //    Base <-> Nugger    //

    protected fun nineNugget(material: HTMaterialKey, baseToNugget: Boolean = true, nuggetToBase: Boolean = true) {
        if (nuggetToBase) {
            nuggetToBase(material)
        }
        if (baseToNugget) {
            baseToNugget(material)
        }
    }

    protected fun nuggetToBase(material: HTMaterialKey) {
        val contents: HTMaterialContents = getContents(material)
        val primalKey: HTMaterialPartKey = contents.primalKey
        // ナゲットと基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val nugget: HTMaterialItemEntry = contents.getEntry(CommonPartKeys.NUGGET) ?: return

        val builder: ShapedRecipeBuilder = shaped(RecipeCategory.MISC, base)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('B', nugget)
        // 基本タグの有無で分岐
        val nuggetTag: TagKey<Item>? = contents.getTagKey(CommonPartKeys.NUGGET)
        if (nuggetTag == null) {
            builder
                .define('A', nugget)
                .unlockedBy(getHasName(nugget), has(nugget))
        } else {
            builder
                .define('A', nuggetTag)
                .unlockedBy(getHasName(nugget), has(nuggetTag))
        }.save(output, idFrom(material, primalKey, CommonPartKeys.RAW))
    }

    protected fun baseToNugget(material: HTMaterialKey) {
        val contents: HTMaterialContents = getContents(material)
        val primalKey: HTMaterialPartKey = contents.primalKey
        // ナゲットは必須
        val nugget: HTMaterialItemEntry = contents.getEntry(CommonPartKeys.NUGGET) ?: return
        HTShapelessRecipeBuilder.create {
            ingredient {
                contents.primalEntry.map(
                    { base: HTMaterialItemEntry -> items { +base } },
                    { baseTag: TagKey<Item> -> +holderSet(baseTag) },
                )
            }
            +ItemStackTemplate(nugget.asItem(), 9)
            recipeId replace idFrom(material, CommonPartKeys.NUGGET, primalKey)
        }.save(output)
    }

    //    Raw <-> Raw Block    //

    protected fun rawStorageBlock(material: HTMaterialKey) {
        baseToBlock(material, CommonPartKeys.RAW, CommonPartKeys.RAW_BLOCK, "AAA", "ABA", "AAA")
        blockToBase(material, CommonPartKeys.RAW, CommonPartKeys.RAW_BLOCK, 9)
    }

    //    XX -> Base    //

    protected fun smeltDustToBase(material: HTMaterialKey) {
        smeltToBase(material, CommonPartKeys.DUST, 0.35f)
    }

    protected fun smeltRawToBase(material: HTMaterialKey, exp: Float) {
        smeltToBase(material, CommonPartKeys.RAW, exp)
    }

    protected fun smeltToBase(material: HTMaterialKey, inputKey: HTMaterialPartKey, exp: Float) {
        val contents: HTMaterialContents = getContents(material)
        val primalKey: HTMaterialPartKey = contents.primalKey
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val input: HTMaterialRawEntry = contents.getRawEntry(inputKey) ?: return
        HTCookingRecipeBuilder.smeltingAndBlasting {
            +input.toIngredient()
            this.exp = exp
            +base.toTemplate()
            recipeId replace idFrom(material, primalKey, inputKey)
        }.forEach { it.save(output) }
    }

    //    XX -> Dust    //

    protected fun crushBaseToDust(material: HTMaterialKey, scale: Fraction = Fraction.ONE) {
        crushToDust(material, getContents(material).primalKey, scale)
    }

    protected fun crushToDust(material: HTMaterialKey, inputKey: HTMaterialPartKey, scale: Fraction = Fraction.ONE) {
        val contents: HTMaterialContents = getContents(material)
        val input: HTMaterialRawEntry = contents.getRawEntry(inputKey) ?: return
        val dust: HTMaterialItemEntry = contents.getEntry(CommonPartKeys.DUST) ?: return

        val (outputCount: Int, inputCount: Int) = scale
        HCRecipeBuilders.crushing {
            +(input.toIngredient() withSize inputCount)
            result {
                +dust
                count = outputCount
            }
            recipeId replace idFrom(material, inputKey, CommonPartKeys.DUST)
        }.save(output)
    }

    protected fun idFrom(material: HTMaterialKey, after: HTMaterialPartKey, before: HTMaterialPartKey): Identifier = id(HTConstants.MATERIAL, material.identifier().path, "${after.name}_from_${before.name}")
}
