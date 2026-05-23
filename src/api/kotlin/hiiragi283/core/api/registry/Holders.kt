package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.unwrapKey().orElseThrow { error("Unregistered holder: $this") }

fun <T : Any> Holder<T>.toLike(): SupplierWithId<T> = object : SupplierWithId<T> {
    override fun get(): T = this@toLike.value()

    override fun getId(): ResourceLocation = this@toLike.getKeyOrThrow().location()
}
