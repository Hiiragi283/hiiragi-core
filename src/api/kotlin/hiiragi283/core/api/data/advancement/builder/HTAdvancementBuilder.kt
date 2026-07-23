@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.data.ConditionBuilder
import hiiragi283.core.api.data.advancement.AdvancementKey
import hiiragi283.core.api.data.advancement.HTAdvancementExporter
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.toOptional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.DisplayInfo
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[進捗][Advancement]のビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@HTBuilderMarker
class HTAdvancementBuilder(val key: AdvancementKey) {
    companion object {
        @JvmStatic
        inline fun create(exporter: HTAdvancementExporter, key: AdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            HTAdvancementBuilder(key).apply(builderAction).save(exporter)
        }
    }

    var parent: AdvancementKey? = null
    var display: DisplayInfo? = null
    var rewards: AdvancementRewards = AdvancementRewards.EMPTY
    val criteria: Criterions = Criterions()
    var requirements: AdvancementRequirements? = null
    var strategy: AdvancementRequirements.Strategy = AdvancementRequirements.Strategy.AND

    inline fun display(builderAction: HTDisplayInfoBuilder.() -> Unit) {
        display = HTDisplayInfoBuilder.create(key, builderAction)
    }

    fun save(exporter: HTAdvancementExporter) {
        val id: ResourceLocation = key.location()
        val adv = Advancement(
            parent?.location().toOptional(),
            display.toOptional(),
            rewards,
            criteria.toMap(),
            this.requirements ?: criteria.createRequirements(),
            true,
        )
        exporter.accept(id, adv, conditions)
    }

    //    Conditions    //

    /**
     * [ICondition]を保持するインスタンス
     */
    @PublishedApi
    internal val conditions: MutableList<ICondition> = mutableListOf()

    inline fun condition(builderAction: ConditionBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ConditionBuilder(conditions).apply(builderAction)
    }

    //    Criterion    //

    inner class Criterions {
        private val map: MutableMap<String, Criterion<*>> = mutableMapOf()

        @JvmName("hasItem")
        operator fun set(key: String, builder: HTInventoryChangeBuilder.() -> Unit) {
            this[key] = HTInventoryChangeBuilder()
                .apply(builder)
                .build()
                .let(CriteriaTriggers.INVENTORY_CHANGED::createCriterion)
        }

        operator fun set(key: String, criterion: Criterion<*>) {
            map[key] = criterion
        }

        fun createRequirements(): AdvancementRequirements = strategy.create(map.keys)

        fun toMap(): Map<String, Criterion<*>> = map
    }
}
