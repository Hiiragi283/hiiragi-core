package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.data.recipe.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.VanillaFluidContents
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.OminousBottleAmplifier
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.fluids.FluidType

class HCTankInteractionRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        emptyAndFill(Items.EXPERIENCE_BOTTLE, HCFluids.EXPERIENCE)
        emptyAndFill(Items.HONEY_BOTTLE, HCFluids.HONEY)
        emptyAndFill(Items.MUSHROOM_STEW, HCFluids.MUSHROOM_STEW, container = Items.BOWL)
        emptyAndFill(Items.DRAGON_BREATH, HCFluids.DRAGON_BREATH)

        emptyAndFill(Items.WET_SPONGE, VanillaFluidContents.WATER, FluidType.BUCKET_VOLUME, Items.SPONGE)

        repeat(5) { amplifier: Int ->
            HTTankInteractionRecipeBuilder.emptying {
                +DataComponentIngredient.of(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, OminousBottleAmplifier(amplifier), Items.OMINOUS_BOTTLE)
                +resultCreator.create(HCFluids.OMINOUS_FLUX, 250 * (amplifier + 1))
                itemResult { +Items.GLASS_BOTTLE }
                recipeId suffix "_$amplifier"
            }.save(output)
        }

        // Honey Block <-> Honey
        HTTankInteractionRecipeBuilder.emptying {
            ingredient { items { +Items.HONEY_BLOCK } }
            +resultCreator.create(HCFluids.HONEY)
            recipeId suffix "_from_block"
        }.save(output)
        HTTankInteractionRecipeBuilder.filling {
            +tag(Tags.Items.GLASS_BLOCKS)
            +fluidCreator.create(HCFluids.HONEY)
            itemResult { +Items.HONEY_BLOCK }
        }.save(output)

        // Latex + Bowl -> Raw Rubber
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient { items { +Items.BOWL } }
            +fluidCreator.create(HCFluids.LATEX)
            // +resultCreator.create(HCItems.RAW_RUBBER, 4)
        }
        // Raw Rubber -> Rubber Bar
        HTCookingRecipeBuilder.smelting {
            // +ingredientCreator.create(HCItems.RAW_RUBBER)
            // +HCItems.CURED_RUBBER.toStack()
            exp = 0.7f
        }
    }

    private fun emptyAndFill(
        bottle: ItemLike,
        fluid: HTFluidContent,
        amount: Int = 250,
        container: ItemLike = Items.GLASS_BOTTLE,
    ) {
        // Emptying
        HTTankInteractionRecipeBuilder.emptying {
            ingredient { items { +bottle } }
            +resultCreator.create(fluid, amount)
            itemResult { +container }
        }.save(output)
        // Filling
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient { items { +container } }
            +fluidCreator.create(fluid, amount)
            itemResult { +bottle }
        }.save(output)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCTankInteractionRecipeProvider) {
        override fun getName(): String = "Tank Interaction Recipes"
    }
}
