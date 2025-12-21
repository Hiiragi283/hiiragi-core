package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.HolderLookup
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * レシピの完成品を表すインターフェースです。
 * @param STACK 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTItemResult
 * @see HTFluidResult
 */
interface HTRecipeResult<STACK : ImmutableStack<*, STACK>> : HTIdLike {
    /**
     * 指定した[レジストリ][provider]から完成品の[結果][HTTextResult]を返します。
     */
    fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<STACK>

    /**
     * 指定した[レジストリ][provider]から完成品の[結果][HTTextResult]を返します。
     * @return 完成品がない場合は[Optional.empty]
     */
    fun getOptionalResult(provider: HolderLookup.Provider?): Optional<STACK> = getStackResult(provider).left()

    /**
     * 指定した[レジストリ][provider]から完成品を返します。
     * @return 完成品がない場合は`null`
     */
    fun getStackOrNull(provider: HolderLookup.Provider?): STACK? = getOptionalResult(provider).getOrNull()
}
