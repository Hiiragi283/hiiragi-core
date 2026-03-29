package hiiragi283.core.common.registry

import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

class HTDeferredEntityType<ENTITY : Entity> :
    HTBasicHolderLike<EntityType<*>, EntityType<ENTITY>>,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<EntityType<*>>) : super(key)

    constructor(id: Identifier) : super(Registries.ENTITY_TYPE, id)

    @Suppress("UNCHECKED_CAST")
    override fun get(): EntityType<ENTITY> = BuiltInRegistries.ENTITY_TYPE.getValueOrThrow(key) as EntityType<ENTITY>

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().description

    override fun toString(): String = "HTDeferredEntityType(key=$key)"
}
