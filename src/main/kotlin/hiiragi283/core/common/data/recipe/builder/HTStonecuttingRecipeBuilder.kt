package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import hiiragi283.core.api.stack.ImmutableItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike

/**
 * [StonecutterRecipe]向けの[HTStackRecipeBuilder]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class HTStonecuttingRecipeBuilder(stack: ImmutableItemStack) :
    HTStackRecipeBuilder.Single<HTStonecuttingRecipeBuilder>("stonecutting", stack) {
    companion object {
        /**
         * [StonecutterRecipe]のビルダーを作成します。
         */
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTStonecuttingRecipeBuilder =
            HTStonecuttingRecipeBuilder(ImmutableItemStack.of(item, count))
    }

    private var group: String? = null

    /**
     * レシピのグループを指定します。
     */
    fun setGroup(group: String?): HTStonecuttingRecipeBuilder = apply {
        this.group = group
    }

    override fun createRecipe(output: ItemStack): StonecutterRecipe = StonecutterRecipe(
        group ?: "",
        ingredient,
        output,
    )
}
