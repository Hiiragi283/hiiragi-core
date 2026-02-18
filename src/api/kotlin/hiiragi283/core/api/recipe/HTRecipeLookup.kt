package hiiragi283.core.api.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import thedarkcolour.kotlinforforge.neoforge.forge.callWhenOn

/**
 * @see mekanism.common.recipe.IMekanismRecipeTypeProvider
 */
interface HTRecipeLookup<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTIdLike {
    fun createCache(): HTRecipeCache<INPUT, RECIPE>

    fun getAllRecipes(): List<RecipeHolder<RECIPE>> = callWhenOn(Dist.CLIENT) { Minecraft.getInstance().level?.let(::getAllRecipes) }
        ?: run { HiiragiCoreAPI.getActiveServer()?.let(::getAllRecipes) }
        ?: emptyList()

    fun getAllRecipes(level: Level): List<RecipeHolder<RECIPE>> =
        getAllRecipes(Context(level.recipeManager, level.registryAccess(), level.potionBrewing()))

    fun getAllRecipes(server: MinecraftServer): List<RecipeHolder<RECIPE>> =
        getAllRecipes(Context(server.recipeManager, server.registryAccess(), server.potionBrewing()))

    fun getAllRecipes(context: Context): List<RecipeHolder<RECIPE>>

    fun findFirst(level: Level?, predicate: (RECIPE) -> Boolean): RecipeHolder<RECIPE>? =
        (level?.let(this::getAllRecipes) ?: this.getAllRecipes()).firstOrNull { predicate(it.value) }

    data class Context(val manager: RecipeManager, val access: RegistryAccess, val brewing: PotionBrewing)
}
