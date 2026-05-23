package hiiragi283.core.api.registry

import java.util.function.Consumer
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

class HTDeferredEntityTypeRegister(namespace: String) : HTDeferredRegister<EntityType<*>>(Registries.ENTITY_TYPE, namespace) {
    fun <ENTITY : Entity> registerType(
        name: String,
        factory: EntityType.EntityFactory<ENTITY>,
        category: MobCategory,
        builderAction: Consumer<EntityType.Builder<ENTITY>>,
    ): HTDeferredEntityType<ENTITY> {
        val holder = HTDeferredEntityType<ENTITY>(createId(name))
        this.register(name) { id: ResourceLocation ->
            EntityType.Builder
                .of(factory, category)
                .apply(builderAction::accept)
                .build(id.path)
        }
        return holder
    }
}
