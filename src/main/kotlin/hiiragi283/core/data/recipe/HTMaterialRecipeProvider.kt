package hiiragi283.core.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.save
import hiiragi283.lib.item.toTemplate
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTMaterialRawEntry
import hiiragi283.lib.math.component1
import hiiragi283.lib.math.component2
import hiiragi283.lib.registry.HTDeferredMaterialContents
import hiiragi283.lib.resource.HTIdLike
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.apache.commons.lang3.math.Fraction

abstract class HTMaterialRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    //    Base <-> Storage Block    //

    protected fun nineStorageBlock(contents: HTDeferredMaterialContents, baseToBlock: Boolean = true, blockToBase: Boolean = true) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
        if (baseToBlock) {
            baseToBlock(contents, primalKey, CommonPartKeys.STORAGE_BLOCK, "AAA", "ABA", "AAA")
        }
        if (blockToBase) {
            blockToBase(contents, primalKey, CommonPartKeys.STORAGE_BLOCK, 9)
        }
    }

    protected fun fourStorageBlock(contents: HTDeferredMaterialContents, baseToBlock: Boolean = true, blockToBase: Boolean = true) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
        if (baseToBlock) {
            baseToBlock(contents, primalKey, CommonPartKeys.STORAGE_BLOCK, "AA", "BA")
        }
        if (blockToBase) {
            blockToBase(contents, primalKey, CommonPartKeys.STORAGE_BLOCK, 4)
        }
    }

    protected fun baseToBlock(contents: HTDeferredMaterialContents, baseKey: HTMaterialPartKey, blockKey: HTMaterialPartKey, vararg pattern: String) {
        // ブロックと基本アイテムは必須
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
        }.save(output, idFrom(contents, blockKey, baseKey))
    }

    protected fun blockToBase(contents: HTDeferredMaterialContents, baseKey: HTMaterialPartKey, blockKey: HTMaterialPartKey, count: Int) {
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(baseKey) ?: return
        val builder: ShapelessRecipeBuilder = shapeless(RecipeCategory.MISC, base, count)
        contents.getRawEntry(blockKey)?.fold(
            { block: HTMaterialItemEntry -> builder.requires(block).unlockedBy(getHasName(block), has(block)) },
            { blockTag: TagKey<Item> -> builder.requires(blockTag).unlockedBy(getHasName(blockTag), has(blockTag)) },
            { block: HTMaterialItemEntry, blockTag: TagKey<Item> -> builder.requires(blockTag).unlockedBy(getHasName(block), has(blockTag)) },
        )?.save(output, idFrom(contents, baseKey, blockKey))
    }

    //    Base <-> Nugger    //

    protected fun nineNugget(contents: HTDeferredMaterialContents, baseToNugget: Boolean = true, nuggetToBase: Boolean = true) {
        if (nuggetToBase) {
            nuggetToBase(contents)
        }
        if (baseToNugget) {
            baseToNugget(contents)
        }
    }

    protected fun nuggetToBase(contents: HTDeferredMaterialContents) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
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
        }.save(output, idFrom(contents, primalKey, CommonPartKeys.RAW))
    }

    protected fun baseToNugget(contents: HTDeferredMaterialContents) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
        // ナゲットは必須
        val nugget: HTMaterialItemEntry = contents.getEntry(CommonPartKeys.NUGGET) ?: return
        val builder: ShapelessRecipeBuilder = shapeless(RecipeCategory.MISC, nugget, 9)
        contents.get().primalEntry.fold(
            { base: HTMaterialItemEntry -> builder.requires(base).unlockedBy(getHasName(base), has(base)) },
            { baseTag: TagKey<Item> -> builder.requires(baseTag).unlockedBy(getHasName(baseTag), has(baseTag)) },
            { base: HTMaterialItemEntry, baseTag: TagKey<Item> -> builder.requires(baseTag).unlockedBy(getHasName(base), has(baseTag)) },
        ).save(output, idFrom(contents, CommonPartKeys.NUGGET, primalKey))
    }

    //    Raw <-> Raw Block    //

    protected fun rawStorageBlock(contents: HTDeferredMaterialContents) {
        baseToBlock(contents, CommonPartKeys.RAW, CommonPartKeys.RAW_BLOCK, "AAA", "ABA", "AAA")
        blockToBase(contents, CommonPartKeys.RAW, CommonPartKeys.RAW_BLOCK, 9)
    }

    //    XX -> Base    //

    protected fun smeltDustToBase(contents: HTDeferredMaterialContents) {
        smeltToBase(contents, CommonPartKeys.DUST, 0.35f)
    }

    protected fun smeltRawToBase(contents: HTDeferredMaterialContents, exp: Float) {
        smeltToBase(contents, CommonPartKeys.RAW, exp)
    }

    protected fun smeltToBase(contents: HTDeferredMaterialContents, inputKey: HTMaterialPartKey, exp: Float) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val input: HTMaterialRawEntry = contents.getRawEntry(inputKey) ?: return
        HTCookingRecipeBuilder.smeltingAndBlasting {
            ingredient = input.toIngredient()
            this.exp = exp
            base.toTemplate().onRight(::result::set)
            input.fold(
                { item: HTMaterialItemEntry -> unlocker.unlockedBy(getHasName(item), has(item)) },
                { itemTag: TagKey<Item> -> unlocker.unlockedBy(getHasName(itemTag), has(itemTag)) },
                { item: HTMaterialItemEntry, itemTag: TagKey<Item> -> unlocker.unlockedBy(getHasName(item), has(itemTag)) },
            )
            recipeId replace idFrom(contents, primalKey, inputKey)
        }.forEach { it.save(output) }
    }

    //    XX -> Dust    //

    protected fun crushBaseToDust(contents: HTDeferredMaterialContents, scale: Fraction = Fraction.ONE) {
        crushToDust(contents, contents.get().primalKey, scale)
    }

    protected fun crushToDust(contents: HTDeferredMaterialContents, inputKey: HTMaterialPartKey, scale: Fraction = Fraction.ONE) {
        val input: HTMaterialRawEntry = contents.getRawEntry(inputKey) ?: return
        val dust: HTMaterialItemEntry = contents.getEntry(CommonPartKeys.DUST) ?: return

        val (outputCount: Int, inputCount: Int) = scale
        HCRecipeBuilders.crushing {
            ingredient = input.toItemIngredient(inputCount)
            results += resultCreator.create(dust, outputCount)
            recipeId replace idFrom(contents, inputKey, CommonPartKeys.DUST)
        }.save(output)
    }

    protected fun idFrom(contents: HTIdLike, after: HTMaterialPartKey, before: HTMaterialPartKey): Identifier = id(HTConstants.MATERIAL, contents.path, "${after.name}_from_${before.name}")
}
