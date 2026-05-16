package hiiragi283.lib.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.sync.HTItemSyncSlot
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.math.HTBounds
import hiiragi283.lib.recipe.viewer.widget.HTGhostWidget
import hiiragi283.lib.recipe.viewer.widget.HTIngredientWidget
import hiiragi283.lib.resource.toId
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

sealed interface HTItemWidget :
    HTWidget,
    HTGhostWidget,
    HTIngredientWidget {
    companion object {
        @JvmField
        val TYPE: HTWidgetType<HTItemWidget> = HTWidgetType.Simple(HTConstants.MOD_ID.toId(HTConstants.ITEM))
    }

    val backgroundType: HTBackgroundType

    fun getStack(): ItemStack

    fun setStack(stack: ItemStack)

    override fun getType(): HTWidgetType<*> = TYPE

    override fun getIngredient(): ItemStack = getStack()

    //    Container    //

    data class Container(val slot: Slot, override val backgroundType: HTBackgroundType) : HTItemWidget {
        override fun getStack(): ItemStack = slot.item

        override fun setStack(stack: ItemStack) {
            slot.set(stack)
        }

        override val bounds: HTBounds = HTBounds.createSlot(slot.x - 1, slot.y - 1)

        override fun onInit(access: HTWidget.Access) {
            access.isActive = false
        }

        override fun getGhostConsumer(): HTGhostWidget.GhostIngredientConsumer? = null
    }

    //    Fake    //

    data class Fake(val slot: HTItemSyncSlot, override val bounds: HTBounds, override val backgroundType: HTBackgroundType, val isGhost: Boolean) : HTItemWidget {
        constructor(slot: HTItemSyncSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(slot, HTBounds.createSlot(x - 1, y - 1), backgroundType, isGhost)

        override fun getStack(): ItemStack = slot.asItemStack

        override fun setStack(stack: ItemStack) {
            slot.asItemStack = stack
        }

        override fun setupHolder(widgetHolder: HTWidgetHolder) {
            widgetHolder.track(
                slot,
                when (isGhost) {
                    true -> HTSyncType.C2S
                    false -> HTSyncType.S2C
                },
            )
        }

        override fun mouseClicked(access: HTWidget.Access, mouseX: Double, mouseY: Double, button: Int) {
            if (isGhost) {
                access.carried.copy().let(::setStack)
            }
        }

        override fun getGhostConsumer(): HTGhostWidget.ItemConsumer = HTGhostWidget.ItemConsumer { stack: Any ->
            if (stack is ItemStack) {
                setStack(stack)
            }
        }
    }
}
