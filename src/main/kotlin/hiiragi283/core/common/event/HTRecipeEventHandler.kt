package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTAnvilLandEvent
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.times
import hiiragi283.core.api.toFraction
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
import net.neoforged.neoforge.event.level.ExplosionEvent
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HTRecipeEventHandler {
    /**
     * [HCLightningChargingRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onStruck(event: EntityStruckByLightningEvent) {
        val entity: Entity = event.entity
        if (isCompleted(entity)) {
            event.isCanceled = true
            return
        }
        val level: Level = entity.level()
        if (level.isClientSide) return
        if (entity is ItemEntity && entity.isAlive) {
            val input: SingleRecipeInput = createInput(entity)
            val recipe: HCLightningChargingRecipe = level.recipeManager
                .getRecipesFor(
                    HCRecipeTypes.CHARGING.get(),
                    input,
                    level,
                ).map(RecipeHolder<HCLightningChargingRecipe>::value)
                .firstOrNull() ?: return
            popResult(recipe.assemble(input, level.registryAccess()), recipe.ingredient.amount, entity)
            if (entity.item.isEmpty) {
                entity.discard()
                event.isCanceled = true
            }
        }
    }

    /**
     * [HCAnvilCrushingRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onAnvilLand(event: HTAnvilLandEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        for (entity: ItemEntity in level.getEntitiesOfClass(ItemEntity::class.java, AABB(pos))) {
            if (isCompleted(entity)) continue
            val input: SingleRecipeInput = createInput(entity)
            val (_, recipe: HCAnvilCrushingRecipe) = HCRecipeTypes.ANVIL_CRUSHING.getRecipeFor(input, level, null) ?: continue
            val multiplier: Int = popResult(input, recipe, level, entity)
            recipe.extraResult?.let { (result: HTItemResult, chance: Fraction, fallback: Optional<HTItemResult>) ->
                val access: RegistryAccess = level.registryAccess()
                val amount: Int = (chance * multiplier).toInt()
                entity.spawnAtLocation(result.copyWithCount { it * amount }.getStackOrEmpty(access))
                fallback.ifPresent { result1: HTItemResult ->
                    entity.spawnAtLocation(result1.copyWithCount { it * (multiplier - amount) }.getStackOrEmpty(access))
                }
            }
            if (entity.item.isEmpty) {
                entity.discard()
            }
        }
    }

    /*fun onStepOnBlock(event: HTStepOnBlockEvent) {
        val level: Level = event.level
        if (level.isClientSide) return
        if (!event.state.`is`(Blocks.MAGMA_BLOCK)) return
        val entity: ItemEntity = event.entity
        if (entity.isAlive && !isCompleted(entity)) {
            val input = createInput(entity) ?: return
            val (_, recipe: HTDryingRecipe) = HCRecipeTypes.DRYING.getRecipeFor(input, level, null) ?: return
            val tag: CompoundTag = entity.persistentData
            val dryingTicks: Int = tag.getInt(HTConst.DRYING_TICKS)
            if (dryingTicks < recipe.time) {
                tag.putInt(HTConst.DRYING_TICKS, dryingTicks + 1)
                if (dryingTicks % 10 == 0) {
                    level.levelEvent(1501, event.pos, 0)
                }
            } else {
                popResult(input, recipe, level, entity)
                if (entity.item.isEmpty) {
                    entity.discard()
                }
            }
        }
    }*/

    /**
     * [HCExplodingRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onExploded(event: ExplosionEvent.Detonate) {
        val level: Level = event.level
        if (level.isClientSide) return
        val iterator: MutableIterator<Entity> = event.affectedEntities.iterator()
        while (iterator.hasNext()) {
            val entity: Entity = iterator.next()
            if (entity is ItemEntity && entity.isAlive && !isCompleted(entity)) {
                val input = HCExplodingRecipe.Input(entity.item, event.explosion.radius().toFraction())
                val (_, recipe: HCExplodingRecipe) = HCRecipeTypes.EXPLODING.getRecipeFor(input, level, null) ?: continue
                popResult(input, recipe, level, entity)
                if (entity.item.isEmpty) {
                    iterator.remove()
                    entity.discard()
                }
            }
        }
    }

    //    Extensions    //

    @JvmStatic
    private fun isCompleted(entity: Entity): Boolean = entity.persistentData.getBoolean(HTConst.COMPLETED_RECIPE)

    @JvmStatic
    private fun createInput(entity: ItemEntity): SingleRecipeInput = SingleRecipeInput(entity.item)

    @JvmStatic
    private fun <INPUT : RecipeInput> popResult(
        input: INPUT,
        recipe: HCSingleItemRecipe<INPUT>,
        level: Level,
        entity: ItemEntity,
    ): Int = popResult(recipe.assemble(input, level.registryAccess()), recipe.ingredient.amount, entity)

    @JvmStatic
    private fun popResult(result: ItemStack, recipeAmount: Int, entity: ItemEntity): Int {
        if (result.isEmpty) return 0
        val multiplier: Int = entity.item.count / recipeAmount
        result
            .copyWithCount(result.count * multiplier)
            .let(entity::spawnAtLocation)
            ?.also { itemEntity: ItemEntity ->
                entity.item.count -= multiplier * recipeAmount
                itemEntity.persistentData.putBoolean(HTConst.COMPLETED_RECIPE, true)
            }
        return multiplier
    }
}
