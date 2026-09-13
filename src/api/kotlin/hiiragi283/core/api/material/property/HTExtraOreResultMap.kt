@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.material.property

import hiiragi283.core.api.collection.mutableEnumMapOf
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTExtraOreResultMap.Phase
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.toFraction
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.apache.commons.lang3.math.Fraction

/**
 * 鉱石処理での副産物を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTExtraOreResultMap private constructor(map: Map<Phase, Pair<HTMaterialKey, Fraction>>) : Map<Phase, Pair<HTMaterialKey, Fraction>> by map {
    companion object {
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTExtraOreResultMap {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return Builder().apply(builderAction).build()
        }
    }

    fun getResult(phase: Phase): HTChancedItemResult? {
        val (key: HTMaterialKey, chance: Fraction) = this[phase] ?: return null
        return HTMaterial.getManager()[key]
            .getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
            .let { HTItemResult.MaterialPartEntry(it, key) }
            .toResult()
            .withChance(chance)
    }

    fun getResult(phase: Phase, material: HTMaterial?): HTChancedItemResult? {
        val (key: HTMaterialKey, chance: Fraction) = this[phase] ?: return null
        return HTItemResult.MaterialPartEntry(material.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART), key).toResult() withChance chance
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
        private val map: MutableMap<Phase, Pair<HTMaterialKey, Fraction>> = mutableEnumMapOf()

        fun all(key: HTMaterialKey, chance: Float) {
            this.all(key, chance.toFraction())
        }

        fun all(key: HTMaterialKey, chance: Fraction) {
            for (phase: Phase in Phase.entries) {
                map[phase] = key to chance
            }
        }

        fun crushOre(key: HTMaterialKey, chance: Float) {
            this.crushOre(key, chance.toFraction())
        }

        fun crushOre(key: HTMaterialKey, chance: Fraction) {
            map[Phase.CRUSH_ORE] = key to chance
        }

        fun crushCrushed(key: HTMaterialKey, chance: Float) {
            this.crushCrushed(key, chance.toFraction())
        }

        fun crushCrushed(key: HTMaterialKey, chance: Fraction) {
            map[Phase.CRUSH_CRUSHED] = key to chance
        }

        fun washCrushed(key: HTMaterialKey, chance: Float) {
            this.washCrushed(key, chance.toFraction())
        }

        fun washCrushed(key: HTMaterialKey, chance: Fraction) {
            map[Phase.WASH_CRUSHED] = key to chance
        }

        fun build(): HTExtraOreResultMap = HTExtraOreResultMap(map)
    }
}
