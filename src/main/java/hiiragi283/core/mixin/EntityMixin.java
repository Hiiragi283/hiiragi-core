package hiiragi283.core.mixin;

import hiiragi283.core.api.registry.TypedInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin implements TypedInstance<EntityType<?>> {
    @SuppressWarnings({"AddedMixinMembersNamePattern", "deprecation"})
    @Override
    public @NotNull Holder<EntityType<?>> typeHolder() {
        return ((Entity) (Object) this).getType().builtInRegistryHolder();
    }
}
