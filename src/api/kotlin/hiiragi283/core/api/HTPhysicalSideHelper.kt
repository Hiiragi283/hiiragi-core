package hiiragi283.core.api

import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

data object HTPhysicalSideHelper {
    /**
     * 現在の[レジストリへのアクセス][RegistryAccess]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     * @author Hiiragi Tsubasa
     * @since 0.15.3
     */
    @JvmStatic
    fun getRegistryAccess(): RegistryAccess? = runForDist(
        { Minecraft.getInstance().level?.registryAccess() },
        { ServerLifecycleHooks.getCurrentServer()?.registryAccess() },
    )
}
