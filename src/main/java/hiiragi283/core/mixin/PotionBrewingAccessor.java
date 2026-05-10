package hiiragi283.core.mixin;

import java.util.List;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {
    @Accessor()
    List<PotionBrewingMixAccessor<Potion>> getPotionMixes();
}
