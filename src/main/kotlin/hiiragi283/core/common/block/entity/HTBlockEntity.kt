package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.block.entity.HTOwnedBlockEntity
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.attachments.HTAttachedEnergy
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.resolver.HTEnergyStorageManager
import hiiragi283.core.common.storage.resolver.HTFluidHandlerManager
import hiiragi283.core.common.storage.resolver.HTItemHandlerManager
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
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

    protected open fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState) {}

    open fun onBlockRemoved(state: BlockState, level: Level, pos: BlockPos) {}

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        // Capability
        for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.copyTo(this, componentInput::get)
            }
        }
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        // Capability
        for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.copyFrom(this, components)
            }
        }
    }

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

    protected val fluidHandlerManager: HTFluidHandlerManager? by lazy { createFluidHandler()?.let { HTFluidHandlerManager(it, this) } }
    protected val energyHandlerManager: HTEnergyStorageManager? by lazy { createEnergyHandler()?.let { HTEnergyStorageManager(it, this) } }
    protected val itemHandlerManager: HTItemHandlerManager? by lazy { createItemHandler()?.let { HTItemHandlerManager(it, this) } }

    // Fluid

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialFluidTanks
     */
    protected open fun createFluidHandler(): HTFluidTankHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.canHandleFluid
     */
    override fun hasFluidHandler(): Boolean = fluidHandlerManager?.canHandle() ?: false

    final override fun getFluidTanks(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: listOf()

    final override fun getFluidHandler(direction: Direction?): IFluidHandler? = fluidHandlerManager?.resolve(direction)

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.applyFluidTanks
     */
    fun applyFluidTanks(containers: List<HTFluidTank>, contents: HTAttachedFluids) {
        for (i: Int in contents.indices) {
            (containers.getOrNull(i) as? HTFluidTank.Basic)?.setStack(contents[i])
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.collectFluidTanks
     */
    fun collectFluidTanks(containers: List<HTFluidTank>): HTAttachedFluids? = containers
        .map(HTFluidTank::getFluidStack)
        .let(::HTAttachedFluids)
        .takeUnless(HTAttachedFluids::isEmpty)

    // Energy

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialEnergyContainers
     */
    protected open fun createEnergyHandler(): HTEnergyBatteryHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.canHandleEnergy
     */
    final override fun hasEnergyStorage(): Boolean = energyHandlerManager?.canHandle() ?: false

    final override fun getEnergyBattery(side: Direction?): HTEnergyBattery? = energyHandlerManager?.getContainers(side)?.firstOrNull()

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = energyHandlerManager?.resolve(direction)

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.applyEnergyContainers
     */
    fun applyEnergyBattery(containers: List<HTEnergyBattery>, contents: HTAttachedEnergy) {
        for (i: Int in contents.indices) {
            val amount: Int = contents[i]
            (containers.getOrNull(i) as? HTAmountView.Mutable)?.setAmount(amount)
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.collectEnergyContainers
     */
    fun collectEnergyBattery(containers: List<HTEnergyBattery>): HTAttachedEnergy? = containers
        .map(HTEnergyBattery::getAmount)
        .let(::HTAttachedEnergy)
        .takeUnless(HTAttachedEnergy::isEmpty)

    // Item

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialInventory
     */
    protected open fun createItemHandler(): HTItemSlotHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.hasInventory
     */
    final override fun hasItemHandler(): Boolean = itemHandlerManager?.canHandle() ?: false

    final override fun getItemSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: listOf()

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandlerManager?.resolve(direction)

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.applyInventorySlots
     */
    fun applyItemSlots(containers: List<HTItemSlot>, contents: HTAttachedItems) {
        for (i: Int in contents.indices) {
            (containers.getOrNull(i) as? HTItemSlot.Basic)?.setStack(contents[i])
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.collectInventorySlots
     */
    fun collectItemSlots(containers: List<HTItemSlot>): HTAttachedItems? = containers
        .map(HTItemSlot::getItemStack)
        .let(::HTAttachedItems)
        .takeUnless(HTAttachedItems::isEmpty)
}
