package hiiragi283.core.common.storage.item

import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.component.HTComponentHandler
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * [HTItemHandler]に基づいたコンポーネント向けの実装
 * @see mekanism.common.attachments.containers.item.ComponentBackedItemHandler
 */
class HTComponentItemHandler(attachedTo: ItemStack, size: Int, containerFactory: ContainerFactory<HTItemSlot>) :
    HTComponentHandler<ItemStack, HTItemSlot, HTAttachedItems>(attachedTo, size, containerFactory),
    HTItemHandler {
    override fun capabilityCodec(): HTCapabilityCodec<HTItemSlot, HTAttachedItems> = HTCapabilityCodec.ITEM

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = getContainers()

    override fun getItemSlot(slot: Int, side: Direction?): HTItemSlot = getContainer(slot)

    override fun getSlots(side: Direction?): Int = size

    override fun getStackInSlot(slot: Int, side: Direction?): ItemStack = getContents(slot) ?: ItemStack.EMPTY
}
