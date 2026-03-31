package hiiragi283.core.impl.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.block.entity.HTOwnedBlockEntity
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.transfer.FluidResourceHandler
import hiiragi283.core.api.transfer.HTHandlerProvider
import hiiragi283.core.api.transfer.HTResourceHandler
import hiiragi283.core.api.transfer.ItemResourceHandler
import hiiragi283.core.api.transfer.energy.HTEnergyBattery
import hiiragi283.core.api.transfer.energy.HTEnergyHandler
import hiiragi283.core.api.transfer.fluid.HTFluidTank
import hiiragi283.core.api.transfer.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.transfer.holder.HTResourceSlotHolder
import hiiragi283.core.api.transfer.item.HTItemSlot
import hiiragi283.core.api.transfer.item.stack
import hiiragi283.core.impl.registry.HTDeferredBlockEntityType
import hiiragi283.core.impl.transfer.HTCapabilityCodec
import hiiragi283.core.impl.transfer.resolver.HTEnergyHandlerManager
import hiiragi283.core.impl.transfer.resolver.HTResourceHandlerManager
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.Nameable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.UUID

/**
 * キャパビリティやオーナーを保持する[HTExtendedBlockEntity]の拡張クラス
 * @see mekanism.common.tile.base.TileEntityMekanism
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(type, pos, state),
    Nameable,
    HTHandlerProvider,
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

    //    Save & Read    //

    val components: List<HTBlockEntityComponent> get() = _components
    private val _components: MutableList<HTBlockEntityComponent> = mutableListOf()

    fun addComponent(component: HTBlockEntityComponent) {
        _components += component
    }

    override fun initReducedUpdateTag(output: ValueOutput) {
        super.initReducedUpdateTag(output)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.serialize(output)
        }
    }

    override fun handleUpdateTag(input: ValueInput) {
        super.handleUpdateTag(input)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.deserialize(input)
        }
    }

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.serialize(output)
        }
        // Capability
        for (type: HTCapabilityCodec<*> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.saveTo(output, this)
            }
        }
        // Custom Name
        output.storeNullable("custom_name", ComponentSerialization.CODEC, this.customName)
        // Owner
        output.storeNullable(HTConst.OWNER, UUIDUtil.CODEC, ownerId)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        // Components
        for (component: HTBlockEntityComponent in components) {
            component.deserialize(input)
        }
        // Capability
        for (type: HTCapabilityCodec<*> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.loadFrom(input, this)
            }
        }
        // Custom Name
        input.read("custom_name", ComponentSerialization.CODEC).ifPresent(::customName::set)
        // Owner
        input.read(HTConst.OWNER, UUIDUtil.CODEC).ifPresent(::ownerId::set)
    }

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        // Components
        for (component: HTBlockEntityComponent in this.components) {
            component.applyComponents(components)
        }
        // Custom Name
        this.customName = components.get(DataComponents.CUSTOM_NAME)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        // Components
        for (component: HTBlockEntityComponent in this.components) {
            component.collectComponents(components)
        }
        // Custom Name
        components.set(DataComponents.CUSTOM_NAME, this.customName)
    }

    //    Nameable    //

    private var customName: Text? = null

    final override fun getName(): Text = customName ?: blockState.block.name

    final override fun getCustomName(): Text? = customName

    //    HTOwnedBlockEntity    //

    var ownerId: UUID? = null

    override fun getOwner(): UUID? = ownerId

    //    Transfer    //

    protected val fluidHandlerManager: HTResourceHandlerManager<FluidResource>?
    protected val energyHandlerManager: HTEnergyHandlerManager?
    protected val itemHandlerManager: HTResourceHandlerManager<ItemResource>?

    val fluidHandler: HTResourceHandler<FluidResource> = object : HTResourceHandler<FluidResource> {
        override fun getSlots(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: listOf()

        override fun hasResourceHandler(): Boolean = fluidHandlerManager?.canHandle() ?: false
    }
    val energyHandler: HTEnergyHandler = object : HTEnergyHandler {
        override fun getBattery(side: Direction?): HTEnergyBattery? = energyHandlerManager?.getContainers(side)?.firstOrNull()

        override fun hasEnergyHandler(): Boolean = energyHandlerManager?.canHandle() ?: false
    }
    val itemHandler: HTResourceHandler<ItemResource> = object : HTResourceHandler<ItemResource> {
        override fun getSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: listOf()

        override fun hasResourceHandler(): Boolean = itemHandlerManager?.canHandle() ?: false
    }

    init {
        initializeVariables()
        fluidHandlerManager = createFluidHandler(::setOnlySave)?.let { HTResourceHandlerManager(it, fluidHandler) }
        energyHandlerManager = createEnergyHandler(::setOnlySave)?.let { HTEnergyHandlerManager(it, energyHandler) }
        itemHandlerManager = createItemHandler(::setOnlySave)?.let { HTResourceHandlerManager(it, itemHandler) }
    }

    protected open fun initializeVariables() {}

    // Fluid
    protected open fun createFluidHandler(listener: HTContentListener): HTResourceSlotHolder<FluidResource>? = null

    final override fun getFluidHandler(direction: Direction?): FluidResourceHandler? = fluidHandlerManager?.resolve(direction)

    // Energy
    protected open fun createEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? = null

    final override fun getEnergyStorage(direction: Direction?): EnergyHandler? = energyHandlerManager?.resolve(direction)

    // Item
    protected open fun createItemHandler(listener: HTContentListener): HTResourceSlotHolder<ItemResource>? = null

    final override fun getItemHandler(direction: Direction?): ItemResourceHandler? = itemHandlerManager?.resolve(direction)

    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        if (shouldDropItems()) {
            val pos1: Vec3 = Vec3.atCenterOf(pos)
            val slots: List<HTItemSlot> = itemHandlerManager?.getContainers(null) ?: return
            for (slot: HTItemSlot in slots) {
                Containers.dropItemStack(level, pos1.x, pos1.y, pos1.z, slot.stack)
            }
        }
    }

    protected open fun shouldDropItems(): Boolean = true
}
