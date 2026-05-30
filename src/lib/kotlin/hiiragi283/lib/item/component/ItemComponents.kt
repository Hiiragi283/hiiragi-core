package hiiragi283.lib.item.component

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.ItemAttributeModifiers

fun Item.Properties.consumables(consumable: Consumable): Item.Properties = this.component(DataComponents.CONSUMABLE, consumable)

//    ItemAttributeModifiers    //

inline fun buildItemAttributeModifiers(builderAction: ItemAttributeModifiers.Builder.() -> Unit): ItemAttributeModifiers = ItemAttributeModifiers.builder().apply(builderAction).build()
