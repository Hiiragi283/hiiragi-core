package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.data.holder.HTItemStackHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * 単一の[ItemStack]を完成品にとるレシピ向けの[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTStackRecipeBuilder(prefix: String) : HTRecipeBuilder(prefix) {
    final override fun getPrimalId(): ResourceLocation = resultStack.getId()

    /**
     * 完成品の[ItemStack]を保持するインスタンス
     */
    val resultStack = HTItemStackHolder()
}
