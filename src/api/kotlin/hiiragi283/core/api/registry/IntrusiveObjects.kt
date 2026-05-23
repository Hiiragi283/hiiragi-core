package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

fun <T : Any> Holder<T>.toLike(): SupplierWithId<T> = object : SupplierWithId<T> {
    override fun get(): T = this@toLike.value()

    override fun getId(): ResourceLocation = this@toLike.unwrapKey().orElseThrow { error("Unregistered holder: $this") }.location()
}

fun <BLOCK : Block> BLOCK.toLike(): SupplierWithId<BLOCK> = object : SupplierWithId<BLOCK> {
    override fun get(): BLOCK = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = get().builtInRegistryHolder().unwrapKey().orElseThrow().location()
}

fun <ENTITY : Entity> EntityType<ENTITY>.toLike(): SupplierWithId<EntityType<ENTITY>> = object : SupplierWithId<EntityType<ENTITY>> {
    override fun get(): EntityType<ENTITY> = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = this@toLike.builtInRegistryHolder().unwrapKey().orElseThrow().location()
}

fun <FLUID : Fluid> FLUID.toLike(): SupplierWithId<FLUID> = object : SupplierWithId<FLUID> {
    override fun get(): FLUID = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = get().builtInRegistryHolder().unwrapKey().orElseThrow().location()
}

fun <ITEM : Item> ITEM.toLike(): SupplierWithId<ITEM> = object : SupplierWithId<ITEM> {
    override fun get(): ITEM = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): ResourceLocation = get().builtInRegistryHolder().unwrapKey().orElseThrow().location()
}
