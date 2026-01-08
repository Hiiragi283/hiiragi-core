package hiiragi283.core.api.material.attribute

import hiiragi283.core.api.data.texture.HTColorPalette

/**
 * 素材のカラーパレットを保持する[HTMaterialAttribute]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@JvmInline
value class HTColorPaletteMaterialAttribute(private val value: HTColorPalette) :
    HTMaterialAttribute,
    HTColorPalette by value
