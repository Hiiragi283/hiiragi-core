package hiiragi283.core.api.material.attribute

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

/**
 * 製錬レシピの情報を保持する[HTMaterialAttribute]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTSmeltingMaterialAttribute private constructor(
    val prefix: HTMaterialPrefix?,
    val key: HTMaterialKey?,
    val isBlasting: Boolean,
    val isSmoking: Boolean,
) : HTMaterialAttribute {
    companion object {
        @JvmStatic
        fun disable(): HTSmeltingMaterialAttribute = HTSmeltingMaterialAttribute(null, null, isBlasting = false, isSmoking = false)

        @JvmStatic
        fun smeltingOnly(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialAttribute =
            HTSmeltingMaterialAttribute(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = false, isSmoking = false)

        @JvmStatic
        fun withBlasting(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialAttribute =
            HTSmeltingMaterialAttribute(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = true, isSmoking = false)

        @JvmStatic
        fun withSmoking(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialAttribute =
            HTSmeltingMaterialAttribute(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = false, isSmoking = true)
    }
}
