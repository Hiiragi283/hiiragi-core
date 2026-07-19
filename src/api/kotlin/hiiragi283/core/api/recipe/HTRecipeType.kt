package hiiragi283.core.api.recipe

import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

/**
 * Hiiragi Seriesで使用される[RecipeType]の実装クラスです。
 * @param T レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@JvmRecord
data class HTRecipeType<T : Recipe<*>>(private val id: ResourceLocation) :
    RecipeType<T>,
    HTKeyLike.SimpleTranslatable<RecipeType<*>> {
    override fun getKey(): ResourceKey<RecipeType<*>> = Registries.RECIPE_TYPE.createKey(id)
}
