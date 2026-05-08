package hiiragi283.core.util

import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.world.flag.FeatureElement
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import java.util.Optional

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
data object HTPhysicalSideHelper {
    /**
     * 現在の[レジストリへのアクセス][RegistryAccess]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     */
    @JvmStatic
    fun getRegistryAccess(): RegistryAccess? = runForDist(
        { Minecraft.getInstance().level?.registryAccess() },
        { ServerLifecycleHooks.getCurrentServer()?.registryAccess() },
    )

    @JvmStatic
    fun <T : Any> lookup(registryKey: RegistryKey<T>): Optional<HolderLookup.RegistryLookup<T>> = getRegistryAccess().wrapOptional().flatMap { it.lookup(registryKey) }

    @JvmStatic
    fun <T : Any> lookupOrThrow(registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T> = getRegistryAccess()?.lookupOrThrow(registryKey) ?: error("Missing Active Registry")

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
    fun <T : FeatureElement> filteredLookup(registryKey: RegistryKey<T>): Optional<HolderLookup.RegistryLookup<T>> = lookup(registryKey).map { it.filterFeatures(getFeatureFlags()) }

    @JvmStatic
    fun <T : FeatureElement> filteredLookupOrThrow(registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T> = lookupOrThrow(registryKey).filterFeatures(getFeatureFlags())
}
