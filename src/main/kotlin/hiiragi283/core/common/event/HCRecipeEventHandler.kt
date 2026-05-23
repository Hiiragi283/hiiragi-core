package hiiragi283.core.common.event

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTAnvilLandEvent
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.item.enchantment.toInstances
import hiiragi283.core.api.item.toStack
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.world.HCInWorldRecipeCaches
import hiiragi283.core.mixin.RecipeManagerAccessor
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCItems
import hiiragi283.core.util.HTShapelessRecipeHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.ReloadableServerResources
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent
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

    @SubscribeEvent
    fun registerBrewing(event: RegisterBrewingRecipesEvent) {
        event.builder.addRecipe(
            Ingredient.of(Items.DRAGON_BREATH),
            Ingredient.of(HCItems.IRIDESCENT_POWDER),
            HCItems.UNLIMITED_POTION.toStack(),
        )
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
            val input: ItemStack = entity.item
            val recipe: HCChargingRecipe = getCaches(level).charging.findFirstRecipe(input, level) ?: return
            spawnResults(entity) { recipe.assemble(input) }
            entity.discard()
            event.isCanceled = true
        }
    }

    /**
     * [HTItemToMultiItemRecipe]を処理するイベント
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
        val input: ItemStack = entity.item
        val recipe: HTItemToMultiItemRecipe = getCaches(level).crushing.findFirstRecipe(input, level) ?: return
        val inputAmount: Int = recipe.getRequiredAmount(input)
        val multiplier: Int = input.count / inputAmount
        (0 until multiplier)
            .flatMap { recipe.assemble(input) }
            .let(HTShapelessRecipeHelper::mergeStacks)
            .mapNotNull(entity::spawnAtLocation)
            .forEach(::setComplete)
        val remainder: Int = input.count - (inputAmount * multiplier)
        if (remainder == 0) {
            entity.discard()
        } else {
            entity.item = input.copyWithCount(remainder)
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
    private fun getCaches(level: Level): HCInWorldRecipeCaches = HCAttachmentTypes.IN_WORLD_RECIPE_CACHES.getData(level)

    @JvmStatic
    private fun isCompleted(entity: Entity): Boolean = entity.persistentData.getBoolean(HTConst.COMPLETED_RECIPE)

    @JvmStatic
    private fun setComplete(entity: Entity) {
        entity.persistentData.putBoolean(HTConst.COMPLETED_RECIPE, true)
    }

    @JvmStatic
    private fun spawnResults(entity: ItemEntity, result: () -> ItemStack) {
        (0 until entity.item.count)
            .map { result() }
            .let(HTShapelessRecipeHelper::mergeStacks)
            .mapNotNull(entity::spawnAtLocation)
            .forEach(::setComplete)
    }
}
