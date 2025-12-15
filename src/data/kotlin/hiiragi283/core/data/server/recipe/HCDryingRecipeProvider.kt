package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.HTSingleItemRecipeBuilder
import net.minecraft.world.item.Items

object HCDryingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Dried Kelp
        HTSingleItemRecipeBuilder
            .drying(Items.DRIED_KELP)
            .addIngredient(Items.KELP)
            .setExp(0.1f)
            .save(output)
    }
}
