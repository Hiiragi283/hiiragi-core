package hiiragi283.lib.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

/**
 * [RecipeType]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredRecipeTypeRegister(namespace: String) : HTDeferredRegister<RecipeType<*>>(Registries.RECIPE_TYPE, namespace) {
    /**
     * 新しい[RecipeType]を登録します。
     * @param RECIPE レシピのクラス
     * @param name [RecipeType]のIDのパス
     * @return 新しい[HTDeferredRecipeType]のインスタンス
     */
    fun <RECIPE : Recipe<*>> registerType(name: String): HTDeferredRecipeType<RECIPE> {
        val holder = HTDeferredRecipeType<RECIPE>(createKey(name))
        this.register(name) { id: Identifier -> RecipeType.simple<RECIPE>(id) }
        return holder
    }
}
