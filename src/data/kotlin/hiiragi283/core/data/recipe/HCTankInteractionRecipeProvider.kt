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
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.OminousBottleAmplifier
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.fluids.FluidType

class HCTankInteractionRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        emptyAndFill(Items.EXPERIENCE_BOTTLE, HCFluids.EXPERIENCE)
        emptyAndFill(Items.HONEY_BOTTLE, HCFluids.HONEY)
        emptyAndFill(Items.MUSHROOM_STEW, HCFluids.MUSHROOM_STEW, container = Items.BOWL)
        emptyAndFill(Items.DRAGON_BREATH, HCFluids.DRAGON_BREATH)

        emptyAndFill(Items.WET_SPONGE, VanillaFluidContents.WATER, FluidType.BUCKET_VOLUME, Items.SPONGE)

        repeat(5) { amplifier: Int ->
            HTTankInteractionRecipeBuilder.emptying {
                +DataComponentIngredient.of(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, OminousBottleAmplifier(amplifier), Items.OMINOUS_BOTTLE)
                fluidResult {
                    +HCFluids.OMINOUS_FLUX
                    amount = 250 * (amplifier + 1)
                }
                itemResult { +Items.GLASS_BOTTLE }
                recipeId suffix "_$amplifier"
            }.save(exporter)
        }

        // Honey Block <-> Honey
        HTTankInteractionRecipeBuilder.emptying {
            ingredient { items { +Items.HONEY_BLOCK } }
            fluidResult { +HCFluids.HONEY }
            recipeId suffix "_from_block"
        }.save(exporter)
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
            fluidIngredient { +holderSet(HCFluids.HONEY) }
            itemResult { +Items.HONEY_BLOCK }
        }.save(exporter)

        // Latex + Bowl -> Raw Rubber
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient { items { +Items.BOWL } }
            fluidIngredient { +holderSet(HCFluids.LATEX) }
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
            ingredient { items { +bottle.asItem() } }
            fluidResult {
                +fluid
                this.amount = amount
            }
            itemResult { +container }
        }.save(exporter)
        // Filling
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient { items { +container.asItem() } }
            fluidIngredient {
                +holderSet(fluid)
                this.amount = amount
            }
            itemResult { +bottle }
        }.save(exporter)
    }

    override fun getName(): String = "Tank Interaction Recipes"
}
