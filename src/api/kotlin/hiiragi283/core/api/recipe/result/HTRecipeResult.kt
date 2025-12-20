package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.HolderLookup

/**
 * レシピの完成品を表すインターフェース
 * @param STACK 完成品のクラス
 * @see HTItemResult
 * @see HTFluidResult
 */
interface HTRecipeResult<STACK : ImmutableStack<*, STACK>> : HTIdLike {
    /**
     * 指定された[provider]から完成品の[HTTextResult]を返します。
     * @param provider レジストリへのアクセス
     * @return 完成品がある場合は[HTTextResult.success], ない場合は[HTTextResult.failure]
     */
    fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<STACK>

    /**
     * 指定された[provider]から完成品を返します。
     * @param provider レジストリへのアクセス
     * @return 完成品がない場合は`null`
     */
    fun getStackOrNull(provider: HolderLookup.Provider?): STACK? = getStackResult(provider).result()
}
