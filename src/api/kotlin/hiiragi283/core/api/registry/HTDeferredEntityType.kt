package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

class HTDeferredEntityType<out TYPE : Entity> :
    HTDeferredHolder<EntityType<*>, EntityType<@UnsafeVariance TYPE>>,
    HTIdLike.Translatable {
    constructor(key: ResourceKey<EntityType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.ENTITY_TYPE, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().description
}
