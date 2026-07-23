@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.util.HTDelegates
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.MinMaxBounds

class HTInventoryChangeBuilder {
    @PublishedApi internal val predicates: MutableList<ItemPredicate> = mutableListOf()

    @PublishedApi internal var slots: InventoryChangeTrigger.TriggerInstance.Slots by HTDelegates.onceInitialize(InventoryChangeTrigger.TriggerInstance.Slots::ANY)

    operator fun ItemPredicate.unaryPlus() {
        predicates += this
    }

    operator fun ItemPredicate.Builder.unaryPlus() {
        +this.build()
    }

    operator fun InventoryChangeTrigger.TriggerInstance.Slots.unaryPlus() {
        slots = this
    }

    inline fun slots(builderAction: SlotsBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        slots = SlotsBuilder().apply(builderAction).build()
    }

    class SlotsBuilder {
        var occupied: MinMaxBounds.Ints = MinMaxBounds.Ints.ANY
        var full: MinMaxBounds.Ints = MinMaxBounds.Ints.ANY
        var empty: MinMaxBounds.Ints = MinMaxBounds.Ints.ANY

        fun build(): InventoryChangeTrigger.TriggerInstance.Slots = InventoryChangeTrigger.TriggerInstance.Slots(occupied, full, empty)
    }

    fun build(): InventoryChangeTrigger.TriggerInstance = InventoryChangeTrigger.TriggerInstance(Optional.empty(), slots, predicates)
}
