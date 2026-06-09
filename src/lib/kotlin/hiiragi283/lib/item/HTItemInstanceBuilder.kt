@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.item

import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.right
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items

@HTBuilderMarker
class HTItemInstanceBuilder {
    companion object {
        @JvmStatic
        inline fun buildTemplate(builderAction: HTItemInstanceBuilder.() -> Unit): HTTextResult<ItemStackTemplate> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTItemInstanceBuilder().apply(builderAction).buildTemplate()
        }

        @JvmStatic
        inline fun buildStack(builderAction: HTItemInstanceBuilder.() -> Unit): ItemStack {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return buildTemplate(builderAction).map(ItemStackTemplate::create).getOrElse { ItemStack.EMPTY }
        }
    }

    val item: ItemHolder = ItemHolder()
    var count: Int = 1
    var patch: DataComponentPatch = DataComponentPatch.EMPTY

    fun buildTemplate(): HTTextResult<ItemStackTemplate> {
        val item: Holder<Item> = this.item.value ?: return HTTextResult("Item must be non-empty")
        check(count >= 0) { "Count must not be negative" }
        if (count == 0) return HTTextResult("Count must be positive")
        return ItemStackTemplate(item, count, patch).right()
    }

    @Suppress("DEPRECATION")
    @HTBuilderMarker
    class ItemHolder {
        var value: Holder<Item>? = null

        operator fun plusAssign(item: Item) {
            plusAssign(item.builtInRegistryHolder())
        }

        operator fun plusAssign(holder: Holder<Item>) {
            check(value == null) { "Item has already initialized" }
            if (holder.`is`(Items.AIR.builtInRegistryHolder())) return
            value = holder
        }
    }
}
