package hiiragi283.core.common.storage

import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.storage.attachments.HTAttachedContainers
import hiiragi283.core.api.storage.attachments.HTAttachedEnergy
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack

/**
 * @see mekanism.common.attachments.containers.ContainerType
 */
class HTCapabilityCodec<CONTAINER : HTDataSerializable, ATTACHED : HTAttachedContainers<*, ATTACHED>>(
    private val component: DataComponentType<ATTACHED>,
    private val attachedFactory: (Int) -> ATTACHED,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    private val canHandle: (HTBlockEntity) -> Boolean,
    private val copyTo: (HTBlockEntity, List<CONTAINER>, ATTACHED) -> Unit,
    private val copyFrom: (HTBlockEntity, List<CONTAINER>) -> ATTACHED?,
) {
    companion object {
        @JvmField
        val ITEM: HTCapabilityCodec<HTItemSlot, HTAttachedItems> = HTCapabilityCodec(
            HCDataComponents.ITEM,
            HTAttachedItems::create,
            HTBlockEntity::getItemSlots,
            HTBlockEntity::hasItemHandler,
            HTBlockEntity::applyItemSlots,
            HTBlockEntity::collectItemSlots,
        )

        @JvmField
        val ENERGY: HTCapabilityCodec<HTEnergyBattery, HTAttachedEnergy> = HTCapabilityCodec(
            HCDataComponents.ENERGY,
            HTAttachedEnergy::create,
            HTBlockEntity::getEnergyBattery.andThen(::listOfNotNull),
            HTBlockEntity::hasEnergyStorage,
            HTBlockEntity::applyEnergyBattery,
            HTBlockEntity::collectEnergyBattery,
        )

        @JvmField
        val FLUID: HTCapabilityCodec<HTFluidTank, HTAttachedFluids> = HTCapabilityCodec(
            HCDataComponents.FLUID,
            HTAttachedFluids::create,
            HTBlockEntity::getFluidTanks,
            HTBlockEntity::hasFluidHandler,
            HTBlockEntity::applyFluidTanks,
            HTBlockEntity::collectFluidTanks,
        )

        @JvmField
        val TYPES: List<HTCapabilityCodec<*, *>> = listOf(ITEM, ENERGY, FLUID)
    }

    //    Data Component    //

    fun getOrCreate(stack: ItemStack, size: Int): ATTACHED = stack.getOrDefault(component, attachedFactory(size))

    fun updateAttached(stack: ItemStack, attached: ATTACHED) {
        if (attached.isEmpty()) {
            stack.remove(component)
        } else {
            stack.set(component, attached)
        }
    }

    /**
     * @see mekanism.common.attachments.containers.ContainerType.copyToTile
     */
    fun copyTo(blockEntity: HTBlockEntity, getter: (DataComponentType<ATTACHED>) -> ATTACHED?) {
        val component: ATTACHED = getter(this.component) ?: return
        copyTo(blockEntity, getContainers(blockEntity), component)
    }

    /**
     * @see mekanism.common.attachments.containers.ContainerType.copyFromTile
     */
    fun copyFrom(blockEntity: HTBlockEntity, builder: DataComponentMap.Builder) {
        val containers: List<CONTAINER> = getContainers(blockEntity)
        if (!containers.isEmpty()) {
            val component: ATTACHED = copyFrom(blockEntity, containers) ?: return
            builder.set(this.component, component)
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
