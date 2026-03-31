package hiiragi283.core.impl.transfer

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.transfer.HTResourceHandler
import hiiragi283.core.api.transfer.HTResourceSlot
import hiiragi283.core.api.transfer.energy.HTEnergyBattery
import hiiragi283.core.api.transfer.fluid.HTFluidTank
import hiiragi283.core.api.transfer.item.HTItemSlot
import hiiragi283.core.impl.block.entity.HTBlockEntity
import net.minecraft.core.Direction
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.resource.Resource
import kotlin.jvm.optionals.getOrNull

class HTCapabilityCodec<CONTAINER : ValueIOSerializable>(
    private val containerTag: String,
    private val containerKey: String,
    private val blockEntityGetter: (HTBlockEntity, Direction?) -> List<CONTAINER>,
    private val canHandle: (HTBlockEntity) -> Boolean,
) {
    companion object {
        @JvmField
        val ENERGY: HTCapabilityCodec<HTEnergyBattery> = HTCapabilityCodec(
            HTConst.BATTERIES,
            HTConst.BATTERY,
            { blockEntity: HTBlockEntity, side: Direction? -> listOfNotNull(blockEntity.energyHandler.getBattery(side)) },
            { blockEntity: HTBlockEntity -> blockEntity.energyHandler.hasEnergyHandler() },
        )

        @JvmField
        val FLUID: HTCapabilityCodec<HTFluidTank> = create(HTConst.FLUIDS, HTConst.TANK, HTBlockEntity::fluidHandler)

        @JvmField
        val ITEM: HTCapabilityCodec<HTItemSlot> = create(HTConst.ITEMS, HTConst.SLOT, HTBlockEntity::itemHandler)

        @JvmField
        val TYPES: List<HTCapabilityCodec<*>> = listOf(ITEM, ENERGY, FLUID)

        @JvmStatic
        private fun <T : Resource> create(
            containerTag: String,
            containerKey: String,
            blockEntityGetter: (HTBlockEntity) -> HTResourceHandler<T>,
        ): HTCapabilityCodec<HTResourceSlot<T>> = HTCapabilityCodec(
            containerTag,
            containerKey,
            { blockEntity: HTBlockEntity, side: Direction? -> blockEntityGetter(blockEntity).getSlots(side) },
            { blockEntity: HTBlockEntity -> blockEntityGetter(blockEntity).hasResourceHandler() },
        )
    }

    //    Save & Read    //

    fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity) {
        saveTo(output, getContainers(blockEntity))
    }

    fun saveTo(output: ValueOutput, containers: List<CONTAINER>) {
        save(output.childrenList(containerTag), containers)
    }

    private fun save(list: ValueOutput.ValueOutputList, containers: List<CONTAINER>) {
        containers.forEachIndexed { slot: Int, container: CONTAINER ->
            val output: ValueOutput = list.addChild()
            container.serialize(output)
            if (output.isEmpty) {
                list.discardLast()
                return@forEachIndexed
            }
            output.putInt(containerKey, slot)
        }
    }

    fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity) {
        loadFrom(input, getContainers(blockEntity))
    }

    fun loadFrom(input: ValueInput, containers: List<CONTAINER>) {
        load(input.childrenListOrEmpty(containerTag), containers)
    }

    private fun load(list: Iterable<ValueInput>, containers: List<CONTAINER>) {
        if (list.none()) return
        for (input: ValueInput in list) {
            val slot: Int = input.getInt(containerKey).getOrNull() ?: continue
            if (slot in containers.indices) {
                containers[slot].deserialize(input)
            }
        }
    }

    fun getContainers(blockEntity: HTBlockEntity): List<CONTAINER> = blockEntityGetter(blockEntity, null)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
