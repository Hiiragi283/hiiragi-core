package hiiragi283.core.api.gui.element

import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.MCSprites
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import com.lowdragmc.lowdraglib2.integration.xei.emi.LDLibEMIPlugin
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import dev.emi.emi.api.stack.EmiStackInteraction
import hiiragi283.core.api.integration.emi.slot.HTListItemSlot
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.extractItem
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.insert
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.Optional
import java.util.function.Consumer
import kotlin.math.min

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[ItemSlot]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
@LDLRegister(name = "hiiragi-item-slot", group = "inventory", registry = "ldlib2:ui_element")
class HTItemSlotElement : ItemSlot {
    private val slot: HTItemSlot?

    constructor() : super() {
        this.slot = null
    }

    constructor(
        slot: HTItemSlot,
        stackSetter: Consumer<ItemStack>,
        manualFilter: (HTItemResourceType, HTStorageAccess) -> Boolean,
    ) : super(ContainerSlot(slot, 0, 0, stackSetter, manualFilter)) {
        this.slot = slot
    }

    constructor(slot: HTItemSlot.Basic) : this(slot, slot::setStack, slot::isStackValidForInsert)

    init {
        style.backgroundTexture(MCSprites.RECT_1)
        LDLibEMIPlugin.stackProvider(this) {
            if (slot is HTListItemSlot) {
                return@stackProvider EmiStackInteraction(slot.getIngredient(), null, false)
            }
            // デフォルトの実装
            val stack: ItemStack = this.value
            if (stack.isEmpty) return@stackProvider null
            EmiStackInteraction(stack.toEmi(), null, false)
        }
    }

    override fun xeiRecipeIngredient(io: IngredientIO): HTItemSlotElement {
        LDLibEMIPlugin.recipeIngredient(this, io) {
            when (slot) {
                is HTListItemSlot -> listOf(slot.getIngredient())
                else -> listOf(this.value.toEmi())
            }
        }
        return this
    }

    override fun xeiRecipeSlot(io: IngredientIO, chance: Float): HTItemSlotElement {
        LDLibEMIPlugin.recipeSlot(this) {
            when (slot) {
                is HTListItemSlot -> slot.getIngredient()
                else -> this.value.toEmi()
            }.setChance(chance)
        }
        return this
    }

    override fun slotStyle(style: Consumer<SlotStyle>): HTItemSlotElement {
        super.slotStyle(style)
        return this
    }

    override fun setItem(item: ItemStack): HTItemSlotElement {
        super.setItem(item)
        return this
    }

    override fun setItem(itemStack: ItemStack, notify: Boolean): HTItemSlotElement {
        super.setItem(itemStack, notify)
        return this
    }

    override fun setValue(value: ItemStack?): HTItemSlotElement {
        super.setValue(value)
        return this
    }

    override fun setValue(value: ItemStack?, notify: Boolean): HTItemSlotElement {
        super.setValue(value, notify)
        return this
    }

    //    ContainerSlot    //

    private class ContainerSlot(
        val slot: HTItemSlot,
        x: Int,
        y: Int,
        private val stackSetter: Consumer<ItemStack>,
        private val manualFilter: (HTItemResourceType, HTStorageAccess) -> Boolean,
    ) : Slot(emptyContainer, 0, x, y) {
        companion object {
            @JvmStatic
            private val emptyContainer = SimpleContainer(0)
        }

        fun updateCount(count: Int) {
            stackSetter.accept(slot.getResource()?.toStack(count) ?: ItemStack.EMPTY)
            setChanged()
        }

        private fun insertItem(stack: ItemStack, action: HTStorageAction): ItemStack {
            val remainder: ItemStack = slot.insert(stack, action, HTStorageAccess.MANUAL)
            if (action.execute() && stack.count != remainder.count) {
                setChanged()
            }
            return remainder
        }

        override fun mayPlace(stack: ItemStack): Boolean {
            val resourceType: HTItemResourceType = stack.toResource() ?: return false
            if (slot.getResource() == null) {
                return insertItem(stack, HTStorageAction.SIMULATE).count < stack.count
            }
            if (slot.extract(1, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL) == 0) return false
            return manualFilter(resourceType, HTStorageAccess.MANUAL)
        }

        override fun getItem(): ItemStack = slot.getItemStack()

        override fun hasItem(): Boolean = slot.getResource() != null

        override fun set(stack: ItemStack) {
            stackSetter.accept(stack)
            setChanged()
        }

        override fun getMaxStackSize(): Int = slot.getCapacity()

        override fun getMaxStackSize(stack: ItemStack): Int = slot.getCapacity(stack.toResource())

        override fun mayPickup(player: Player): Boolean = slot.extract(1, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL) > 0

        override fun remove(amount: Int): ItemStack = slot.extractItem(amount, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)

        override fun tryRemove(count: Int, decrement: Int, player: Player): Optional<ItemStack> {
            if (!mayPickup(player)) {
                return Optional.empty()
            }
            val count: Int = min(count, decrement)
            val stack: ItemStack = remove(count)
            if (stack.isEmpty) {
                return Optional.empty()
            } else if (item.isEmpty) {
                setByPlayer(ItemStack.EMPTY, stack)
            }
            return Optional.of(stack)
        }
    }
}
