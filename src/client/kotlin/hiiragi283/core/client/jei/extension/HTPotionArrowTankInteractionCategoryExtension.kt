package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.common.data.tank.HTPotionArrowTankInteraction
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import net.minecraft.world.item.Items

data object HTPotionArrowTankInteractionCategoryExtension : HTPotionBasedTankInteractionCategoryExtension<HTPotionArrowTankInteraction>() {
    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTPotionArrowTankInteraction, accessor: T) {
        accessor.addItemLike(Items.ARROW)
    }

    override fun supportedBottles(): Iterable<HTBottleType> = listOf(HTBottleType.LINGERING)

    override fun onDisplayedIngredientsUpdate(
        recipe: HTPotionArrowTankInteraction,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        contents: BottledPotionContents,
    ) {
        HTPotionHelper
            .createPotion(Items.TIPPED_ARROW, contents.contents)
            .let(filledSlot.createDisplayOverrides()::addItemStack)
    }
}
