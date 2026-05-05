package hiiragi283.core.util

import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

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

    /**
     * 現在の[FeatureFlagSet]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     */
    @JvmStatic
    fun getFeatureFlags(): FeatureFlagSet = runForDist(
        { Minecraft.getInstance().level?.enabledFeatures() },
        { ServerLifecycleHooks.getCurrentServer()?.worldData?.enabledFeatures() },
    ) ?: FeatureFlags.DEFAULT_FLAGS
}
