package hiiragi283.core.api.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.getResult
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.util.ErrorText
import hiiragi283.core.api.util.HTTextResult
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.common.conditions.ICondition

class HTRegisterRuntimeRecipeEvent(
    recipeManager: RecipeManager,
    provider: HolderLookup.Provider,
    private val patches: MutableList<Result>,
) : Event() {
    val context: HTRecipeProviderContext = object : HTRecipeProviderContext() {
        override val provider: HolderLookup.Provider = provider
        override val output: RecipeOutput = object : RecipeOutput {
            override fun accept(
                id: ResourceLocation,
                recipe: Recipe<*>,
                advancement: AdvancementHolder?,
                vararg conditions: ICondition?,
            ) {
                addRecipe(id, recipe)
            }

            override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
        }
    }

    /**
     * 指定した[ID][id]で[レシピ][recipe]を追加します。
     * @since 0.15.0
     */
    fun addRecipe(id: ResourceLocation, recipe: Recipe<*>) {
        val id1: ResourceLocation = id.withPrefix("runtime/")
        patches += Result(id1, recipe)
        HiiragiCoreAPI.LOGGER.debug("Added runtime recipe {}", id)
    }

    /**
     * 指定した[ID][id]でレシピを削除します。
     * @since 0.14.0
     */
    fun removeRecipe(id: ResourceLocation) {
        patches += Result(id, null)
        HiiragiCoreAPI.LOGGER.debug("Removed recipe {}", id)
    }

    @JvmRecord
    data class Result(val key: ResourceLocation, val recipe: Recipe<*>?)

    /**
     * @since 0.16.0
     */
    val lookupContext: HTRecipeLookup.Context = HTRecipeLookup.Context.create {
        this[HTRecipeLookup.Context.MANAGER] = recipeManager
        this[HTRecipeLookup.Context.REGISTRY] = provider
    }

    /**
     * @since 0.16.0
     */
    fun <RECIPE : Any> getAllRecipes(lookup: HTRecipeLookup<RECIPE>): Map<ResourceLocation, RECIPE> = lookup.getAllRecipes(lookupContext)

    // TagKey
    fun <T : Any> getHolderResult(tagKey: TagKey<T>): HTTextResult<SupplierWithId<T>> = HiiragiCoreAccess.INSTANCE.getFirstHolder(context.provider, tagKey)

    fun <T : Any> getFirstHolder(tagKey: TagKey<T>, printLog: Boolean): SupplierWithId<T>? = getHolderResult(tagKey)
        .onLeft { errorText: ErrorText -> if (printLog) HiiragiCoreAPI.LOGGER.debug(errorText.value) }
        .getOrNull()

    fun <T : Any> isPresentTag(tagKey: TagKey<T>): Boolean = context.provider.asGetterLookup().getResult(tagKey).isRight()

    // Material
    fun getFirstHolder(prefix: HTTagPrefix, material: HTMaterialLike): SupplierWithId<Item>? = getFirstHolder(prefix.itemTagKey(material), true)

    fun isPresentTag(prefix: HTTagPrefix, material: HTMaterialLike): Boolean = isPresentTag(prefix.itemTagKey(material))

    fun getFirstHolder(part: HTFluidPart, material: HTMaterialLike): SupplierWithId<Fluid>? = getFirstHolder(part.createTagKey(material), true)

    fun isPresentTag(part: HTFluidPart, material: HTMaterialLike): Boolean = isPresentTag(part.createTagKey(material))
}
