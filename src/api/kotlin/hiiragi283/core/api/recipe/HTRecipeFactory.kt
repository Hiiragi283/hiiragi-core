package hiiragi283.core.api.recipe

import net.minecraft.world.item.crafting.RecipeInput

fun interface HTRecipeFactory<INPUT : RecipeInput, OUTPUT : Any> {
    /**
     * 指定された[input]から完成品を作成します。
     */
    fun assemble(input: INPUT): OUTPUT
}
