package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.storage.resource.HTResourceView
import java.util.Optional

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param RESOURCE 材料となるリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.IInputHandler
 */
interface HTInputHandler<RESOURCE : HTResourceType> :
    HTAmountInputHandler,
    HTResourceView<RESOURCE> {
    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: HTIngredient<RESOURCE>)

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: Optional<out HTIngredient<RESOURCE>>) {
        ingredient.map(::consume)
    }
}
