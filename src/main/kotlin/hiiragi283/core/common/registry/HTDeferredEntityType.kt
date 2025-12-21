package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

data class HTDeferredEntityType<ENTITY : Entity>(private val key: ResourceKey<EntityType<*>>) :
    HTHolderLike<EntityType<*>, EntityType<ENTITY>>,
    HTHasTranslationKey,
    HTHasText {
    constructor(id: ResourceLocation) : this(Registries.ENTITY_TYPE.createKey(id))

    override fun getResourceKey(): ResourceKey<EntityType<*>> = key

    @Suppress("UNCHECKED_CAST")
    override fun get(): EntityType<ENTITY> {
        val rawType: EntityType<*> = BuiltInRegistries.ENTITY_TYPE.get(key) ?: error("Trying to access unbound value: $key")
        return rawType as EntityType<ENTITY>
    }

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().description
}
