package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.math.fraction
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCChargingRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Ender Pearl -> Ender Eye
        HCChargingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Tags.Items.ENDER_PEARLS)
            result = resultCreator.create(Items.ENDER_EYE).withChance(fraction(1, 2))
        }.save(output)
        // Golden Apple
        HCChargingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Items.GOLDEN_APPLE)
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE).withChance(fraction(1, 8))
        }.save(output)
        // Quartz -> Prismarine
        HCChargingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Tags.Items.GEMS_QUARTZ)
            result = resultCreator.create(Items.PRISMARINE_SHARD).withChance(fraction(3, 4))
        }.save(output)
        // Redstone Dust -> Glowstone Dust
        HCChargingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Tags.Items.DUSTS_REDSTONE)
            result = resultCreator.create(Items.GLOWSTONE_DUST).withChance(fraction(3, 4))
        }.save(output)
        // Honey Bottle -> Exp Bottle
        HCChargingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Tags.Items.DRINKS_HONEY)
            result = resultCreator.create(Items.EXPERIENCE_BOTTLE).withChance(fraction(1, 2))
        }.save(output)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCChargingRecipeProvider) {
        override fun getName(): String = "Charging Recipes"
    }
}
