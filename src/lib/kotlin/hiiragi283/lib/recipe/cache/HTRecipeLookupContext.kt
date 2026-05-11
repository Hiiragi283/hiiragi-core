package hiiragi283.lib.recipe.cache

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import net.minecraft.core.HolderLookup
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.context.ContextKey
import net.minecraft.util.context.ContextKeySet
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.RecipeMap
import net.minecraft.world.level.Level

/**
 * @see net.minecraft.world.item.crafting.display.SlotDisplayContext
 */
object HTRecipeLookupContext {
    @JvmField
    val RECIPES: ContextKey<RecipeMap> = createKey("recipes")

    @JvmField
    val REGISTRIES: ContextKey<HolderLookup.Provider> = createKey("registries")

    @JvmField
    val BREWING: ContextKey<PotionBrewing> = createKey("brewing")

    @JvmField
    val CONTEXT: ContextKeySet = ContextKeySet.Builder().required(RECIPES).optional(REGISTRIES).optional(BREWING).build()

    @JvmStatic
    fun create(level: Level, recipeMap: RecipeMap): ContextMap = ContextMap.Builder()
        .withParameter(RECIPES, recipeMap)
        .withParameter(REGISTRIES, level.registryAccess())
        .withParameter(BREWING, level.potionBrewing())
        .create(CONTEXT)

    @JvmStatic
    fun create(level: ServerLevel): ContextMap = create(level, level.recipeAccess().recipeMap())

    @JvmStatic
    private fun <T : Any> createKey(path: String): ContextKey<T> = ContextKey(HTConstants.MOD_ID.toId(path))
}
