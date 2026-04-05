package hiiragi283.core.api.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.registry.toFluidLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.Text
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeManager
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.common.conditions.ICondition

class HTRegisterRuntimeRecipeEvent(
    val recipeManager: RecipeManager,
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
                val id1: ResourceLocation = id.withPrefix("runtime/")
                patches += Result(id1, recipe)
                HiiragiCoreAPI.LOGGER.debug("Added runtime recipe {}", id1)
            }

            override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.14.0
     */
    fun removeRecipe(id: ResourceLocation) {
        patches += Result(id, null)
        HiiragiCoreAPI.LOGGER.debug("Removed recipe {}", id)
    }

    @JvmRecord
    data class Result(val key: ResourceLocation, val recipe: Recipe<*>?)

    // TagKey
    fun <T : Any> getHolderResult(tagKey: TagKey<T>): HTTextResult<HTSimpleHolderLike<T>> =
        HiiragiCoreAccess.INSTANCE.getFirstHolder(context.provider, tagKey)

    fun <T : Any> getFirstHolder(tagKey: TagKey<T>, printLog: Boolean): HTSimpleHolderLike<T>? = getHolderResult(tagKey)
        .mapOrElse(identity()) { message: Text ->
            if (printLog) HiiragiCoreAPI.LOGGER.debug(message.string)
            null
        }

    fun <T : Any> isPresentTag(tagKey: TagKey<T>): Boolean = context.provider.holderSetOrNull(tagKey) != null

    // Material
    fun getFirstHolder(prefix: HTTagPrefix, material: HTMaterialLike): HTSimpleItemHolderLike? =
        getFirstHolder(prefix.itemTagKey(material), true)?.toItemLike()

    fun isPresentTag(prefix: HTTagPrefix, material: HTMaterialLike): Boolean = isPresentTag(prefix.itemTagKey(material))

    fun getFirstHolder(part: HTFluidPart, material: HTMaterialLike): HTSimpleFluidHolderLike? =
        getFirstHolder(part.createTagKey(material), true)?.toFluidLike()

    fun isPresentTag(part: HTFluidPart, material: HTMaterialLike): Boolean = isPresentTag(part.createTagKey(material))
}
