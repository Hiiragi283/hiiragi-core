package hiiragi283.core.common.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.block.entity.HTOwnedBlockEntity
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.collection.isEmpty
import hiiragi283.core.api.serialization.component.DataComponentGetter
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.api.storage.attachments.HTAttachedEnergy
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.HTMutableFluidView
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.setStack
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.HTMutableItemView
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.setStack
import hiiragi283.core.api.text.Text
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.energy.HTBasicEnergyBattery
import hiiragi283.core.impl.storage.resolver.HTEnergyStorageManager
import hiiragi283.core.impl.storage.resolver.HTFluidHandlerManager
import hiiragi283.core.impl.storage.resolver.HTItemHandlerManager
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
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
            val shouldUpdate: Boolean = blockEntity.onUpdateServer(serverLevel, pos, state)
            blockEntity.ticks++
            if (shouldUpdate) {
                blockEntity.sendUpdatePacket(serverLevel)
            }
        }
    }

    var ticks: Int = 0
        protected set

    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    open fun onBlockRemoved(state: BlockState, level: Level, pos: BlockPos) {}

    //    Save & Read    //

    val components: List<HTBlockEntityComponent> get() = _components
    private val _components: MutableList<HTBlockEntityComponent> = mutableListOf()

    fun addComponent(component: HTBlockEntityComponent) {
        _components += component
    }

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.serialize(output)
        }
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.deserialize(input)
        }
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.serialize(output)
        }
        // Capability
        for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.saveTo(output, this)
            }
        }
        // Custom Name
        output.write("custom_name", ComponentSerialization.CODEC, this.customName)
        // Owner
        output.write(HTConst.OWNER, UUIDUtil.CODEC, ownerId)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.deserialize(input)
        }
        // Capability
        for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.loadFrom(input, this)
            }
        }
        // Custom Name
        input.read("custom_name", ComponentSerialization.CODEC).let(::customName::set)
        // Owner
        input.read(HTConst.OWNER, UUIDUtil.CODEC).let(::ownerId::set)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.applyComponents(object : DataComponentGetter {
                override fun <T : Any> get(type: DataComponentType<T>): T? = componentInput.get(type)
            })
        }
        // Capability
        for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.copyTo(this, componentInput::get)
            }
        }
        // Custom Name
        this.customName = componentInput.get(DataComponents.CUSTOM_NAME)
    }

    override fun collectImplicitComponents(builder: DataComponentMap.Builder) {
        super.collectImplicitComponents(builder)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.collectComponents(builder)
        }
        // Capability
        for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.copyFrom(this, builder)
            }
        }
        // Custom Name
        builder.set(DataComponents.CUSTOM_NAME, this.customName)
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.addContainerTrackers
     */
    /*open fun addMenuTrackers(holder: HTWidgetHolder) {
        // Fluid Tanks
        if (hasFluidHandler()) {
            for (tank: HTFluidTank in this.getFluidTanks(this.getFluidSideFor())) {
                (tank as? HTMutableFluidView)?.let {
                    holder.track(HTFluidSyncSlot(it), HTSyncType.S2C)
                }
            }
        }
        // Energy Battery
        if (hasEnergyStorage()) {
            val battery: HTEnergyBattery? = this.getEnergyBattery(this.getEnergySideFor())
            if (battery is HTAmountView.Mutable) {
                holder.track(HTIntSyncSlot.create(battery), HTSyncType.S2C)
            }
        }
    }*/

    //    Nameable    //

    private var customName: Text? = null

    final override fun getName(): Text = customName ?: blockState.block.name

    final override fun getCustomName(): Text? = customName

    //    HTMenuCallback    //

    fun openMenu(player: Player) {
        this.getServerLevel()?.let(::sendUpdatePacket)
    }

    //    HTOwnedBlockEntity    //

    var ownerId: UUID? = null

    override fun getOwner(): UUID? = ownerId

    //    Capability    //

    protected val fluidHandlerManager: HTFluidHandlerManager?
    protected val energyHandlerManager: HTEnergyStorageManager?
    protected val itemHandlerManager: HTItemHandlerManager?

    init {
        initializeVariables()
        fluidHandlerManager = createFluidHandler(::setOnlySave)?.let { HTFluidHandlerManager(it, this) }
        energyHandlerManager = createEnergyHandler(::setOnlySave)?.let { HTEnergyStorageManager(it, this) }
        itemHandlerManager = createItemHandler(::setOnlySave)?.let { HTItemHandlerManager(it, this) }
    }

    protected open fun initializeVariables() {}

    // Fluid

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialFluidTanks
     */
    protected open fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.canHandleFluid
     */
    override fun hasFluidHandler(): Boolean = fluidHandlerManager?.canHandle() ?: false

    final override fun getFluidTanks(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: emptyList()

    final override fun getFluidHandler(direction: Direction?): IFluidHandler? = fluidHandlerManager?.resolve(direction)

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.applyFluidTanks
     */
    @Suppress("UNCHECKED_CAST")
    fun applyFluidTanks(containers: List<HTFluidTank>, contents: HTAttachedFluids) {
        for (i: Int in contents.indices) {
            (containers.getOrNull(i) as? HTMutableFluidView)?.setStack(contents[i])
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.collectFluidTanks
     */
    fun collectFluidTanks(containers: List<HTFluidTank>): HTAttachedFluids? = containers
        .map(HTFluidTank::getFluidStack)
        .let(::HTAttachedFluids)
        .takeUnless { it.isEmpty(FluidStack::isEmpty) }

    // Energy

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialEnergyContainers
     */
    protected open fun createEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? = null

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
            (containers.getOrNull(i) as? HTBasicEnergyBattery)?.setAmountUnchecked(amount, true)
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.collectEnergyContainers
     */
    fun collectEnergyBattery(containers: List<HTEnergyBattery>): HTAttachedEnergy? = containers
        .map(HTEnergyBattery::getAmount)
        .let(::HTAttachedEnergy)
        .takeUnless { it.isEmpty() || it.all { amount: Int -> amount <= 0 } }

    // Item

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialInventory
     */
    protected open fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.hasInventory
     */
    final override fun hasItemHandler(): Boolean = itemHandlerManager?.canHandle() ?: false

    final override fun getItemSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: emptyList()

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandlerManager?.resolve(direction)

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.applyInventorySlots
     */
    @Suppress("UNCHECKED_CAST")
    fun applyItemSlots(containers: List<HTItemSlot>, contents: HTAttachedItems) {
        for (i: Int in contents.indices) {
            (containers.getOrNull(i) as? HTMutableItemView)?.setStack(contents[i])
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.collectInventorySlots
     */
    fun collectItemSlots(containers: List<HTItemSlot>): HTAttachedItems? = containers
        .map(HTItemSlot::getItemStack)
        .let(::HTAttachedItems)
        .takeUnless { it.isEmpty(ItemStack::isEmpty) }
}
