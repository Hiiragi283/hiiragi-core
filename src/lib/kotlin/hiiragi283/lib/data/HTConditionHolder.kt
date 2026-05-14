package hiiragi283.lib.data

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.tag.RawTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlag
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.conditions.FeatureFlagsEnabledCondition
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.RegisteredCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition

/**
 * 複数の[ICondition]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTConditionHolder {
    private val conditions: MutableList<ICondition> = mutableListOf()

    /**
     * 指定した[modId]に対応するmodを要求する[ICondition]を追加します。
     */
    @JvmName("addModCondition")
    operator fun plusAssign(modId: String) {
        this.plusAssign(ModLoadedCondition(modId))
    }

    /**
     * 指定した[rawTagKey]を要求する[ICondition]を追加します。
     * @since 0.16.0
     */
    @JvmName("addTagCondition")
    operator fun plusAssign(rawTagKey: RawTagKey) {
        this.plusAssign(rawTagKey.create(Registries.ITEM))
    }

    /**
     * 指定した[tagKey]を要求する[ICondition]を追加します。
     */
    @JvmName("addTagCondition")
    operator fun plusAssign(tagKey: TagKey<Item>) {
        this.plusAssign(NotCondition(TagEmptyCondition(tagKey)))
    }

    @JvmName("addFeatureFlagCondition")
    operator fun plusAssign(flag: FeatureFlag) {
        this.plusAssign(FeatureFlagSet.of(flag))
    }

    @JvmName("addFeatureFlagsCondition")
    operator fun plusAssign(flags: FeatureFlagSet) {
        this.plusAssign(FeatureFlagsEnabledCondition(flags))
    }

    @JvmName("addRegisteredCondition")
    operator fun <T : Any> plusAssign(pair: Pair<RegistryKey<T>, Identifier>) {
        val (key: RegistryKey<T>, id: Identifier) = pair
        this.plusAssign(key.createKey(id))
    }

    @JvmName("addRegisteredCondition")
    operator fun <T : Any> plusAssign(key: ResourceKey<T>) {
        this.plusAssign(RegisteredCondition(key))
    }

    /**
     * 指定した[condition]を追加します。
     */
    @JvmName("addCondition")
    operator fun plusAssign(condition: ICondition) {
        conditions += condition
    }

    /**
     * [List]に変換します。
     */
    fun toList(): List<ICondition> = conditions

    /**
     * [Array]に変換します。
     */
    fun toArray(): Array<ICondition> = conditions.toTypedArray()
}
