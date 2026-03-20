package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.base.HTFluidRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [RecipeInput]と[HTRecipe]を束ねたクラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTHandledRecipe<INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> private constructor(val input: INPUT, val recipe: RECIPE) {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> create(input: INPUT, recipe: RECIPE): HTHandledRecipe<INPUT, RECIPE>? = when {
            recipe.test(input) -> HTHandledRecipe(input, recipe)
            else -> null
        }
    }

    /**
     * レシピの完成品を取得します。
     */
    fun assemble(registries: HolderLookup.Provider): ItemStack = map(registries, HTRecipe<INPUT>::assemble)

    /**
     * 保持している[input]と[recipe]を変換します。
     * @param T 変換後のクラス
     * @param transform 変換するブロック
     */
    inline fun <T> map(transform: (RECIPE, INPUT) -> T): T = transform(recipe, input)

    inline fun <T, C> map(context: C, transform: (RECIPE, INPUT, C) -> T): T = transform(recipe, input, context)
}

//    Extensions    //

/**
 * [HTFluidRecipe.assembleFluid]に基づいて完成品を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <INPUT : RecipeInput, RECIPE> HTHandledRecipe<INPUT, RECIPE>.assembleFluid(
    registries: HolderLookup.Provider,
): FluidStack where RECIPE : HTRecipe<INPUT>, RECIPE : HTFluidRecipe<INPUT> = this.map(registries, HTFluidRecipe<INPUT>::assembleFluid)
