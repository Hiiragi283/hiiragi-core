package hiiragi283.core.api.event

import com.mojang.logging.LogUtils
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.holderSetOrNull
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.common.conditions.ICondition
import org.slf4j.Logger
import java.util.function.Consumer

/**
 * @see plus.dragons.createdragonsplus.common.recipe.UpdateRecipesEvent
 */
class HTRegisterRuntimeRecipeEvent(
    val registryAccess: RegistryAccess,
    val recipeManager: RecipeManager,
    private val consumer: Consumer<RecipeHolder<*>>,
) : Event() {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    val output: RecipeOutput = object : RecipeOutput {
        override fun accept(
            id: ResourceLocation,
            recipe: Recipe<*>,
            advancement: AdvancementHolder?,
            vararg conditions: ICondition?,
        ) {
            val id1: ResourceLocation = id.withPrefix("runtime/")
            consumer.accept(RecipeHolder(id1, recipe))
            LOGGER.debug("Added runtime recipe {}", id1)
        }

        override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
    }

    fun isPresentTag(prefix: HTPrefixLike, material: HTMaterialLike): Boolean = isPresentTag(prefix.itemTagKey(material))

    fun getFirstHolder(prefix: HTPrefixLike, material: HTMaterialLike): Holder<Item>? = getFirstHolder(prefix.itemTagKey(material))

    fun <T : Any> isPresentTag(tagKey: TagKey<T>): Boolean = getFirstHolder(tagKey) != null

    fun <T : Any> getFirstHolder(tagKey: TagKey<T>): Holder<T>? = registryAccess.holderSetOrNull(tagKey)?.firstOrNull()

    fun save(id: ResourceLocation, recipe: Recipe<*>) {
        output.accept(id, recipe, null)
    }
}
