package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIorOrThrow

typealias HTComplexResult = Ior<HTItemResult, HTFluidResult>

fun Pair<HTItemResult?, HTFluidResult?>.toComplex(): HTComplexResult = this.toIorOrThrow("Either item or fluid result required")
