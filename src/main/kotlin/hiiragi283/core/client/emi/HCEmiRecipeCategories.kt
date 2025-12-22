package hiiragi283.core.client.emi

import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object HCEmiRecipeCategories {
    @JvmField
    val MACHINE_BOUNDS = HTBounds(0, 0, 7 * 18, 3 * 18)

    @JvmStatic
    private fun create(hasText: HTHasText, id: ResourceLocation, vararg workStations: ItemLike): HTEmiRecipeCategory =
        HTEmiRecipeCategory.create(MACHINE_BOUNDS, hasText, id, *workStations)

    @JvmStatic
    private fun <T> create(recipeType: T, vararg workStations: ItemLike): HTEmiRecipeCategory where T : HTHasText, T : HTIdLike =
        create(recipeType, recipeType.getId(), *workStations)

    @JvmField
    val CHARGING: HTEmiRecipeCategory = create(HCRecipeTypes.CHARGING, Items.LIGHTNING_ROD)

    @JvmField
    val CRUSHING: HTEmiRecipeCategory = create(HCRecipeTypes.CRUSHING, Items.ANVIL)

    @JvmField
    val DRYING: HTEmiRecipeCategory = create(HCRecipeTypes.DRYING, Items.MAGMA_BLOCK)

    @JvmField
    val EXPLODING: HTEmiRecipeCategory = create(HCRecipeTypes.EXPLODING, Items.TNT)
}
