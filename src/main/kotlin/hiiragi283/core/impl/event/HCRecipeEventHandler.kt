package hiiragi283.core.impl.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.entity.serverLevel
import hiiragi283.core.api.entity.spawnAtLocation
import hiiragi283.core.api.event.HTAnvilLandEvent
import hiiragi283.core.api.item.enchantment.toInstances
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.findFirst
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.base.HTItemToChancedRecipe
import hiiragi283.core.common.recipe.base.HTItemToItemRecipe
import hiiragi283.core.common.util.HTShapelessRecipeHelper
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.phys.AABB
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.OnDatapackSyncEvent
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
import net.neoforged.neoforge.transfer.item.ItemResource

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRecipeEventHandler {
    @SubscribeEvent
    fun syncRecipeToClient(event: OnDatapackSyncEvent) {
        event.sendRecipes(
            HCRecipeTypes.REGISTER
                .asSequence()
                .map { it.get() }
                .toList(),
        )
    }

    /**
     * [HTItemToItemRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onStruck(event: EntityStruckByLightningEvent) {
        val entity: Entity = event.entity
        if (isCompleted(entity)) {
            event.isCanceled = true
            return
        }
        val level: ServerLevel = entity.serverLevel() ?: return
        if (entity is ItemEntity && entity.isAlive) {
            val input: SingleRecipeInput = createInput(entity)
            val recipe: HCChargingRecipe = HCRecipeTypes.CHARGING.findFirst(input, level)?.value() ?: return
            popResult(input, recipe, 1, entity)
            if (entity.item.isEmpty) {
                entity.discard()
                event.isCanceled = true
            }
        }
    }

    /**
     * [HTItemToChancedRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onAnvilLand(event: HTAnvilLandEvent) {
        val level: ServerLevel = event.level as? ServerLevel ?: return
        for (entity: ItemEntity in level.getEntitiesOfClass(ItemEntity::class.java, AABB(event.pos))) {
            if (isCompleted(entity)) continue
            anvilCrushing(entity, level)
            splitEnchantment(entity, level)
        }
    }

    @JvmStatic
    private fun anvilCrushing(entity: ItemEntity, level: ServerLevel) {
        val input: SingleRecipeInput = createInput(entity)
        val recipe: HTItemToChancedRecipe.Serializable = HCRecipeTypes.CRUSHING.findFirst(input, level)?.value() ?: return
        val multiplier: Int = popResult(input, recipe, entity, HTItemToChancedRecipe::getRequiredAmount)
        (0 until multiplier)
            .map { recipe.assembleExtraItem(input, level.random) }
            .let(HTShapelessRecipeHelper::createMap)
            .map { (resource: ItemResource, count: Int) -> resource.toStack(count) }
            .forEach { entity.spawnAtLocation(level, it) }
        if (entity.item.isEmpty) {
            entity.discard()
        }
    }

    @JvmStatic
    private fun splitEnchantment(entity: ItemEntity, level: ServerLevel) {
        val stored: ItemEnchantments = entity.item.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
        if (stored.size() <= 1) return
        stored
            .toInstances()
            .map(EnchantmentHelper::createBook)
            .mapNotNull { entity.spawnAtLocation(level, it) }
            .onEach(::setComplete)
        entity.discard()
    }

    /**
     * [HCExplodingRecipe]を処理するイベント
     */
    /*@SubscribeEvent
    fun onExploded(event: ExplosionEvent.Detonate) {
        val level: Level = event.level
        if (level.isClientSide) return
        val iterator: MutableIterator<Entity> = event.affectedEntities.iterator()
        while (iterator.hasNext()) {
            val entity: Entity = iterator.next()
            if (entity is ItemEntity && entity.isAlive && !isCompleted(entity)) {
                val input = HCExplodingRecipe.Input(entity.item, event.explosion.radius().toFraction())
                val recipe: HCExplodingRecipe = getCaches(level).exploding.getFirstRecipe(input, level) ?: continue
                popResult(input, recipe, level, entity) { recipeIn, _ -> recipeIn.ingredient.amount }
                if (entity.item.isEmpty) {
                    iterator.remove()
                    entity.discard()
                }
            }
        }
    }*/

    /*@JvmStatic
    private lateinit var tankInteraction: HTJsonResourceReloadListener<HTTankInteraction.Serializable>

    @JvmStatic
    val tankInteractionMap: Map<ResourceLocation, HTTankInteraction.Serializable>
        get() = tankInteraction.resultMap

    @SubscribeEvent
    fun addReloadListener(event: AddReloadListenerEvent) {
        tankInteraction = HTJsonResourceReloadListener.create(HTConst.TANK_INTERACTION, HTTankInteraction.Serializable.CODEC)
        event.addListener(tankInteraction)
    }*/

    //    Extensions    //

    @JvmStatic
    private fun isCompleted(entity: Entity): Boolean = entity.persistentData.getBooleanOr(HTConst.COMPLETED_RECIPE, false)

    @JvmStatic
    private fun setComplete(entity: Entity) {
        entity.persistentData.putBoolean(HTConst.COMPLETED_RECIPE, true)
    }

    @JvmStatic
    private fun createInput(entity: ItemEntity): SingleRecipeInput = SingleRecipeInput(entity.item)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> popResult(
        input: INPUT,
        recipe: RECIPE,
        entity: ItemEntity,
        amountGetter: (RECIPE, INPUT) -> Int,
    ): Int = popResult(recipe.assemble(input), amountGetter(recipe, input), entity)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> popResult(
        input: INPUT,
        recipe: RECIPE,
        amount: Int,
        entity: ItemEntity,
    ): Int = popResult(recipe.assemble(input), amount, entity)

    @JvmStatic
    private fun popResult(result: ItemStack, recipeAmount: Int, entity: ItemEntity): Int {
        if (result.isEmpty) return 0
        val multiplier: Int = entity.item.count / recipeAmount
        result
            .copyWithCount(result.count * multiplier)
            .let(entity::spawnAtLocation)
            ?.also { itemEntity: ItemEntity ->
                entity.item.count -= multiplier * recipeAmount
                setComplete(itemEntity)
            }
        return multiplier
    }
}
