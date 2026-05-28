package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.recipe.viewer.widget.HTGhostWidget
import hiiragi283.core.api.recipe.viewer.widget.HTIngredientWidget
import hiiragi283.core.common.gui.HTContainerItemSlot
import hiiragi283.core.impl.storage.item.HTItemStackResourceSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

private typealias ItemStackSetter = (ItemStack) -> Unit

sealed class HTItemWidget(val backgroundType: HTBackgroundType) :
    HTWidget,
    HTIngredientWidget {
    abstract fun getStack(): ItemStack

    final override fun getType(): HTWidgetType<*> = HCWidgetTypes.ITEM.get()

    final override fun getIngredient(): ItemStack = getStack()

    //    Container    //

    class Container(val slot: Slot, backgroundType: HTBackgroundType) : HTItemWidget(backgroundType) {
        constructor(slot: HTItemStackResourceSlot, x: Int, y: Int, backgroundType: HTBackgroundType) : this(HTContainerItemSlot.create(slot, x, y, backgroundType), backgroundType)

        override fun getStack(): ItemStack = slot.item

        override val bounds: HTBounds = HTBounds.createSlot(slot.x - 1, slot.y - 1)

        override fun onInit(access: HTWidget.Access) {
            access.isActive = false
        }
    }

    //    Fake    //

    class Fake(private val stackGetter: () -> ItemStack, private val stackSetter: ItemStackSetter?, override val bounds: HTBounds, backgroundType: HTBackgroundType, val isGhost: Boolean) :
        HTItemWidget(backgroundType),
        HTGhostWidget {
        constructor(stackGetter: () -> ItemStack, stackSetter: ItemStackSetter?, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(stackGetter, stackSetter, HTBounds.createSlot(x - 1, y - 1), backgroundType, isGhost)

        constructor(slot: HTItemStackResourceSlot, bounds: HTBounds, backgroundType: HTBackgroundType, isGhost: Boolean) : this(slot::getStack, slot::setStack, bounds, backgroundType, isGhost)

        constructor(slot: HTItemStackResourceSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(slot, HTBounds.createSlot(x - 1, y - 1), backgroundType, isGhost)

        override fun getStack(): ItemStack = stackGetter()

        override fun mouseClicked(access: HTWidget.Access, mouseX: Double, mouseY: Double, button: Int) {
            if (isGhost) {
                stackSetter?.invoke(access.carried.copy())
            }
        }

        override fun getGhostConsumer(): HTGhostWidget.ItemConsumer = HTGhostWidget.ItemConsumer { stack: Any ->
            if (stack is ItemStack) {
                stackSetter?.invoke(stack)
            }
        }
    }
}
