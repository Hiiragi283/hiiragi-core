package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTKeyLike
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

/**
 * [RecipeType]向けの[HTDeferredHolder]の拡張クラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredRecipeType<RECIPE : Recipe<*>>(key: ResourceKey<RecipeType<*>>) :
    HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>>(key),
    HTKeyLike.SimpleTranslatable<RecipeType<*>>
