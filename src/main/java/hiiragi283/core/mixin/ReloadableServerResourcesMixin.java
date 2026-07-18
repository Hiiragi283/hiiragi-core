package hiiragi283.core.mixin;

import hiiragi283.core.api.HiiragiCoreAccess;
import hiiragi283.core.api.plugin.HTMaterialPlugin;
import hiiragi283.core.common.data.HCDynamicServerResources;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {
    @Inject(method = "loadResources", at = @At("HEAD"))
    private static void hiiragiCore$loadResources(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> registries, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, int functionCompilationLevel, Executor backgroundExecutor, Executor gameExecutor, CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {
        HCDynamicServerResources.initialize();
        HiiragiCoreAccess.INSTANCE.forEachPlugin("Registering Server Resources", HTMaterialPlugin::registerServerResources);
    }

    /*@Inject(method = "updateRegistryTags()V", at = @At("TAIL"))
    private void hiiragiCore$addRuntimeRecipes(CallbackInfo ci) {
        HCRecipeEventHandler.registerRuntimeRecipe((ReloadableServerResources) (Object) this);
    }*/
}
