package hiiragi283.core.api.material.attribute

import hiiragi283.core.api.material.prefix.HTMaterialPrefix

/**
 * 素材のデフォルトのプレフィックスを保持する[HTMaterialAttribute]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@JvmInline
value class HTDefaultPrefixMaterialAttribute(val prefix: HTMaterialPrefix) : HTMaterialAttribute
