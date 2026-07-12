@file:Suppress("DEPRECATION")

package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithKey
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * この[Block][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <BLOCK : Block> BLOCK.toLike(): SupplierWithKey<Block, BLOCK> = BlockWithKey(this)

@JvmInline
private value class BlockWithKey<out BLOCK : Block>(private val block: BLOCK) : SupplierWithKey<Block, BLOCK> {
    override fun get(): BLOCK = block

    override fun getKey(): ResourceKey<Block> = get().builtInRegistryHolder().getKeyOrThrow()
}

/**
 * この[EntityType][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ENTITY : Entity> EntityType<ENTITY>.toLike(): SupplierWithKey<EntityType<*>, EntityType<ENTITY>> = EntityTypeWithKey(this)

@JvmInline
private value class EntityTypeWithKey<out ENTITY : Entity>(private val type: EntityType<ENTITY>) : SupplierWithKey<EntityType<*>, EntityType<@UnsafeVariance ENTITY>> {
    override fun get(): EntityType<@UnsafeVariance ENTITY> = type

    override fun getKey(): ResourceKey<EntityType<*>> = get().builtInRegistryHolder().getKeyOrThrow()
}

/**
 * この[Fluid][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <FLUID : Fluid> FLUID.toLike(): SupplierWithKey<Fluid, FLUID> = FluidWithKey(this)

@JvmInline
private value class FluidWithKey<out FLUID : Fluid>(private val fluid: FLUID) : SupplierWithKey<Fluid, FLUID> {
    override fun get(): FLUID = fluid

    override fun getKey(): ResourceKey<Fluid> = get().builtInRegistryHolder().getKeyOrThrow()
}

/**
 * この[Item][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ITEM : Item> ITEM.toLike(): SupplierWithKey<Item, ITEM> = ItemWithKey(this)

@JvmInline
private value class ItemWithKey<out ITEM : Item>(private val item: ITEM) : SupplierWithKey<Item, ITEM> {
    override fun get(): ITEM = item

    override fun getKey(): ResourceKey<Item> = get().builtInRegistryHolder().getKeyOrThrow()
}
