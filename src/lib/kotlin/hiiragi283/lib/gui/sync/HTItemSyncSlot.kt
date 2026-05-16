package hiiragi283.lib.gui.sync

import hiiragi283.lib.transfer.item.ItemResourceHandler
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import net.neoforged.neoforge.transfer.IndexModifier
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler

/**
 * [ItemStack]向けの[HTIntSyncSlot]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTItemSyncSlot(private val getter: Supplier<ItemStack>, private val setter: Consumer<ItemStack>) : HTIntSyncSlot {
    constructor(property: KMutableProperty0<ItemStack>) : this(property::get, property::set)

    constructor(handler: ItemStacksResourceHandler, index: Int) : this(handler, index, handler::set)

    constructor(handler: ItemResourceHandler, index: Int, modifier: IndexModifier<ItemResource>) : this(
        { handler.getItemStack(index) },
        { stack: ItemStack ->
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            modifier.set(index, resource, amount)
        },
    )

    private var lastStack: ItemStack = ItemStack.EMPTY

    var asItemStack: ItemStack
        get() = this.getter.get()
        set(value) {
            this.setter.accept(value)
        }

    override var amountAsInt: Int
        get() = asItemStack.count
        set(value) {
            asItemStack = asItemStack.copyWithCount(value)
        }

    override fun getChange(): HTChangeType? {
        val current: ItemStack = this.asItemStack
        if (current.isEmpty && lastStack.isEmpty) {
            return null
        }
        val sameItem: Boolean = ItemStack.isSameItemSameComponents(current, lastStack)
        if (!sameItem || this.amountAsInt != this.lastStack.count) {
            this.lastStack = current.copy()
            return when {
                sameItem -> HTChangeType.PARTIAL
                else -> HTChangeType.FULL
            }
        }
        return null
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload? = when (changeType) {
        HTChangeType.PARTIAL -> HTIntSyncPayload(this.amountAsInt)
        HTChangeType.FULL -> HTItemSyncPayload(this.asItemStack.copy())
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): ItemStack = asItemStack

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ItemStack) {
        asItemStack = value
    }
}
