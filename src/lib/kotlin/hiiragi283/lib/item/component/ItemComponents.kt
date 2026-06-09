@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.item.component

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.ItemAttributeModifiers

fun Item.Properties.consumables(consumable: Consumable): Item.Properties = this.component(DataComponents.CONSUMABLE, consumable)

//    ItemAttributeModifiers    //

inline fun buildItemAttributeModifiers(builderAction: ItemAttributeModifiers.Builder.() -> Unit): ItemAttributeModifiers {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return ItemAttributeModifiers.builder().apply(builderAction).build()
}
