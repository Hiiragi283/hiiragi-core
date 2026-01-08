package hiiragi283.core.api.material.attribute

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType

/**
 * 素材の翻訳名を保持する[HTMaterialAttribute]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@JvmInline
value class HTLangNameMaterialAttribute(private val value: HTLangName) :
    HTMaterialAttribute,
    HTLangName by value {
    constructor(enName: String, jaName: String) : this(
        { langType: HTLanguageType ->
            when (langType) {
                HTLanguageType.EN_US -> enName
                HTLanguageType.JA_JP -> jaName
            }
        },
    )
}
