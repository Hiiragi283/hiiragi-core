package hiiragi283.core.api.material.property

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTExtraOreResultMap.Phase
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.toFraction
import org.apache.commons.lang3.math.Fraction
import java.util.EnumMap

/**
 * 鉱石処理での副産物を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTExtraOreResultMap private constructor(map: Map<Phase, Pair<HTMaterialKey, Fraction>>) :
    Map<Phase, Pair<HTMaterialKey, Fraction>> by map {
        companion object {
            @HTBuilderMarker
            @JvmStatic
            inline fun create(builderAction: Builder.() -> Unit): HTExtraOreResultMap = Builder().apply(builderAction).build()
        }

        fun getResult(phase: Phase): HTChancedItemResult? {
            val (key: HTMaterialKey, chance: Fraction) = this[phase] ?: return null
            val entry: HTPropertyMap = HiiragiCoreAccess.INSTANCE.materialManager.getOrEmpty(key)
            return HTChancedItemResult(
                HTResultCreator.material(entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART), key),
                chance,
            )
        }

        fun getResult(phase: Phase, entry: HTMaterialManager.Entry): HTChancedItemResult? {
            val (key: HTMaterialKey, chance: Fraction) = this[phase] ?: return null
            return HTChancedItemResult(
                HTResultCreator.material(entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART), key),
                chance,
            )
        }

        //    Phase    //

        enum class Phase {
            CRUSH_ORE,
            CRUSH_CRUSHED,
            WASH_CRUSHED,
        }

        //    Builder    //

        /**
         * [HTExtraOreResultMap]のビルダークラスです。
         * @author Hiiragi Tsubasa
         * @since 0.10.0
         */
        class Builder {
            private val map: MutableMap<Phase, Pair<HTMaterialKey, Fraction>> = EnumMap(Phase::class.java)

            fun all(material: HTMaterialLike, chance: Float) {
                this.all(material, chance.toFraction())
            }

            fun all(material: HTMaterialLike, chance: Fraction) {
                for (phase: Phase in Phase.entries) {
                    map[phase] = material.asMaterialKey() to chance
                }
            }

            fun crushOre(material: HTMaterialLike, chance: Float) {
                this.crushOre(material, chance.toFraction())
            }

            fun crushOre(material: HTMaterialLike, chance: Fraction) {
                map[Phase.CRUSH_ORE] = material.asMaterialKey() to chance
            }

            fun crushCrushed(material: HTMaterialLike, chance: Float) {
                this.crushCrushed(material, chance.toFraction())
            }

            fun crushCrushed(material: HTMaterialLike, chance: Fraction) {
                map[Phase.CRUSH_CRUSHED] = material.asMaterialKey() to chance
            }

            fun washCrushed(material: HTMaterialLike, chance: Float) {
                this.washCrushed(material, chance.toFraction())
            }

            fun washCrushed(material: HTMaterialLike, chance: Fraction) {
                map[Phase.WASH_CRUSHED] = material.asMaterialKey() to chance
            }

            fun build(): HTExtraOreResultMap = HTExtraOreResultMap(map)
        }
    }
