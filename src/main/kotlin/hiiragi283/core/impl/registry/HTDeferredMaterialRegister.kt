package hiiragi283.core.impl.registry

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.function.Identity
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.registry.HTDeferredRegister
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

class HTDeferredMaterialRegister(namespace: String) : HTDeferredRegister<HTMaterial>(HCRegistries.Keys.MATERIAL, namespace) {
    private val materialEntries: MutableCollection<HTDeferredMaterial<*>> = mutableSetOf()

    fun <M : HTMaterial> registerMaterial(
        name: String,
        factory: (HTMaterial.Properties) -> M,
        operator: Identity<HTMaterial.Properties> = identity(),
    ): HTDeferredMaterial<M> {
        val key: ResourceKey<HTMaterial> = createKey(name)
        delegate
            .register(name) { _: Identifier ->
                HTMaterial
                    .Properties()
                    .setId(key)
                    .let(operator)
                    .let(factory)
            }
        val holder = HTDeferredMaterial<M>(key)
        materialEntries += holder
        return holder
    }

    fun registerSimpleMaterial(name: String, operator: Identity<HTMaterial.Properties> = identity()): HTDeferredMaterial<HTMaterial> =
        registerMaterial(name, ::HTMaterial, operator)

    fun asMaterialSequence(): Sequence<HTDeferredMaterial<*>> = materialEntries.asSequence()
}
