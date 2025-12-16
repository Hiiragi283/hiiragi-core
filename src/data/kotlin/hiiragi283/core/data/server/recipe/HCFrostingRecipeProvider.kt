package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMoltenCrystalData
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HCFrostingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Water -> Ice
        HTSingleItemRecipeBuilder
            .frosting(Items.ICE)
            .addIngredient(Tags.Items.BUCKETS_WATER)
            .setExp(0.3f)
            .save(output)
        // Lava -> Obsidian
        HTSingleItemRecipeBuilder
            .frosting(Items.OBSIDIAN)
            .addIngredient(Tags.Items.BUCKETS_LAVA)
            .setExp(0.3f)
            .save(output)

        // Molten
        for (data: HCMoltenCrystalData in HCMoltenCrystalData.entries) {
            val material: HCMaterial = data.material
            HTSingleItemRecipeBuilder
                .frosting(HCItems.MATERIALS.getOrThrow(material.basePrefix, material))
                .addIngredient(data.molten.bucketTag)
                .setExp(0.5f)
                .saveSuffixed(output, "_from_molten")
        }
    }
}
