package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeType<RECIPE : Recipe<*>> :
    HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>>,
    HTIdLike.Translatable {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: Identifier) : super(Registries.RECIPE_TYPE, id)

    override val translationKey: String = id.toLanguageKey("recipe_type")

    override fun getText(): Text = translatableText(translationKey)
}
