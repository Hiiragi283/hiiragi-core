package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTTextResult
import java.util.function.Supplier

/**
 * レシピの完成品を表すインターフェースです。
 * @param T 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTItemResult
 * @see HTFluidResult
 */
interface HTRecipeResult<T : Any> :
    Supplier<HTTextResult<T>>,
    HTIdLike
