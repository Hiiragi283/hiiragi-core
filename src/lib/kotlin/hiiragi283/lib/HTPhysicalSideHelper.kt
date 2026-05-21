package hiiragi283.lib

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.registry.lookupResult
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.right
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.world.flag.FeatureElement
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import net.minecraft.world.item.crafting.RecipeMap
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RecipesReceivedEvent

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
@EventBusSubscriber
data object HTPhysicalSideHelper {
    /**
     * 現在の[レジストリへのアクセス][RegistryAccess]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     */
    @JvmStatic
    fun getRegistryAccess(): HTTextResult<RegistryAccess> = runForDist(
        { Minecraft.getInstance().level?.registryAccess() },
        { ServerLifecycleHooks.getCurrentServer()?.registryAccess() },
    )?.right() ?: HTTextResult("Could not get active registry access")

    @JvmStatic
    fun <T : Any> lookup(registryKey: RegistryKey<T>): HTTextResult<Registry<T>> = getRegistryAccess().flatMap { it.lookupResult(registryKey) }

    //    Feature Flag    //

    /**
     * 現在の[FeatureFlagSet]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     * @since 0.16.0
     **/
    @JvmStatic
    fun getFeatureFlags(): FeatureFlagSet = runForDist(
        { Minecraft.getInstance().level?.enabledFeatures() },
        { ServerLifecycleHooks.getCurrentServer()?.worldData?.enabledFeatures() },
    ) ?: FeatureFlags.DEFAULT_FLAGS

    @JvmStatic
    fun <T : FeatureElement> filteredLookup(registryKey: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = lookup(registryKey).map { it.filterFeatures(getFeatureFlags()) }

    //    RecipeMap    //

    @JvmStatic
    var cachedRecipes: RecipeMap = RecipeMap.EMPTY
        private set

    @SubscribeEvent
    fun onRecipeSync(event: RecipesReceivedEvent) {
        cachedRecipes = event.recipeMap
    }
}
