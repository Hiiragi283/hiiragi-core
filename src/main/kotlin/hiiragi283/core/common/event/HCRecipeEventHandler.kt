package hiiragi283.core.common.event

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTAnvilLandEvent
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.item.enchantment.toInstances
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.toFraction
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.world.HCInWorldRecipeCaches
import hiiragi283.core.mixin.RecipeManagerAccessor
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.util.HTShapelessRecipeHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.ReloadableServerResources
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
import net.neoforged.neoforge.event.level.ExplosionEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCRecipeEventHandler {
    @JvmStatic
    fun registerRuntimeRecipe(registries: ReloadableServerResources) {
        val provider: HolderLookup.Provider = registries.fullRegistries().get()
        val recipeManager: RecipeManager = registries.recipeManager
        val patches: MutableList<HTRegisterRuntimeRecipeEvent.Result> = mutableListOf()
        val event = HTRegisterRuntimeRecipeEvent(recipeManager, provider, patches)
        NeoForge.EVENT_BUS.post(event)

        val accessor: RecipeManagerAccessor = recipeManager as RecipeManagerAccessor
        val byType: Multimap<RecipeType<*>, RecipeHolder<*>> = HashMultimap.create(accessor.byType)
        val byName: MutableMap<ResourceLocation, RecipeHolder<*>> = accessor.byName.toMutableMap()

        for (result: HTRegisterRuntimeRecipeEvent.Result in patches) {
            val (id: ResourceLocation, newRecipe: Recipe<*>?) = result
            if (newRecipe != null) {
                val holder: RecipeHolder<Recipe<*>> = RecipeHolder(id, newRecipe)
                byType.put(newRecipe.type, holder)
                byName[id] = holder
            } else {
                val oldHolder: RecipeHolder<*> = byName.remove(id) ?: continue
                byType.remove(oldHolder.value().type, oldHolder)
            }
        }

        accessor.byType = byType
        accessor.byName = byName
    }

    /**
     * [HCChargingRecipe]を処理するイベント
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
            val input = HCChargingRecipe.Input(entity.item, null)
            val recipe: HCChargingRecipe = getCaches(level).charging.getFirstRecipe(input, level) ?: return
            popResult(input, recipe, level, entity) { recipe: HCChargingRecipe, _ -> recipe.ingredient.amount }
            if (entity.item.isEmpty) {
                entity.discard()
                event.isCanceled = true
            }
        }
    }

    /**
     * [HTSingleMultiOutputRecipe]を処理するイベント
     */
    @SubscribeEvent
    fun onAnvilLand(event: HTAnvilLandEvent) {
        for (entity: ItemEntity in event.level.getEntitiesOfClass(ItemEntity::class.java, AABB(event.pos))) {
            if (isCompleted(entity)) continue
            anvilCrushing(entity)
            splitEnchantment(entity)
        }
    }

    @JvmStatic
    private fun anvilCrushing(entity: ItemEntity) {
        val level: Level = entity.level()
        val input: SingleRecipeInput = createInput(entity)
        val recipe: HTSingleMultiOutputRecipe = getCaches(level).crushing.getFirstRecipe(input, level) ?: return
        val multiplier: Int = popResult(input, recipe, level, entity, HTSingleMultiOutputRecipe::getRequiredAmount)
        (0 until multiplier)
            .flatMap { recipe.assembleItems(input, level.registryAccess()) }
            .let(HTShapelessRecipeHelper::createMap)
            .map { (resource: HTItemResourceType, count: Int) -> resource.toStack(count) }
            .forEach(entity::spawnAtLocation)
        if (entity.item.isEmpty) {
            entity.discard()
        }
    }

    @JvmStatic
    private fun splitEnchantment(entity: ItemEntity) {
        val stored: ItemEnchantments = entity.item.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
        if (stored.size() <= 1) return
        stored
            .toInstances()
            .map(EnchantedBookItem::createForEnchantment)
            .mapNotNull(entity::spawnAtLocation)
            .onEach(::setComplete)
        entity.discard()
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
                val recipe: HCExplodingRecipe = getCaches(level).exploding.getFirstRecipe(input, level) ?: continue
                popResult(input, recipe, level, entity) { recipeIn, _ -> recipeIn.ingredient.amount }
                if (entity.item.isEmpty) {
                    iterator.remove()
                    entity.discard()
                }
            }
        }
    }

    //    Extensions    //

    @JvmStatic
    private fun getCaches(level: Level): HCInWorldRecipeCaches = HCAttachmentTypes.IN_WORLD_RECIPE_CACHES.getData(level)

    @JvmStatic
    private fun isCompleted(entity: Entity): Boolean = entity.persistentData.getBoolean(HTConst.COMPLETED_RECIPE)

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
        level: Level,
        entity: ItemEntity,
        amountGetter: (RECIPE, INPUT) -> Int,
    ): Int = popResult(recipe.assemble(input, level.registryAccess()), amountGetter(recipe, input), entity)

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
