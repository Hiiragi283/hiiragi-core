package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.toTemplate
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTMaterialRawEntry
import hiiragi283.lib.registry.HTDeferredMaterialContents
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.util.getOrThrow
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

abstract class HTMaterialRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    //    Base <-> Storage Block    //

    protected fun fourBaseToBlock(contents: HTDeferredMaterialContents) {
        baseToBlock(contents, CommonPartKeys.STORAGE_BLOCK, "AA", "BA")
    }

    protected fun nineBaseToBlock(contents: HTDeferredMaterialContents) {
        baseToBlock(contents, CommonPartKeys.STORAGE_BLOCK, "AAA", "ABA", "AAA")
    }

    protected fun baseToBlock(contents: HTDeferredMaterialContents, blockKey: HTMaterialPartKey, vararg pattern: String) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
        // ブロックと基本アイテムは必須
        val block: HTMaterialItemEntry = contents.getEntry(blockKey) ?: return
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return

        val builder: ShapedRecipeBuilder = shaped(RecipeCategory.BUILDING_BLOCKS, block)
        pattern.forEach(builder::pattern)
        builder.define('B', base)
        // 基本タグの有無で分岐
        val baseTag: TagKey<Item>? = contents.getTagKey(primalKey)
        if (baseTag == null) {
            builder
                .define('A', base)
                .unlockedBy(getHasName(base), has(base))
        } else {
            builder
                .define('A', baseTag)
                .unlockedBy(getHasName(base), has(baseTag))
        }.save(output, idFrom(contents, blockKey, primalKey))
    }

    protected fun blockToFourBase(contents: HTDeferredMaterialContents) {
        blockToBase(contents, CommonPartKeys.STORAGE_BLOCK, 4)
    }

    protected fun blockToNineBase(contents: HTDeferredMaterialContents) {
        blockToBase(contents, CommonPartKeys.STORAGE_BLOCK, 9)
    }

    protected fun blockToBase(contents: HTDeferredMaterialContents, blockKey: HTMaterialPartKey, count: Int) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val builder: ShapelessRecipeBuilder = shapeless(RecipeCategory.MISC, base, count)
        contents.getRawEntry(blockKey)?.fold(
            { block: HTMaterialItemEntry -> builder.requires(block).unlockedBy(getHasName(block), has(block)) },
            { blockTag: TagKey<Item> -> builder.requires(blockTag).unlockedBy(getHasName(blockTag), has(blockTag)) },
            { block: HTMaterialItemEntry, blockTag: TagKey<Item> -> builder.requires(blockTag).unlockedBy(getHasName(block), has(blockTag)) },
        )?.save(output, idFrom(contents, primalKey, blockKey))
    }

    //    Base <-> Nugger    //

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

    protected fun rawToBlock(contents: HTDeferredMaterialContents) {
        baseToBlock(contents, CommonPartKeys.RAW_BLOCK, "AAA", "ABA", "AAA")
        blockToBase(contents, CommonPartKeys.RAW_BLOCK, 9)
    }

    //    Raw -> Base    //

    protected fun rawToBase(contents: HTDeferredMaterialContents) {
        val primalKey: HTMaterialPartKey = contents.get().primalKey
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val raw: HTMaterialRawEntry = contents.getRawEntry(CommonPartKeys.RAW) ?: return
        HTCookingRecipeBuilder.smeltingAndBlasting {
            ingredient = raw.map(Ingredient::of, ::tag)
            exp = 3.5f
            result = base.toTemplate().getOrThrow()
            raw.fold(
                { item: HTMaterialItemEntry -> unlocker.unlockedBy(getHasName(item), has(item)) },
                { itemTag: TagKey<Item> -> unlocker.unlockedBy(getHasName(itemTag), has(itemTag)) },
                { item: HTMaterialItemEntry, itemTag: TagKey<Item> -> unlocker.unlockedBy(getHasName(item), has(itemTag)) },
            )
            recipeId replace idFrom(contents, primalKey, CommonPartKeys.RAW)
        }.forEach { it.save(output) }
    }

    protected fun idFrom(contents: HTIdLike, after: HTMaterialPartKey, before: HTMaterialPartKey): Identifier = id(HTConstants.MATERIAL, contents.path, "${after.name}_from_${before.name}")
}
