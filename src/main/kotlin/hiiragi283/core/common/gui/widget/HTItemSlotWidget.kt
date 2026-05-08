package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.recipe.viewer.widget.HTGhostWidget
import hiiragi283.core.api.recipe.viewer.widget.HTIngredientWidget
import hiiragi283.core.api.util.Either
import hiiragi283.core.common.gui.HTContainerItemSlot
import hiiragi283.core.common.gui.sync.HTItemSyncSlot
import hiiragi283.core.impl.gui.widget.HTAbstractWidget
import hiiragi283.core.impl.storage.item.HTItemStackResourceSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class HTItemSlotWidget private constructor(
    val contents: Either<HTItemSyncSlot, Slot>,
    val backgroundType: HTBackgroundType,
    bounds: HTBounds,
) : HTAbstractWidget(bounds),
    HTGhostWidget,
    HTIngredientWidget {
    companion object {
        @JvmStatic
        fun container(
            slot: HTItemStackResourceSlot,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ): HTItemSlotWidget = container(HTContainerItemSlot.create(slot, x, y, backgroundType), backgroundType)

        @JvmStatic
        fun container(slot: Slot, backgroundType: HTBackgroundType): HTItemSlotWidget = HTItemSlotWidget(Either.Right(slot), backgroundType, HTBounds.createSlot(slot.x - 1, slot.y - 1))

        @JvmStatic
        fun fake(
            slot: HTItemStackResourceSlot,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ): HTItemSlotWidget = fake(HTItemSyncSlot(slot), x, y, backgroundType)

        @JvmStatic
        fun fake(
            slot: HTItemSyncSlot,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ): HTItemSlotWidget = HTItemSlotWidget(Either.Left(slot), backgroundType, HTBounds.createSlot(x - 1, y - 1))
    }

    fun getStack(): ItemStack = contents.map(HTItemSyncSlot::asItemStack, Slot::getItem)

    fun setStack(stack: ItemStack) {
        contents.map({ it.asItemStack = stack }, { it.set(stack) })
    }

    val containerSlot: Slot? get() = contents.getRight()

    override fun getType(): HTWidgetType<HTItemSlotWidget> = HCWidgetTypes.ITEM_SLOT.get()

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        val slot: HTItemSyncSlot = contents.getLeft() ?: return
        widgetHolder.track(
            slot,
            when (isGhost) {
                true -> HTSyncType.C2S
                false -> HTSyncType.S2C
            },
        )
    }

    override fun onInit(access: HTWidget.Access) {
        if (containerSlot != null) {
            access.isActive = false
        }
    }

    //    HTGhostWidget    //

    private var isGhost: Boolean = false

    fun setGhost(): HTItemSlotWidget = apply { this.isGhost = true }

    override fun getGhostConsumer(): HTGhostWidget.GhostIngredientConsumer? = when {
        !isGhost -> null
        else -> HTGhostWidget.ItemConsumer { stack: Any ->
            if (stack is ItemStack) {
                setStack(stack)
            }
        }
    }

    override fun mouseClicked(
        access: HTWidget.Access,
        mouseX: Double,
        mouseY: Double,
        button: Int,
    ) {
        HiiragiCoreAPI.LOGGER.debug("Slot clicked!")
        if (isGhost) {
            val stack = access.carried.copy()
            HiiragiCoreAPI.LOGGER.debug("Tries to set stack: {}", stack)
            setStack(stack)
        }
    }

    //    HTIngredientWidget    //

    override fun getIngredient(): ItemStack = getStack()
}
