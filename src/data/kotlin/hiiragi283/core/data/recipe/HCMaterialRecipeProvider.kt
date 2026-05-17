package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCMaterialContents
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.save
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.util.Ior
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

class HCMaterialRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Base <-> Storage Block
        nineBaseToBlock(HCMaterialContents.CHARCOAL)
        blockToNineBase(HCMaterialContents.CHARCOAL)

        blockToFourBase(HCMaterialContents.GLOWSTONE)
        blockToFourBase(HCMaterialContents.QUARTZ)
        blockToFourBase(HCMaterialContents.AMETHYST)
        fourBaseToBlock(HCMaterialContents.ECHO)
        blockToFourBase(HCMaterialContents.ECHO)

        nineBaseToBlock(HCMaterialContents.ENDER_PEARL)
        blockToNineBase(HCMaterialContents.ENDER_PEARL)
        // Base <-> Nugget
        nuggetToBase(HCMaterialContents.NETHERITE)
        baseToNugget(HCMaterialContents.NETHERITE)

        // Raw <-> Raw Storage Block
        // Raw -> Base

        // Base -> Gear
    }

    //    Base <-> Storage Block    //

    private fun fourBaseToBlock(contents: HTMaterialContents) {
        baseToBlock(contents, CommonPartKeys.STORAGE_BLOCK, "AA", "BA")
    }

    private fun nineBaseToBlock(contents: HTMaterialContents) {
        baseToBlock(contents, CommonPartKeys.STORAGE_BLOCK, "AAA", "ABA", "AAA")
    }

    private fun baseToBlock(contents: HTMaterialContents, blockKey: HTMaterialPartKey, vararg pattern: String) {
        val primalKey: HTMaterialPartKey = contents.primalKey
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
        }.save(output, idFrom(block, base))
    }

    private fun blockToFourBase(contents: HTMaterialContents) {
        blockToBase(contents, CommonPartKeys.STORAGE_BLOCK, 4)
    }

    private fun blockToNineBase(contents: HTMaterialContents) {
        blockToBase(contents, CommonPartKeys.STORAGE_BLOCK, 9)
    }

    private fun blockToBase(contents: HTMaterialContents, blockKey: HTMaterialPartKey, count: Int) {
        val primalKey: HTMaterialPartKey = contents.primalKey
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val builder: ShapelessRecipeBuilder = shapeless(RecipeCategory.MISC, base, count)
        contents.getRawEntry(blockKey)?.fold(
            { block: HTMaterialItemEntry ->
                builder.requires(block)
                    .unlockedBy(getHasName(block), has(block))
                    .save(output, idFrom(base, block))
            },
            { blockTag: TagKey<Item> ->
                builder.requires(blockTag)
                    .unlockedBy(getHasName(blockTag), has(blockTag))
                    .save(output, idFrom(base, blockTag))
            },
            { block: HTMaterialItemEntry, blockTag: TagKey<Item> ->
                builder.requires(blockTag)
                    .unlockedBy(getHasName(block), has(blockTag))
                    .save(output, idFrom(base, block))
            },
        )
    }

    //    Base <-> Nugger    //

    private fun nuggetToBase(contents: HTMaterialContents) {
        // ナゲットと基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(contents.primalKey) ?: return
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
        }.save(output, idFrom(base, nugget))
    }

    private fun baseToNugget(contents: HTMaterialContents) {
        // ナゲットは必須
        val nugget: HTMaterialItemEntry = contents.getEntry(CommonPartKeys.NUGGET) ?: return
        val builder: ShapelessRecipeBuilder = shapeless(RecipeCategory.MISC, nugget, 9)
        contents.primalEntry.fold(
            { base: HTMaterialItemEntry ->
                builder.requires(base)
                    .unlockedBy(getHasName(base), has(base))
                    .save(output, idFrom(nugget, base))
            },
            { baseTag: TagKey<Item> ->
                builder.requires(baseTag)
                    .unlockedBy(getHasName(baseTag), has(baseTag))
                    .save(output, idFrom(nugget, baseTag))
            },
            { base: HTMaterialItemEntry, baseTag: TagKey<Item> ->
                builder.requires(baseTag)
                    .unlockedBy(getHasName(base), has(baseTag))
                    .save(output, idFrom(nugget, base))
            },
        )
    }

    //    Raw <-> Raw Block    //

    private fun rawToBlock(contents: HTMaterialContents) {
        baseToBlock(contents, CommonPartKeys.RAW_BLOCK, "AAA", "ABA", "AAA")
        blockToBase(contents, CommonPartKeys.RAW_BLOCK, 9)
    }

    //    Raw -> Base    //

    private fun rawToBase(contents: HTMaterialContents) {
        val primalKey: HTMaterialPartKey = contents.primalKey
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val raw: Ior<HTMaterialItemEntry, TagKey<Item>> = contents.getRawEntry(CommonPartKeys.RAW) ?: return
        HTCookingRecipeBuilder.smeltingAndBlasting {
            ingredient = raw.map(Ingredient::of, ::tag)
            exp = 3.5f
            result = base.toTemplate().getOrThrow()
            raw.fold(
                { item: HTMaterialItemEntry ->
                    unlocker.unlockedBy(getHasName(item), has(item))
                    recipeId replace idFrom(base, item)
                },
                { itemTag: TagKey<Item> ->
                    unlocker.unlockedBy(getHasName(itemTag), has(itemTag))
                    recipeId replace idFrom(base, itemTag)
                },
                { item: HTMaterialItemEntry, itemTag: TagKey<Item> ->
                    unlocker.unlockedBy(getHasName(item), has(itemTag))
                    recipeId replace idFrom(base, item)
                },
            )
        }.forEach { it.save(output) }
    }

    private fun idFrom(after: HTIdLike, before: HTIdLike): Identifier = id("${after.path}_from_${before.path}")

    private fun idFrom(after: HTIdLike, before: TagKey<*>): Identifier = id("${after.path}_from_${before.location().path}")

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCMaterialRecipeProvider) {
        override fun getName(): String = "Material Recipes"
    }
}
