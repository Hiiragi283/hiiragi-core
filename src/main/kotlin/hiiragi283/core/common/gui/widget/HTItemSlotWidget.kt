package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.integration.jei.widget.HTGhostWidget
import hiiragi283.core.api.integration.jei.widget.HTIngredientWidget
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.storage.item.HTMutableItemSlot
import hiiragi283.core.common.gui.HTContainerItemSlot
import hiiragi283.core.common.gui.sync.HTItemSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class HTItemSlotWidget private constructor(
    val contents: Either<HTItemSyncSlot, Slot>,
    val backgroundType: HTBackgroundType,
    bounds: HTBounds,
) : HTAbstractWidget(bounds),
    HTGhostWidget,
    HTIngredientWidget {
    constructor(
        slot: Slot,
        backgroundType: HTBackgroundType,
    ) : this(
        Either.Right(slot),
        backgroundType,
        HTBounds.createSlot(
            slot.x - 1,
            slot.y - 1,
        ),
    )

    constructor(slot: HTMutableItemSlot, x: Int, y: Int, backgroundType: HTBackgroundType) : this(
        HTContainerItemSlot.create(slot, x, y, backgroundType),
        backgroundType,
    )

    constructor(slot: HTItemSyncSlot, x: Int, y: Int, backgroundType: HTBackgroundType) : this(
        Either.Left(slot),
        backgroundType,
        HTBounds.createSlot(
            x - 1,
            y - 1,
        ),
    )

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
                true -> HTSyncType.BOTH
                false -> HTSyncType.S2C
            },
        )
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
        menu: AbstractContainerMenu,
        mouseX: Double,
        mouseY: Double,
        button: Int,
    ) {
        if (isGhost) {
            setStack(menu.carried.copy())
        }
    }

    //    HTIngredientWidget    //

    override fun getIngredient(): ItemStack = getStack()
}
