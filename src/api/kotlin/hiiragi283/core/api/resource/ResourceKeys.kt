package hiiragi283.core.api.resource

import hiiragi283.core.api.function.Identity
import hiiragi283.core.api.registry.createKey
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

//    ResourceKey    //

fun <T : Any> ResourceKey<T>.withPrefix(prefix: String): ResourceKey<T> =
    this.identifier().withPrefix(prefix).let(this.registryKey()::createKey)

fun <T : Any> ResourceKey<T>.withSuffix(suffix: String): ResourceKey<T> =
    this.identifier().withSuffix(suffix).let(this.registryKey()::createKey)

inline fun <T : Any> ResourceKey<T>.withPath(transform: Identity<String>): ResourceKey<T> {
    val id: Identifier = this.identifier()
    return id.path
        .let(transform)
        .let(id.namespace::toId)
        .let(this.registryKey()::createKey)
}
