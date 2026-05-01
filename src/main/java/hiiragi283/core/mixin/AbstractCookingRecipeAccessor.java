package hiiragi283.core.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractCookingRecipe.class)
public interface AbstractCookingRecipeAccessor {
    @Accessor
    Ingredient getIngredient();
    
    @Accessor
    ItemStack getResult();
}
