package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.data.advancement.HTAdvancementOutput
import hiiragi283.core.api.data.holder.HTConditionHolder
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.DisplayInfo
import net.minecraft.resources.ResourceLocation
import java.util.Optional

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[進捗][Advancement]のビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTAdvancementBuilder(private val key: HTAdvancementKey) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        fun create(key: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
            HTAdvancementBuilder(key).apply(builderAction)
        }
    }

    var parent: HTAdvancementKey? = null
    var display: DisplayInfo? = null
    var rewards: AdvancementRewards = AdvancementRewards.EMPTY
    val criteria: Criterions = Criterions()
    var requirements: AdvancementRequirements? = null
    var strategy: AdvancementRequirements.Strategy = AdvancementRequirements.Strategy.AND
    val conditions = HTConditionHolder()

    fun save(output: HTAdvancementOutput) {
        val id: ResourceLocation = key.getId()
        val adv = Advancement(
            Optional.ofNullable(parent?.getId()),
            Optional.ofNullable(display), // TODO
            rewards,
            criteria.toMap(),
            this.requirements ?: criteria.createRequirements(),
            true,
        )
        output.accept(id, adv, conditions.toList())
    }

    //    Criterion    //

    inner class Criterions {
        private val map: MutableMap<String, Criterion<*>> = mutableMapOf()

        @HTBuilderMarker
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
