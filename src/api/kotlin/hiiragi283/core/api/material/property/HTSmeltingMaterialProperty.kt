package hiiragi283.core.api.material.property

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

/**
 * 製錬レシピの情報を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTSmeltingMaterialProperty private constructor(
    val prefix: HTMaterialPrefix?,
    val key: HTMaterialKey?,
    val isBlasting: Boolean,
    val isSmoking: Boolean,
) {
    companion object {
        @JvmStatic
        fun disable(): HTSmeltingMaterialProperty = HTSmeltingMaterialProperty(null, null, isBlasting = false, isSmoking = false)

        @JvmStatic
        fun smeltingOnly(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = false, isSmoking = false)

        @JvmStatic
        fun withBlasting(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = true, isSmoking = false)

        @JvmStatic
        fun withSmoking(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = false, isSmoking = true)
    }
}
