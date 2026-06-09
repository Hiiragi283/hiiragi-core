@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.loot

import hiiragi283.lib.util.Option
import hiiragi283.lib.util.kotlin
import java.util.concurrent.CompletableFuture
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition

abstract class HTGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, modid: String) : GlobalLootModifierProvider(output, registries, modid) {
    protected fun add(key: ResourceKey<LootTable>, conditions: Collection<LootItemCondition>, priority: Int = 0) {
        add(key.identifier().path, AddTableLootModifier(conditions.toTypedArray(), priority, key))
    }

    protected inline fun add(key: ResourceKey<LootTable>, priority: Int = 0, builderAction: MutableCollection<LootItemCondition>.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        add(key.identifier().path, AddTableLootModifier(buildList(builderAction).toTypedArray(), priority, key))
    }

    protected fun builder(key: ResourceKey<LootTable>): LootItemCondition = LootTableIdCondition.Builder(key.identifier()).build()

    protected fun builder(block: Block): Option<LootItemCondition> = block.lootTable.kotlin.map(::builder)

    protected fun builder(entityType: EntityType<*>): Option<LootItemCondition> = entityType.defaultLootTable.kotlin.map(::builder)
}
