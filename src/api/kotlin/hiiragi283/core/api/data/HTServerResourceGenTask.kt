package hiiragi283.core.api.data

import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.server.packs.resources.ResourceManager

/**
 * サーバーサイドのリソースに使用される[ResourceGenTask]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun interface HTServerResourceGenTask : ResourceGenTask {
    fun accept(sink: ResourceSink)

    override fun accept(manager: ResourceManager, sink: ResourceSink) {
        accept(sink)
    }
}
