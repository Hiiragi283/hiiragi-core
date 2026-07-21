package hiiragi283.core.support.capability

import hiiragi283.core.api.capability.HTMultiCapability
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.energy.IEnergyStorage

data object HTEnergyCapabilities : HTMultiCapability.Simple<IEnergyStorage> {
    override val block: BlockCapability<IEnergyStorage, Direction?> get() = Capabilities.EnergyStorage.BLOCK
    override val entity: EntityCapability<IEnergyStorage, Direction?> get() = Capabilities.EnergyStorage.ENTITY
    override val item: ItemCapability<IEnergyStorage, Void?> get() = Capabilities.EnergyStorage.ITEM

    fun wrap(storage: IEnergyStorage): HTEnergyHandler = when (storage) {
        is HTEnergyHandler -> storage
        else -> object : HTEnergyHandler, HTValueSerializable.Empty {
            override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = storage.receiveEnergy(amount, action.simulate())

            override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = storage.extractEnergy(amount, action.simulate())

            override fun getAmount(): Int = storage.energyStored

            override fun getCapacity(): Int = storage.maxEnergyStored
        }
    }

    fun getHandler(level: Level, pos: BlockPos, side: Direction?): HTEnergyHandler? = getCapability(level, pos, side)?.let(::wrap)

    fun getHandler(entity: Entity, side: Direction?): HTEnergyHandler? = getCapability(entity, side)?.let(::wrap)

    fun getHandler(stack: ItemStack): HTEnergyHandler? = getCapability(stack)?.let(::wrap)

    fun getHandler(resource: HTItemResourceType?): HTEnergyHandler? = getCapability(resource)?.let(::wrap)
}
