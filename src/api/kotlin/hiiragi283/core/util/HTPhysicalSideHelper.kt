package hiiragi283.core.util

import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
data object HTPhysicalSideHelper {
    /**
     * 現在の[レジストリへのアクセス][net.minecraft.core.RegistryAccess]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     */
    @JvmStatic
    fun getRegistryAccess(): RegistryAccess? = runForDist(
        { Minecraft.getInstance().level?.registryAccess() },
        { ServerLifecycleHooks.getCurrentServer()?.registryAccess() },
    )
}
