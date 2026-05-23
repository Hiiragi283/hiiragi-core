package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * 指定した[Block][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <BLOCK : Block> BLOCK.toLike(): SupplierWithId<BLOCK> = object : SupplierWithId<BLOCK> {
    override fun get(): BLOCK = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = get().builtInRegistryHolder().getKeyOrThrow().location()
}

/**
 * 指定した[EntityType][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <ENTITY : Entity> EntityType<ENTITY>.toLike(): SupplierWithId<EntityType<ENTITY>> = object : SupplierWithId<EntityType<ENTITY>> {
    override fun get(): EntityType<ENTITY> = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = this@toLike.builtInRegistryHolder().getKeyOrThrow().location()
}

/**
 * 指定した[Fluid][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <FLUID : Fluid> FLUID.toLike(): SupplierWithId<FLUID> = object : SupplierWithId<FLUID> {
    override fun get(): FLUID = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = get().builtInRegistryHolder().getKeyOrThrow().location()
}

/**
 * 指定した[Item][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <ITEM : Item> ITEM.toLike(): SupplierWithId<ITEM> = object : SupplierWithId<ITEM> {
    override fun get(): ITEM = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = get().builtInRegistryHolder().getKeyOrThrow().location()
}
