package hiiragi283.core.mixin;

import hiiragi283.core.api.event.HTAnvilLandEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {
    @Inject(method = "onLand", at = @At("TAIL"), require = 1)
    private void hiiragiCore$onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new HTAnvilLandEvent(level, pos, replaceableState, state, fallingBlock));
    }
}
