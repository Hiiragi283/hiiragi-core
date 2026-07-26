package hiiragi283.core.data.recipe

import appeng.recipes.transform.TransformCircumstance
import appeng.recipes.transform.TransformRecipeBuilder
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient

class HCAERecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider.Integration(packOutput, future, HiiragiCoreAPI.MOD_ID, HCIConstants.AE2) {
    override fun buildRecipes() {
        val output: RecipeOutput = exporter.asOutput().withConditions(condition)
        // Steel Ingot
        useItem(CommonParts.INGOT, CommonMaterialKeys.STEEL) {
            TransformRecipeBuilder.transform(
                output,
                id("transform", "steel_ingot_with_charcoal"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                baseOrDustIngredient(VanillaMaterialKeys.IRON),
                baseOrDustIngredient(VanillaMaterialKeys.CHARCOAL),
            )
            TransformRecipeBuilder.transform(
                output,
                id("transform", "steel_ingot_with_coal"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                baseOrDustIngredient(VanillaMaterialKeys.IRON),
                baseOrDustIngredient(VanillaMaterialKeys.COAL),
            )
        }
        // Cured Rubber
        TransformRecipeBuilder.transform(
            output,
            id("transform", "cured_rubber"),
            HCItems.CURED_RUBBER,
            2,
            TransformCircumstance.EXPLOSION,
            Ingredient.of(HCItems.RAW_RUBBER),
            baseOrDustIngredient(CommonMaterialKeys.SULFUR),
        )
        // Azure Shard
        useItem(CommonParts.GEM, HCMaterialKeys.AZURE) {
            TransformRecipeBuilder.transform(
                output,
                id("transform", "azure_shard"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                baseOrDustIngredient(VanillaMaterialKeys.AMETHYST),
                baseOrDustIngredient(VanillaMaterialKeys.LAPIS),
            )
        }
        // Azure Steel
        useItem(CommonParts.INGOT, HCMaterialKeys.AZURE_STEEL) {
            TransformRecipeBuilder.transform(
                output,
                id("transform", "azure_steel_ingot"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                baseOrDustIngredient(VanillaMaterialKeys.IRON),
                baseOrDustIngredient(HCMaterialKeys.AZURE),
            )
        }
    }

    fun baseOrDustIngredient(key: HTMaterialKey): Ingredient = IngredientBuilder().apply { +baseOrDust(key) }.build()

    override fun getName(): String = "AE2 Recipes"
}
