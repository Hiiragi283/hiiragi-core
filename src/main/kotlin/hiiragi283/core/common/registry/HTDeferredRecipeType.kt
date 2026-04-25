package hiiragi283.core.common.registry

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeType<RECIPE : Recipe<*>> :
    HTBasicHolderLike<RecipeType<*>, RecipeType<RECIPE>>,
    HTIdLike.Translatable {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    @Suppress("UNCHECKED_CAST")
    override fun get(): RecipeType<RECIPE> = BuiltInRegistries.RECIPE_TYPE.getOrThrow(key) as RecipeType<RECIPE>

    override val translationKey: String = getId().toLanguageKey("recipe_type")

    override fun getText(): Text = translatableText(translationKey)
}
