package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTAnvilLandEvent
import hiiragi283.core.api.event.HTStepOnBlockEvent
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.stack.toImmutable
import hiiragi283.core.common.recipe.HTChargingRecipe
import hiiragi283.core.common.recipe.HTCrushingRecipe
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTExplodingRecipe
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
import net.neoforged.neoforge.event.level.ExplosionEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HTRecipeEventHandler {
    /**
     * [HTChargingRecipe]を処理するイベント
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
            val input: HTRecipeInput = createInput(entity) ?: return
            val recipe: HTChargingRecipe = level.recipeManager
                .getRecipesFor(
                    HCRecipeTypes.CHARGING.get(),
                    input,
                    level,
                ).map(RecipeHolder<HTChargingRecipe>::value)
                .firstOrNull { it.energy >= HTChargingRecipe.DEFAULT_ENERGY } ?: return
            popResult(recipe.assembleItem(input, level.registryAccess()), recipe.ingredient.getRequiredAmount(), entity)
            if (entity.item.isEmpty) {
                entity.discard()
                event.isCanceled = true
            }
        }
    }

    /**
     * [HTCrushingRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onAnvilLand(event: HTAnvilLandEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        for (entity: ItemEntity in level.getEntitiesOfClass(ItemEntity::class.java, AABB(pos))) {
            if (isCompleted(entity)) continue
            val input: HTRecipeInput = createInput(entity) ?: continue
            val (_, recipe: HTCrushingRecipe) = HCRecipeTypes.CRUSHING.getRecipeFor(input, level, null) ?: continue
            popResult(input, recipe, level, entity)
            if (entity.item.isEmpty) {
                entity.discard()
            }
        }
    }

    @SubscribeEvent
    fun onStepOnBlock(event: HTStepOnBlockEvent) {
        val level: Level = event.level
        if (level.isClientSide) return
        if (!event.state.`is`(Blocks.MAGMA_BLOCK)) return
        val entity: ItemEntity = event.entity
        if (entity.isAlive && !isCompleted(entity)) {
            val input: HTRecipeInput = createInput(entity) ?: return
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
    }

    /**
     * [HTExplodingRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onExploded(event: ExplosionEvent.Detonate) {
        val level: Level = event.level
        if (level.isClientSide) return
        val iterator: MutableIterator<Entity> = event.affectedEntities.iterator()
        while (iterator.hasNext()) {
            val entity: Entity = iterator.next()
            if (entity is ItemEntity && entity.isAlive && !isCompleted(entity)) {
                val input: HTRecipeInput = createInput(entity) ?: continue
                val (_, recipe: HTExplodingRecipe) = HCRecipeTypes.EXPLODING.getRecipeFor(input, level, null) ?: continue
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
    private fun createInput(entity: ItemEntity): HTRecipeInput? = HTRecipeInput.create(null) {
        items += entity.item.toImmutable()
    }

    @JvmStatic
    private fun popResult(
        input: HTRecipeInput,
        recipe: HTSingleItemRecipe,
        level: Level,
        entity: ItemEntity,
    ): ItemEntity? = popResult(recipe.assembleItem(input, level.registryAccess()), recipe.ingredient.getRequiredAmount(), entity)

    @JvmStatic
    private fun popResult(result: ImmutableItemStack?, recipeAmount: Int, entity: ItemEntity): ItemEntity? {
        if (result == null) return null
        val multiplier: Int = entity.item.count / recipeAmount
        return result
            .copyWithAmount(result.amount() * multiplier)
            ?.unwrap()
            ?.let(entity::spawnAtLocation)
            ?.also { itemEntity: ItemEntity ->
                entity.item.count -= multiplier * recipeAmount
                itemEntity.persistentData.putBoolean(HTConst.COMPLETED_RECIPE, true)
            }
    }
}
