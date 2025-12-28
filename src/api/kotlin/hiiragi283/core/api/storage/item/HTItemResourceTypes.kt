package hiiragi283.core.api.storage.item

import net.minecraft.world.item.ItemStack

fun ItemStack.toResource(): HTItemResourceType? = HTItemResourceType.of(this)
