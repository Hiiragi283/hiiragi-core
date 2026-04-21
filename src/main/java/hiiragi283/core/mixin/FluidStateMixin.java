package hiiragi283.core.mixin;

import hiiragi283.core.api.registry.TypedInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FluidState.class)
public class FluidStateMixin implements TypedInstance<Fluid> {
    @SuppressWarnings({"AddedMixinMembersNamePattern", "deprecation"})
    @Override
    public @NotNull Holder<Fluid> typeHolder() {
        return ((FluidState) (Object) this).getType().builtInRegistryHolder();
    }
}
