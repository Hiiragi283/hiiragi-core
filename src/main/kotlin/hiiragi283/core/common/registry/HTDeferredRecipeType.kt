package hiiragi283.core.common.registry

import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.translatableText
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

data class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val key: ResourceKey<RecipeType<*>>) :
    HTHolderLike<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeFinder.Vanilla<INPUT, RECIPE>,
    HTHasTranslationKey,
    HTHasText {
    constructor(id: ResourceLocation) : this(Registries.RECIPE_TYPE.createKey(id))

    override fun getResourceKey(): ResourceKey<RecipeType<*>> = key

    @Suppress("UNCHECKED_CAST")
    override fun get(): RecipeType<RECIPE> = BuiltInRegistries.RECIPE_TYPE.getOrThrow(key) as RecipeType<RECIPE>

    override fun getVanillaRecipeFor(input: INPUT, level: Level, lastRecipe: RecipeHolder<RECIPE>?): RecipeHolder<RECIPE>? =
        level.recipeManager
            .getRecipeFor(get(), input, level, lastRecipe)
            .getOrNull()

    override val translationKey: String = getId().toLanguageKey("recipe_type")

    override fun getText(): Component = translatableText(translationKey)
}
