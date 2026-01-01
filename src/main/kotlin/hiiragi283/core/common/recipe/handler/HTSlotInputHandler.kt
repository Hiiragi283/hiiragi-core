package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.storage.resource.HTResourceView

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.InputHelper.getInputHandler
 */
class HTSlotInputHandler<RESOURCE : HTResourceType<*>>(private val slot: HTResourceSlot<RESOURCE>) :
    HTInputHandler<RESOURCE>,
    HTResourceView<RESOURCE> by slot {
    override fun getMatchingAmount(ingredient: HTIngredient<*, RESOURCE>): Int {
        val resource: RESOURCE = getResource() ?: return 0
        if (ingredient.testOnlyType(resource)) {
            return ingredient.getRequiredAmount()
        }
        return 0
    }

    override fun consume(amount: Int) {
        if (amount > 0) {
            slot.extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
