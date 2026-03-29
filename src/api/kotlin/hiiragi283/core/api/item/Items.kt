package hiiragi283.core.api.item

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentType
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

//    Item    //

fun <T : Any, R : Any> Item.Properties.delayedComponent(
    type: DataComponentType<R>,
    key: ResourceKey<T>,
    transform: (Holder<T>) -> R,
): Item.Properties = this.delayedComponent(type) { provider: HolderLookup.Provider -> provider.getOrThrow(key).let(transform) }

fun <T : Any, R : Any> Item.Properties.delayedComponent(
    type: DataComponentType<R>,
    tagKey: TagKey<T>,
    transform: (HolderSet<T>) -> R,
): Item.Properties = this.delayedComponent(type) { provider: HolderLookup.Provider -> provider.getOrThrow(tagKey).let(transform) }
