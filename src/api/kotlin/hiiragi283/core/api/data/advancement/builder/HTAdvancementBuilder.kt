@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.data.ConditionBuilder
import hiiragi283.core.api.data.advancement.AdvancementKey
import hiiragi283.core.api.data.advancement.HTAdvancementExporter
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.java
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.advancements.DisplayInfo
import net.neoforged.neoforge.common.conditions.ICondition

@HTBuilderMarker
class HTAdvancementBuilder(val key: AdvancementKey) {
    companion object {
        @JvmStatic
        inline fun create(key: AdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit): HTAdvancementBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTAdvancementBuilder(key).apply(builderAction)
        }
    }

    var parent: Option<AdvancementKey> by HTDelegates.optionalOnceInitialize()

    operator fun AdvancementKey.unaryPlus() {
        parent = Option.some(this)
    }

    @PublishedApi internal var display: Option<DisplayInfo> by HTDelegates.optionalOnceInitialize()
    var rewards: AdvancementRewards = AdvancementRewards.EMPTY
    var requirements: AdvancementRequirements? = null
    var strategy: AdvancementRequirements.Strategy = AdvancementRequirements.Strategy.AND

    inline fun display(builderAction: HTDisplayInfoBuilder.() -> Unit) {
        display = Option.some(HTDisplayInfoBuilder.create(key, builderAction))
    }

    //    Conditions    //

    /**
     * [ICondition]を保持するインスタンス
     */
    @PublishedApi internal val conditions: MutableList<ICondition> = mutableListOf()

    inline fun condition(builderAction: ConditionBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ConditionBuilder(conditions).apply(builderAction)
    }

    //    Criterion    //

    @PublishedApi internal val criterions: MutableMap<String, Criterion<*>> = mutableMapOf()

    infix fun String.criterion(criterion: Criterion<*>) {
        check(this !in criterions) { "Duplicated criterion: $this" }
        criterions[this] = criterion
    }

    fun <T : CriterionTriggerInstance> define(key: String, trigger: CriterionTrigger<T>, instance: T) {
        key criterion trigger.createCriterion(instance)
    }

    inline fun inventory(key: String, builderAction: HTInventoryChangeBuilder.() -> Unit) {
        define(key, CriteriaTriggers.INVENTORY_CHANGED, HTInventoryChangeBuilder().apply(builderAction).build())
    }

    //    Save    //

    fun save(exporter: HTAdvancementExporter) {
        val adv = Advancement(
            parent.map(AdvancementKey::location).java,
            display.java,
            rewards,
            criterions,
            this.requirements ?: strategy.create(criterions.keys),
            true,
        )
        exporter.accept(key, adv, conditions)
    }
}
