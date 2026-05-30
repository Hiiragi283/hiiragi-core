package hiiragi283.core.common.storage

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.block.entity.HTBlockEntity
import net.minecraft.core.Direction

/**
 * @see mekanism.common.attachments.containers.ContainerType
 */
class HTCapabilityCodec<CONTAINER : HTValueSerializable>(
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    private val canHandle: (HTBlockEntity) -> Boolean,
) {
    companion object {
        @JvmField
        val ITEM: HTCapabilityCodec<HTItemSlot> = HTCapabilityCodec(
            HTConst.ITEMS,
            HTConst.SLOT,
            HTBlockEntity::getItemSlots,
            HTBlockEntity::hasItemHandler,
        )

        @JvmField
        val FLUID: HTCapabilityCodec<HTFluidTank> = HTCapabilityCodec(
            HTConst.FLUIDS,
            HTConst.TANK,
            HTBlockEntity::getFluidTanks,
            HTBlockEntity::hasFluidHandler,
        )

        @JvmField
        val TYPES: List<HTCapabilityCodec<*>> = listOf(ITEM, FLUID)
    }

    //    Save & Read    //

    fun saveTo(output: HTValueOutput, blockEntity: HTBlockEntity) {
        saveTo(output, getContainers(blockEntity))
    }

    fun saveTo(output: HTValueOutput, containers: List<CONTAINER>) {
        save(output.childrenList(containerTag), containers)
    }

    private fun save(list: HTValueOutput.ValueOutputList, containers: List<CONTAINER>) {
        containers.forEachIndexed { slot: Int, container: CONTAINER ->
            val output: HTValueOutput = list.addChild()
            container.serialize(output)
            if (output.isEmpty()) {
                list.discardLast()
                return@forEachIndexed
            }
            output.putInt(containerKey, slot)
        }
    }

    fun loadFrom(input: HTValueInput, blockEntity: HTBlockEntity) {
        loadFrom(input, getContainers(blockEntity))
    }

    fun loadFrom(input: HTValueInput, containers: List<CONTAINER>) {
        load(input.childrenListOrEmpty(containerTag), containers)
    }

    private fun load(list: Iterable<HTValueInput>, containers: List<CONTAINER>) {
        if (list.none()) return
        for (input: HTValueInput in list) {
            val slot: Int = input.getInt(containerKey) ?: continue
            if (slot in containers.indices) {
                containers[slot].deserialize(input)
            }
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
