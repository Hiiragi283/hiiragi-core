package hiiragi283.core.api.material.attribute

import hiiragi283.core.api.material.prefix.HTMaterialPrefix

/**
 * 素材のデフォルトのプレフィックスを保持する属性のクラス
 */
@JvmInline
value class HTDefaultPrefixMaterialAttribute(val prefix: HTMaterialPrefix) : HTMaterialAttribute
