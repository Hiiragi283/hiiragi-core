package hiiragi283.core.client.emi

import dev.emi.emi.api.widget.Bounds
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object HCEmiRecipeCategories {
    @JvmStatic
    private fun create(
        hasText: HTHasText,
        id: ResourceLocation,
        width: Int,
        height: Int,
        vararg workStations: ItemLike,
    ): HTEmiRecipeCategory = HTEmiRecipeCategory.create(Bounds(0, 0, width, height), hasText, id, *workStations)

    @JvmStatic
    private fun <T> create(
        recipeType: T,
        width: Int,
        height: Int,
        vararg workStations: ItemLike,
    ): HTEmiRecipeCategory where T : HTHasText, T : HTIdLike = create(recipeType, recipeType.getId(), width, height, *workStations)

    @JvmField
    val ANVIL_CRUSHING: HTEmiRecipeCategory = create(HCRecipeTypes.ANVIL_CRUSHING, 18 * 4, 18, Items.ANVIL)

    @JvmField
    val CHARGING: HTEmiRecipeCategory = create(HCRecipeTypes.CHARGING, 18 * 4, 18, Items.LIGHTNING_ROD)

    @JvmField
    val EXPLODING: HTEmiRecipeCategory = create(HCRecipeTypes.EXPLODING, 18 * 4, 18, Items.TNT)
}
