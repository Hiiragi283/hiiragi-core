package hiiragi283.core.api.material.attribute

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike

/**
 * 製錬レシピの情報を保持する[HTMaterialAttribute]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTSmeltingMaterialAttribute private constructor(val key: HTMaterialKey?, val isBlasting: Boolean, val isSmoking: Boolean) :
    HTMaterialAttribute {
        companion object {
            @JvmStatic
            fun disable(): HTSmeltingMaterialAttribute = HTSmeltingMaterialAttribute(null, isBlasting = false, isSmoking = false)

            @JvmStatic
            fun smeltingOnly(material: HTMaterialLike): HTSmeltingMaterialAttribute =
                HTSmeltingMaterialAttribute(material.asMaterialKey(), isBlasting = false, isSmoking = false)

            @JvmStatic
            fun withBlasting(material: HTMaterialLike): HTSmeltingMaterialAttribute =
                HTSmeltingMaterialAttribute(material.asMaterialKey(), isBlasting = true, isSmoking = false)

            @JvmStatic
            fun withSmoking(material: HTMaterialLike): HTSmeltingMaterialAttribute =
                HTSmeltingMaterialAttribute(material.asMaterialKey(), isBlasting = false, isSmoking = true)
        }
    }
