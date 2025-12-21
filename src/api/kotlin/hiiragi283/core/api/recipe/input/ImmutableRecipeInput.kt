package hiiragi283.core.api.recipe.input

import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.stack.toImmutable
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [RecipeInput]を[ImmutableItemStack]の[List]として扱うためのラッパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class ImmutableRecipeInput(private val input: RecipeInput) : AbstractList<ImmutableItemStack?>() {
    override val size: Int
        get() = input.size()

    override fun get(index: Int): ImmutableItemStack? = input.getItem(index).toImmutable()
}
