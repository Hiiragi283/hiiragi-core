package hiiragi283.core.client.emi.category

import dev.emi.emi.EmiUtil
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.world.level.ItemLike

class HCEmiRecipeCategory(path: String, val iconStack: EmiStack) :
    EmiRecipeCategory(
        HiiragiCoreAPI.id(path),
        iconStack,
        iconStack,
        EmiRecipeSorting.compareOutputThenInput(),
    ),
    HTHasTranslationKey {
    constructor(path: String, icon: ItemLike) : this(path, EmiStack.of(icon))

    override val translationKey: String get() = EmiUtil.translateId("emi.category.", getId())
}
