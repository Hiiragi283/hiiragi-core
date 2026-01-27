package hiiragi283.core.api.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.creator.HTFluidResultCreator
import hiiragi283.core.api.data.recipe.creator.HTIngredientCreator
import hiiragi283.core.api.data.recipe.creator.HTItemResultCreator
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.common.conditions.ICondition
import java.util.function.Consumer

/**
 * 動的にレシピを追加するイベントクラスです。
 * @see plus.dragons.createdragonsplus.common.recipe.UpdateRecipesEvent
 */
class HTRegisterRuntimeRecipeEvent(
    val registryAccess: RegistryAccess,
    val recipeManager: RecipeManager,
    private val consumer: Consumer<RecipeHolder<*>>,
) : Event() {
    val output: RecipeOutput = object : RecipeOutput {
        override fun accept(
            id: ResourceLocation,
            recipe: Recipe<*>,
            advancement: AdvancementHolder?,
            vararg conditions: ICondition?,
        ) {
            val id1: ResourceLocation = id.withPrefix("runtime/")
            consumer.accept(RecipeHolder(id1, recipe))
            HiiragiCoreAPI.LOGGER.debug("Added runtime recipe {}", id1)
        }

        override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
    }

    val inputCreator: HTIngredientCreator = HTIngredientCreator
    val itemResult: HTItemResultCreator = HTItemResultCreator
    val fluidResult: HTFluidResultCreator = HTFluidResultCreator

    val materialManager: HTMaterialManager by lazy(HiiragiCoreAccess.INSTANCE::materialManager)

    fun save(id: ResourceLocation, recipe: Recipe<*>) {
        output.accept(id, recipe, null)
    }

    // TagKey
    fun <T : Any> getHolderResult(tagKey: TagKey<T>): HTTextResult<Holder<T>> =
        HiiragiCoreAccess.INSTANCE.getFirstHolder(registryAccess, tagKey)

    fun <T : Any> getFirstHolder(tagKey: TagKey<T>, printLog: Boolean): Holder<T>? = getHolderResult(tagKey)
        .mapOrElse(identity()) { message: Component ->
            if (printLog) HiiragiCoreAPI.LOGGER.warn(message.string)
            null
        }

    fun <T : Any> isPresentTag(tagKey: TagKey<T>): Boolean = registryAccess.holderSetOrNull(tagKey) != null

    // Material
    fun getFirstHolder(prefix: HTTagPrefix, material: HTMaterialLike): Holder<Item>? = getFirstHolder(prefix.itemTagKey(material), true)

    fun isPresentTag(prefix: HTTagPrefix, material: HTMaterialLike): Boolean = isPresentTag(prefix.itemTagKey(material))

    fun getAllMaterials(): Sequence<Map.Entry<HTMaterialKey, HTPropertyMap>> = materialManager.asSequence()
}
