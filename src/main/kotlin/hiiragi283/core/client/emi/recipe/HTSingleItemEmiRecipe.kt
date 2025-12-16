package hiiragi283.core.client.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import net.minecraft.resources.ResourceLocation

class HTSingleItemEmiRecipe(category: EmiRecipeCategory, private val recipe: HTSingleItemRecipe, id: ResourceLocation) :
    BasicEmiRecipe(category, id, 82, 38) {
    init {
        inputs.add(EmiIngredient.of(recipe.ingredient))

        outputs.add(EmiStack.of(recipe.result))
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets
            .addFillingArrow(24, 5, 50 * recipe.time)
            .tooltipText(listOf(translatableText("emi.cooking.time", recipe.time / 20f)))

        widgets.addTexture(EmiTexture.EMPTY_FLAME, 1, 24)

        widgets.addText(
            translatableText("emi.cooking.experience", recipe.exp.toFloat()),
            26,
            28,
            -1,
            true,
        )
        widgets.addSlot(inputs[0], 0, 4)
        widgets.addSlot(outputs[0], 56, 0).large(true).recipeContext(this)
    }
}
