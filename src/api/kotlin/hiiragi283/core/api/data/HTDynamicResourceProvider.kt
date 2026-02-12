package hiiragi283.core.api.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.toId
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicClientResourceProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicServerResourceProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.PackGenerationStrategy

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
object HTDynamicResourceProvider {
    /**
     * クライアント側での動的リソースを提供する抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class Client(modId: String) :
        DynamicClientResourceProvider(modId.toId("dynamic_resources"), PackGenerationStrategy.REGEN_ON_EVERY_RELOAD) {
        final override fun gatherSupportedNamespaces(): Collection<String> = buildSet {
            this += HTConst.MINECRAFT
            this += HTConst.COMMON
            this += HTConst.NEOFORGE
        }
    }

    /**
     * サーバー側での動的リソースを提供する抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class Server(modId: String) :
        DynamicServerResourceProvider(modId.toId("dynamic_resources"), PackGenerationStrategy.REGEN_ON_EVERY_RELOAD) {
        final override fun gatherSupportedNamespaces(): Collection<String> = buildSet {
            this += HTConst.MINECRAFT
            this += HTConst.COMMON
            this += HTConst.NEOFORGE
        }
    }
}
