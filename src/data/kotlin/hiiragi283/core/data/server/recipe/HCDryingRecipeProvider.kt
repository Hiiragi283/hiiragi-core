package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
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
                .drying(itemCreator.fromItem(input), resultHelper.item(output))
                .setExp(0.1f)
                .save(this.output)
        }

        HTSingleItemRecipeBuilder
            .drying(itemCreator.fromItem(Items.SEA_PICKLE), resultHelper.item(Items.LIME_DYE, 2))
            .setExp(0.1f)
            .save(this.output)

        HTSingleItemRecipeBuilder
            .drying(itemCreator.fromTagKey(Tags.Items.DRINKS_HONEY), resultHelper.item(Items.SUGAR, 4))
            .setExp(0.1f)
            .saveSuffixed(this.output, "_from_honey_bottle")
        HTSingleItemRecipeBuilder
            .drying(itemCreator.fromItem(Items.HONEY_BLOCK), resultHelper.item(Items.SUGAR, 16))
            .setExp(0.4f)
            .saveSuffixed(this.output, "_from_honey_block")

        HTSingleItemRecipeBuilder
            .drying(
                itemCreator.fromItem(Items.SLIME_BALL),
                HCMaterialResultHelper.item(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER),
            ).setExp(0.1f)
            .saveSuffixed(this.output, "_from_slimeball")
    }
}
