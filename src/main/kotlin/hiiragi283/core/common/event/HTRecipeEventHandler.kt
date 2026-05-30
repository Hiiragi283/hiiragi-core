package hiiragi283.core.common.event

import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.world.HCInWorldRecipeCaches
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTConstants
import hiiragi283.lib.entity.serverLevel
import hiiragi283.lib.recipe.result.HTResultHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.OnDatapackSyncEvent
import net.neoforged.neoforge.event.level.ExplosionEvent

@EventBusSubscriber
data object HTRecipeEventHandler {
    @SubscribeEvent
    fun onDatapackSync(event: OnDatapackSyncEvent) {
        HCRecipeTypes.REGISTER.entries.map { it.get() }.let(event::sendRecipes)
    }

    /**
     * [HCExplodingRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onExploded(event: ExplosionEvent.Detonate) {
        val level: Level = event.level
        if (level !is ServerLevel) return
        val iterator: MutableIterator<Entity> = event.affectedEntities.iterator()
        while (iterator.hasNext()) {
            val entity: Entity = iterator.next()
            if (entity is ItemEntity && entity.isAlive && !isCompleted(entity)) {
                val input: ItemStack = entity.item
                val recipe: HCExplodingRecipe = getCaches(level).exploding.findFirstRecipe(input, level) ?: continue
                spawnResults(entity) { recipe.assemble(input) }
                if (entity.item.isEmpty) {
                    iterator.remove()
                    entity.discard()
                }
            }
        }
    }

    //    Extensions    //

    @JvmStatic
    private fun getCaches(level: Level): HCInWorldRecipeCaches = level.getData(HCAttachmentTypes.IN_WORLD_RECIPE_CACHES)

    @JvmStatic
    private fun isCompleted(entity: Entity): Boolean = entity.persistentData.getBooleanOr(HTConstants.COMPLETED_RECIPE, false)

    @JvmStatic
    private fun setComplete(entity: Entity) {
        entity.persistentData.putBoolean(HTConstants.COMPLETED_RECIPE, true)
    }

    @JvmStatic
    private fun spawnResults(entity: ItemEntity, result: () -> ItemStack) {
        val level: ServerLevel = entity.serverLevel() ?: return
        (0 until entity.item.count)
            .map { result() }
            .let(HTResultHelper::mergeStacks)
            .mapNotNull { entity.spawnAtLocation(level, it) }
            .forEach(::setComplete)
    }
}
