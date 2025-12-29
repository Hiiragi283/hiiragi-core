package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.HolderLookup

/**
 * レシピの完成品を表すインターフェースです。
 * @param STACK 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTItemResult
 * @see HTFluidResult
 */
interface HTRecipeResult<STACK : Any> : HTIdLike {
    /**
     * 指定した[レジストリ][provider]から完成品の[結果][HTTextResult]を返します。
     */
    fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<STACK>
}
