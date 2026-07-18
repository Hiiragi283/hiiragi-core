package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTTankInteractionRecipeBuilder
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
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
                +DataComponentIngredient.of(false, DataComponents.OMINOUS_BOTTLE_AMPLIFIER, amplifier, Items.OMINOUS_BOTTLE)
                fluidResult {
                    +HCFluids.OMINOUS_FLUX.get()
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
            fluidIngredient { +HCFluids.HONEY }
            result { +Items.HONEY_BLOCK }
        }.save(exporter)

        // Latex + Bowl -> Raw Rubber
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient { items { +Items.BOWL } }
            fluidIngredient { +HCFluids.LATEX }
            result {
                +HCItems.RAW_RUBBER
                count = 4
            }
        }.save(exporter)
        // Raw Rubber -> Rubber Bar
        HTCookingRecipeBuilder.smelting {
            ingredient { items { +HCItems.RAW_RUBBER } }
            +HCItems.CURED_RUBBER.toStack()
            exp = 0.7f
        }.save(exporter)
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
                +fluid
                this.amount = amount
            }
            result { +bottle }
        }.save(exporter)
    }

    override fun getName(): String = "Tank Interaction Recipes"
}
