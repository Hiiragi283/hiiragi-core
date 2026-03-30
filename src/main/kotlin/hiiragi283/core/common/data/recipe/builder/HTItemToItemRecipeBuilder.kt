package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.holder.HTItemStackHolder
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.impl.recipe.HTBasicItemToItemRecipe
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTItemToItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    lateinit var ingredient: SizedIngredient
    val result: HTItemStackHolder = HTItemStackHolder()

    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): HTBasicItemToItemRecipe = factory.create(ingredient, result.template, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemToItemRecipe> {
        fun create(ingredient: SizedIngredient, result: ItemStackTemplate, time: Int): RECIPE
    }
}
