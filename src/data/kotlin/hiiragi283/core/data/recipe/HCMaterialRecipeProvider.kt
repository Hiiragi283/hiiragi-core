package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCMaterialContents
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.save
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.material.HTMaterialPartKey
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

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
    }

    private fun fourBaseToBlock(contents: HTMaterialContents) {
        baseToBlock(contents, "AA", "BA")
    }

    private fun nineBaseToBlock(contents: HTMaterialContents) {
        baseToBlock(contents, "AAA", "ABA", "AAA")
    }

    private fun baseToBlock(contents: HTMaterialContents, vararg pattern: String) {
        val primalKey: HTMaterialPartKey = contents.primalKey
        // ブロックと基本アイテムは必須
        val block: HTMaterialItemEntry = contents.getEntry(CommonPartKeys.STORAGE_BLOCK) ?: return
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
        }.save(output, id("${block.path}_from_${base.path}"))
    }

    private fun blockToFourBase(contents: HTMaterialContents) {
        blockToBase(contents, 4)
    }

    private fun blockToNineBase(contents: HTMaterialContents) {
        blockToBase(contents, 9)
    }

    private fun blockToBase(contents: HTMaterialContents, count: Int) {
        val primalKey: HTMaterialPartKey = contents.primalKey
        // 基本アイテムは必須
        val base: HTMaterialItemEntry = contents.getEntry(primalKey) ?: return
        val builder: ShapelessRecipeBuilder = shapeless(RecipeCategory.MISC, base, count)
        contents.getRawEntry(CommonPartKeys.STORAGE_BLOCK)?.fold(
            { block: HTMaterialItemEntry ->
                builder.requires(block)
                    .unlockedBy(getHasName(block), has(block))
                    .save(output, id("${base.path}_from_${block.path}"))
            },
            { blockTag: TagKey<Item> ->
                builder.requires(blockTag)
                    .unlockedBy(getHasName(blockTag), has(blockTag))
                    .save(output, id("${base.path}_from_${blockTag.location().path}"))
            },
            { block: HTMaterialItemEntry, blockTag: TagKey<Item> ->
                builder.requires(blockTag)
                    .unlockedBy(getHasName(block), has(blockTag))
                    .save(output, id("${base.path}_from_${block.path}"))
            },
        )
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCMaterialRecipeProvider) {
        override fun getName(): String = "Material Recipes"
    }
}
