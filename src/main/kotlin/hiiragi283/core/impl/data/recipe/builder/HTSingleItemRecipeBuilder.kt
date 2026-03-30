package hiiragi283.core.impl.data.recipe.builder

import hiiragi283.core.api.data.holder.HTItemStackHolder
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import net.minecraft.resources.Identifier

abstract class HTSingleItemRecipeBuilder(prefix: String) : HTRecipeBuilder(prefix) {
    val result: HTItemStackHolder = HTItemStackHolder()

    final override fun getPrimalId(): Identifier = result.getId()
}
