package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * この[Block][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <BLOCK : Block> BLOCK.toLike(): SupplierWithId<BLOCK> = BlockWithId(this)

@JvmInline
private value class BlockWithId<out BLOCK : Block>(private val block: BLOCK) : SupplierWithId<BLOCK> {
    override fun get(): BLOCK = block

    @Suppress("DEPRECATION")
    override fun getId(): Identifier = get().builtInRegistryHolder().getKeyOrThrow().identifier()
}

/**
 * この[EntityType][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ENTITY : Entity> EntityType<ENTITY>.toLike(): SupplierWithId<EntityType<ENTITY>> = EntityTypeWithId(this)

@JvmInline
private value class EntityTypeWithId<out ENTITY : Entity>(private val type: EntityType<ENTITY>) : SupplierWithId<EntityType<@UnsafeVariance ENTITY>> {
    override fun get(): EntityType<@UnsafeVariance ENTITY> = type

    @Suppress("DEPRECATION")
    override fun getId(): Identifier = get().builtInRegistryHolder().getKeyOrThrow().identifier()
}

/**
 * この[Fluid][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <FLUID : Fluid> FLUID.toLike(): SupplierWithId<FLUID> = FluidWithId(this)

@JvmInline
private value class FluidWithId<out FLUID : Fluid>(private val fluid: FLUID) : SupplierWithId<FLUID> {
    override fun get(): FLUID = fluid

    @Suppress("DEPRECATION")
    override fun getId(): Identifier = get().builtInRegistryHolder().getKeyOrThrow().identifier()
}

/**
 * この[Item][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ITEM : Item> ITEM.toLike(): SupplierWithId<ITEM> = ItemWithId(this)

@JvmInline
private value class ItemWithId<out ITEM : Item>(private val item: ITEM) : SupplierWithId<ITEM> {
    override fun get(): ITEM = item

    @Suppress("DEPRECATION")
    override fun getId(): Identifier = get().builtInRegistryHolder().getKeyOrThrow().identifier()
}
