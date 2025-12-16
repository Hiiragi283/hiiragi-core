package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.item.HTHammerItem
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.setup.HCItems
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HCToolRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        createHammer()
    }

    @JvmStatic
    private fun createHammer() {
        for (material: HCMaterial in HCMaterial.entries) {
            val hammer: ItemLike = HCItems.TOOLS[HTHammerItem.ToolType, material] ?: continue
            // Shaped
            HTShapedRecipeBuilder
                .create(hammer)
                .pattern(
                    "AAA",
                    "ABA",
                    " B ",
                ).define('A', material.getBaseIngredient())
                .define('B', Tags.Items.RODS_WOODEN)
                .save(output)
        }
    }
}
