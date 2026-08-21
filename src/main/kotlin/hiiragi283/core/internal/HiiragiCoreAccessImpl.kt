package hiiragi283.core.internal

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.config.HCConfig
import hiiragi283.core.util.HTPluginLoader
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment

class HiiragiCoreAccessImpl : HiiragiCoreAccess() {
    companion object {
        @JvmStatic
        private val modIdComparator: Comparator<HTIdLike> by lazy {
            Comparator
                .comparingInt { id: HTIdLike ->
                    val modIds: List<String> = HCConfig.SERVER.tagOutputPriority.get()
                    when (val priority: Int = modIds.indexOf(id.namespace)) {
                        -1 -> modIds.size
                        else -> priority
                    }
                }.thenBy(HTIdLike::namespace)
        }

        @JvmStatic
        private val pluginComparator: Comparator<HTMaterialPlugin> = compareBy(HTMaterialPlugin::priority).thenComparing(HTMaterialPlugin::getId, HTComparators.ID)
    }

    override val enableDebugFeatures: Boolean get() = !FMLEnvironment.production || HCConfig.COMMON.enableDebugFeatures.get()

    override val materialPlugins: Sequence<HTMaterialPlugin> by lazy {
        HTPluginLoader
            .collectPlugins<HTMaterialPlugin>()
            .filter {
                val modId: String = it.namespace
                modId in HTConst.getBuiltInIdSet(HiiragiCoreAPI.MOD_ID) || ModList.get().isLoaded(modId)
            }.sortedWith(pluginComparator)
    }

    override fun <T : Any> getFirstHolder(holders: Iterable<Holder<T>>): SimpleSupplierWithKey<T> = holders.asSequence().map(Holder<T>::toLike).sortedWith(modIdComparator).first()

    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(context: HTRecipeLookup.Context, recipeType: RecipeType<RECIPE>): Map<ResourceLocation, RECIPE> {
        val map: MutableMap<ResourceLocation, RECIPE> = mutableMapOf()
        for ((first: ResourceLocation, second: RECIPE) in context.getAllRecipes(recipeType)) {
            if (!second.isIncomplete) map[first] = second
        }
        return map
    }
}
