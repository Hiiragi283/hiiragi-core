package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.text.HTTextResult

/**
 * レシピの完成品を表すインターフェースです。
 * @param T 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTItemResult
 * @see HTFluidResult
 */
interface HTRecipeResult<T : Any> : SupplierWithId<HTTextResult<T>>
