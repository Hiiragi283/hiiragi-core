package hiiragi283.core.impl.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.block.entity.HTOwnedBlockEntity
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.transfer.HTHandlerProvider
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
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
        /*for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.saveTo(output, this)
            }
        }*/
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
        /*for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.loadFrom(input, this)
            }
        }*/
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
        // Capability
        /*for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.copyTo(this, components::get)
            }
        }*/
        // Custom Name
        this.customName = components.get(DataComponents.CUSTOM_NAME)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        // Components
        for (component: HTBlockEntityComponent in this.components) {
            component.collectComponents(components)
        }
        // Capability
        /*for (type: HTCapabilityCodec<*, *> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.copyFrom(this, builder)
            }
        }*/
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
}
