package hiiragi283.core.api.capability

import com.google.common.util.concurrent.Runnables
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider
import net.neoforged.neoforge.capabilities.ICapabilityProvider
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import java.util.function.BooleanSupplier

/**
 * 複数のキャパビリティを束ねるインターフェース
 * @param HANDLER キャパビリティのインターフェース
 * @param ITEM_HANDLER アイテムにおけるキャパビリティのインターフェース
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.capabilities.IMultiTypeCapability
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
interface HTMultiCapability<HANDLER : Any, ITEM_HANDLER : HANDLER> {
    val block: BlockCapability<HANDLER, Direction?>
    val entity: EntityCapability<HANDLER, Direction?>
    val item: ItemCapability<ITEM_HANDLER, Void?>

    //    Block    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(level: Level, pos: BlockPos, side: Direction?): HANDLER? = level.getCapability(block, pos, side)

    fun createCache(level: ServerLevel, pos: BlockPos, side: Direction?): BlockCapabilityCache<HANDLER, Direction?> = BlockCapabilityCache.create(block, level, pos, side)

    fun createCache(level: ServerLevel, pos: BlockPos, side: Direction?, validator: BooleanSupplier = BooleanSupplier { true }, listener: Runnable = Runnables.doNothing()): BlockCapabilityCache<HANDLER, Direction?> = BlockCapabilityCache.create(block, level, pos, side, validator, listener)

    //    Entity    //

    fun getCapability(entity: Entity, side: Direction?): HANDLER? = entity.getCapability(this@HTMultiCapability.entity, side)

    //    Item    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(stack: ItemStack): ITEM_HANDLER? = stack.getCapability(item)

    fun hasCapability(stack: ItemStack): Boolean = getCapability(stack) != null

    // HTItemResourceType
    fun getCapability(resource: HTItemResourceType?): ITEM_HANDLER? = resource?.toStack()?.getCapability(item)

    fun hasCapability(resource: HTItemResourceType?): Boolean = resource?.toStack()?.let(::hasCapability) != null

    //    Register    //

    /**
     * @since 0.7.0
     */
    fun registerBlock(event: RegisterCapabilitiesEvent, provider: IBlockCapabilityProvider<HANDLER, Direction?>, vararg blocks: Block) {
        event.registerBlock(
            block,
            provider,
            *blocks,
        )
    }

    /**
     * @since 0.7.0
     */
    fun <BE : BlockEntity> registerBlockEntity(
        event: RegisterCapabilitiesEvent,
        type: BlockEntityType<BE>,
        provider: ICapabilityProvider<BE, Direction?, HANDLER>,
    ) {
        event.registerBlockEntity(block, type, provider)
    }

    /**
     * @since 0.7.0
     */
    fun <ENTITY : Entity> registerEntity(
        event: RegisterCapabilitiesEvent,
        type: EntityType<ENTITY>,
        provider: ICapabilityProvider<ENTITY, Direction?, HANDLER>,
    ) {
        event.registerEntity(entity, type, provider)
    }

    /**
     * @since 0.7.0
     */
    fun registerItem(event: RegisterCapabilitiesEvent, factory: (ItemStack) -> ITEM_HANDLER?, vararg items: ItemLike) {
        event.registerItem(
            item,
            { stack: ItemStack, _: Void? -> factory(stack) },
            *items,
        )
    }

    //    Simple    //

    interface Simple<HANDLER : Any> : HTMultiCapability<HANDLER, HANDLER>
}
