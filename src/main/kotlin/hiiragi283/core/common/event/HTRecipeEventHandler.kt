package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.stack.toImmutable
import hiiragi283.core.common.recipe.HTChargingRecipe
import hiiragi283.core.common.recipe.HTExplodingRecipe
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
import net.neoforged.neoforge.event.level.ExplosionEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HTRecipeEventHandler {
    /**
     * [HTExplodingRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onStruck(event: EntityStruckByLightningEvent) {
        val entity: Entity = event.entity
        if (entity.persistentData.getBoolean(HTConst.PREVENT_LIGHTNING_STRIKE)) {
            event.isCanceled = true
            return
        }
        val level: Level = entity.level()
        if (level.isClientSide) return
        if (entity is ItemEntity && entity.isAlive) {
            val input: HTRecipeInput = HTRecipeInput.create(null) {
                items += entity.item.toImmutable()
            } ?: return
            val recipe: HTChargingRecipe = level.recipeManager
                .getRecipesFor(
                    HCRecipeTypes.CHARGING.get(),
                    input,
                    level,
                ).map(RecipeHolder<HTChargingRecipe>::value)
                .firstOrNull { it.energy >= HTChargingRecipe.DEFAULT_ENERGY } ?: return
            val result: ImmutableItemStack = recipe.assembleItem(input, level.registryAccess()) ?: return
            val requiredAmount: Int = recipe.ingredient.getRequiredAmount()
            while (entity.item.count >= requiredAmount) {
                entity.spawnAtLocation(result.unwrap())?.persistentData?.putBoolean(HTConst.PREVENT_LIGHTNING_STRIKE, true)
                entity.item.count -= requiredAmount
            }
            if (entity.item.isEmpty) {
                entity.discard()
                event.isCanceled = true
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
            if (entity is ItemEntity && entity.isAlive) {
                val input: HTRecipeInput = HTRecipeInput.create(null) {
                    items += entity.item.toImmutable()
                } ?: continue
                val (_, recipe: HTExplodingRecipe) = HCRecipeTypes.EXPLODING.getRecipeFor(input, level, null) ?: continue
                val result: ImmutableItemStack = recipe.assembleItem(input, level.registryAccess()) ?: continue
                val requiredAmount: Int = recipe.ingredient.getRequiredAmount()
                while (entity.item.count >= requiredAmount) {
                    entity.spawnAtLocation(result.unwrap())
                    entity.item.count -= requiredAmount
                }
                if (entity.item.isEmpty) {
                    iterator.remove()
                    entity.discard()
                }
            }
        }
    }
}
