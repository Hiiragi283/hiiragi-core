package hiiragi283.core.mixin;

import hiiragi283.core.api.event.HTStepOnBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "stepOn", at = @At("TAIL"))
    private void hiiragiCore$stepOn(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (entity instanceof ItemEntity itemEntity) {
            NeoForge.EVENT_BUS.post(new HTStepOnBlockEvent(level, pos, state, itemEntity));
        }
    }
}
