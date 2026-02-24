package hiiragi283.core.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import java.util.function.Predicate

/**
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see hiiragi283.core.api.recipe.base.HTProcessingRecipe
 */
interface HTRecipe<INPUT : RecipeInput> : Predicate<INPUT> {
    abstract override fun test(input: INPUT): Boolean

    fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack
}
