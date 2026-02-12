package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.HTBuilderMarker
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import java.util.Optional

/**
 * [InventoryChangeTrigger.TriggerInstance]のビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTInventoryChangeBuilder {
    var slots: InventoryChangeTrigger.TriggerInstance.Slots = InventoryChangeTrigger.TriggerInstance.Slots.ANY
    val predicates = ItemPredicates()

    fun build(): InventoryChangeTrigger.TriggerInstance =
        InventoryChangeTrigger.TriggerInstance(Optional.empty(), slots, predicates.toList())

    //    ItemPredicates    //

    class ItemPredicates {
        private val predicates: MutableList<ItemPredicate> = mutableListOf()

        @HTBuilderMarker
        operator fun plusAssign(builderAction: ItemPredicate.Builder.() -> Unit) {
            ItemPredicate.Builder
                .item()
                .apply(builderAction)
                .let(this::plusAssign)
        }

        operator fun plusAssign(builder: ItemPredicate.Builder) {
            this.plusAssign(builder.build())
        }

        operator fun plusAssign(predicate: ItemPredicate) {
            this.predicates += predicate
        }

        fun toList(): List<ItemPredicate> = predicates
    }
}
