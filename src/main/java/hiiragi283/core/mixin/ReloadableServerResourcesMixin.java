package hiiragi283.core.mixin;

import hiiragi283.core.common.event.HCRecipeEventHandler;
import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {
    @Inject(method = "updateRegistryTags()V", at = @At("TAIL"))
    private void hiiragiCore$addRuntimeRecipes(CallbackInfo ci) {
        HCRecipeEventHandler.registerRuntimeRecipe((ReloadableServerResources) (Object) this);
    }
}
