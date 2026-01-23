package hiiragi283.core.common.item

import hiiragi283.core.api.data.recipe.creator.HTIngredientCreator
import hiiragi283.core.api.item.HTColoredNameItem
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCItems
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import java.awt.Color
import java.util.function.Consumer

class HTChromaticPowderItem(properties: Properties) :
    HTColoredNameItem(properties.component(HCDataComponents.COMPLETE_PROGRESS, 0)),
    HTSubCreativeTabContents {
    companion object {
        const val MAX_COMPLETE = 5

        @JvmStatic
        fun createStack(complete: Int): ItemStack = createItemStack(HCItems.CHROMATIC_POWDER, HCDataComponents.COMPLETE_PROGRESS, complete)

        @JvmStatic
        fun createIngredient(complete: Int): HTItemIngredient = HTIngredientCreator.create(
            false,
            HCItems.CHROMATIC_POWDER,
        ) { expect(HCDataComponents.COMPLETE_PROGRESS, complete) }

        @JvmStatic
        fun getColor(stack: ItemStack): Int {
            val complete: Int = stack.getOrDefault(HCDataComponents.COMPLETE_PROGRESS, 0)
            return when (complete) {
                1 -> Color(0xff9966)
                2 -> Color(0x99ff66)
                3 -> Color(0x66ff99)
                4 -> Color(0x6699ff)
                5 -> Color(0x9966ff)
                else -> Color(0xff6699)
            }.rgb
        }
    }

    override fun getNameColor(stack: ItemStack): TextColor = TextColor.fromRgb(getColor(stack))

    //    HTSubCreativeTabContents    //

    override fun addItems(
        baseItem: HTItemHolderLike<*>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        consumer: Consumer<ItemStack>,
    ) {
        (0..MAX_COMPLETE).map(::createStack).forEach(consumer)
    }

    override fun shouldAddDefault(): Boolean = false
}
