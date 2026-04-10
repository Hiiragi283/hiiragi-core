package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.common.data.tank.HTPotionTankInteraction
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import net.minecraft.world.item.Items

data object HTPotionTankInteractionCE : HTPotionBasedTankInteractionCE<HTPotionTankInteraction>() {
    override val canFill: Boolean = true
    override val canEmpty: Boolean = true

    override fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: HTPotionTankInteraction, accessor: T) {
        accessor.addItemLike(Items.GLASS_BOTTLE)
    }

    override fun supportedBottles(): Iterable<HTBottleType> = HTBottleType.entries

    override fun onDisplayedIngredientsUpdate(
        recipe: HTPotionTankInteraction,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        contents: BottledPotionContents,
    ) {
        contents.let(HTPotionHelper::createPotion).let(filledSlot.createDisplayOverrides()::addItemStack)
    }
}
