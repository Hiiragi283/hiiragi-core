package hiiragi283.core.mixin;

import hiiragi283.core.api.registry.TypedInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements TypedInstance<Item> {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public @NotNull Holder<Item> typeHolder() {
        return ((ItemStack) (Object) this).getItemHolder();
    }
}
