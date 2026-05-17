package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

fun <BLOCK : Block> BLOCK.toLike(): SupplierWithId<BLOCK> = object : SupplierWithId<BLOCK> {
    override fun get(): BLOCK = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): Identifier = get().builtInRegistryHolder().unwrapKey().orElseThrow().identifier()
}

fun <FLUID : Fluid> FLUID.toLike(): SupplierWithId<FLUID> = object : SupplierWithId<FLUID> {
    override fun get(): FLUID = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): Identifier = get().builtInRegistryHolder().unwrapKey().orElseThrow().identifier()
}

fun <ITEM : Item> ITEM.toLike(): SupplierWithId<ITEM> = object : SupplierWithId<ITEM> {
    override fun get(): ITEM = this@toLike

    @Suppress("DEPRECATION")
    override fun getId(): Identifier = get().builtInRegistryHolder().unwrapKey().orElseThrow().identifier()
}
