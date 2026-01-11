package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.block.entity.HTOwnedBlockEntity
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.resolver.HTEnergyStorageManager
import hiiragi283.core.common.storage.resolver.HTFluidHandlerManager
import hiiragi283.core.common.storage.resolver.HTItemHandlerManager
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.Nameable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import java.util.UUID

/**
 * キャパビリティやオーナーを保持する[HTExtendedBlockEntity]の拡張クラス
 * @see mekanism.common.tile.base.TileEntityMekanism
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(type, pos, state),
    Nameable,
    HTEnergyHandler,
    HTFluidHandler,
    HTHandlerProvider,
    HTItemHandler,
    HTOwnedBlockEntity,
    HTSoundPlayerBlockEntity {
    //    Ticking    //

    companion object {
        /**
         * @see mekanism.common.tile.base.TileEntityMekanism.tickClient
         */
        @JvmStatic
        fun tickClient(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            blockEntity.onUpdateClient(level, pos, state)
            blockEntity.ticks++
        }

        /**
         * @see mekanism.common.tile.base.TileEntityMekanism.tickServer
         */
        @JvmStatic
        fun tickServer(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            val serverLevel: ServerLevel = level as? ServerLevel ?: return
            blockEntity.onUpdateServer(serverLevel, pos, state)
            blockEntity.ticks++
        }
    }

    var ticks: Int = 0
        protected set

    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState)

    open fun onBlockRemoved(state: BlockState, level: Level, pos: BlockPos) {}

    //    Nameable    //

    @DescSynced
    @Persisted(key = "custom_name")
    private var customName: Component? = null

    final override fun getName(): Component = customName ?: blockState.block.name

    final override fun getCustomName(): Component? = customName

    //    HTOwnedBlockEntity    //

    @DescSynced
    @Persisted(key = "owner")
    var ownerId: UUID? = null

    final override fun getOwner(): UUID? = ownerId

    //    HTSoundPlayerBlockEntity    //

    override fun getSoundPos(): BlockPos = blockPos

    override fun playSound(sound: SoundEvent, volume: Float, pitch: Float) {
        level?.playSound(null, getSoundPos(), sound, getSoundSource(), volume, pitch)
    }

    //    Capability    //

    protected val fluidHandlerManager: HTFluidHandlerManager?
    protected val energyHandlerManager: HTEnergyStorageManager?
    protected val itemHandlerManager: HTItemHandlerManager?

    init {
        initializeVariables()
        fluidHandlerManager = initializeFluidHandler(::setOnlySave)?.let { HTFluidHandlerManager(it, this) }
        energyHandlerManager = initializeEnergyHandler(::setOnlySave)?.let { HTEnergyStorageManager(it, this) }
        itemHandlerManager = initializeItemHandler(::setOnlySave)?.let { HTItemHandlerManager(it, this) }
    }

    protected open fun initializeVariables() {}

    // Fluid

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialFluidTanks
     */
    protected open fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.canHandleFluid
     */
    override fun hasFluidHandler(): Boolean = fluidHandlerManager?.canHandle() ?: false

    final override fun getFluidTanks(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: listOf()

    final override fun getFluidHandler(direction: Direction?): IFluidHandler? = fluidHandlerManager?.resolve(direction)

    // Energy

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialEnergyContainers
     */
    protected open fun initializeEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.canHandleEnergy
     */
    final override fun hasEnergyStorage(): Boolean = energyHandlerManager?.canHandle() ?: false

    final override fun getEnergyBattery(side: Direction?): HTEnergyBattery? = energyHandlerManager?.getContainers(side)?.firstOrNull()

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = energyHandlerManager?.resolve(direction)

    // Item

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialInventory
     */
    protected open fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.hasInventory
     */
    final override fun hasItemHandler(): Boolean = itemHandlerManager?.canHandle() ?: false

    final override fun getItemSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: listOf()

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandlerManager?.resolve(direction)
}
