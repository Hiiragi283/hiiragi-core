package hiiragi283.core.mixin;

import hiiragi283.core.api.registry.TypedInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockState.class)
public class BlockStateMixin implements TypedInstance<Block> {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public @NotNull Holder<Block> typeHolder() {
        return ((BlockState) (Object) this).getBlockHolder();
    }
}
