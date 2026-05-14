package hiiragi283.lib.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

interface HTItemLike<ITEM : Item> : ItemLike {
    override fun asItem(): ITEM

    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<ItemStackTemplate> = createItemTemplate(this, count, patch)

    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = createItemStack(this, count, patch)
}
