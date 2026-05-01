package hiiragi283.core.api.recipe

import net.minecraft.world.item.crafting.RecipeInput

fun interface HTRecipePredicate<INPUT : RecipeInput> {
    /**
     * 指定された[input]が，このレシピの条件を満たすか判定します。
     */
    fun matches(input: INPUT): Boolean
}
