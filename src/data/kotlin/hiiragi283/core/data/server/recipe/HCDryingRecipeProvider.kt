package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HCDryingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        mapOf(
            Items.CLAY to Items.MUD,
            Items.COARSE_DIRT to Items.DIRT,
            Items.LEATHER to Items.ROTTEN_FLESH,
            Items.SPONGE to Items.WET_SPONGE,
            Items.DRIED_KELP to Items.KELP,
        ).forEach { (output: Item, input: Item) ->
            HTSingleItemRecipeBuilder
                .drying(output)
                .addIngredient(input)
                .setExp(0.1f)
                .save(this.output)
        }

        HTSingleItemRecipeBuilder
            .drying(Items.LIME_DYE, 2)
            .addIngredient(Items.SEA_PICKLE)
            .setExp(0.1f)
            .save(this.output)

        HTSingleItemRecipeBuilder
            .drying(Items.SUGAR, 4)
            .addIngredient(Tags.Items.DRINKS_HONEY)
            .setExp(0.1f)
            .saveSuffixed(this.output, "_from_honey_bottle")
        HTSingleItemRecipeBuilder
            .drying(Items.SUGAR, 16)
            .addIngredient(Items.HONEY_BLOCK)
            .setExp(0.4f)
            .saveSuffixed(this.output, "_from_honey_block")

        HTSingleItemRecipeBuilder
            .drying(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER))
            .addIngredient(Tags.Items.SLIME_BALLS)
            .setExp(0.1f)
            .saveSuffixed(this.output, "_from_slimeball")
    }
}
