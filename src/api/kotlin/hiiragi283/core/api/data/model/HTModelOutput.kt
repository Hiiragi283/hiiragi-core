package hiiragi283.core.api.data.model

import com.google.gson.JsonElement
import net.mehvahdjukaar.moonlight.api.resources.ResType
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer
import java.util.function.Supplier

/**
 * モデルJSONの出力先を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun interface HTModelOutput : BiConsumer<ResourceLocation, Supplier<JsonElement>> {
    /**
     * 指定された引数からモデルJSONを保存します。
     * @param id モデルJSONの[ID][ResourceLocation]
     * @param json モデルJSONの本体
     * @param resType 保存するリソースの種類
     */
    fun accept(id: ResourceLocation, json: JsonElement, resType: ResType)

    override fun accept(id: ResourceLocation, json: Supplier<JsonElement>) {
        accept(id, json.get(), ResType.MODELS)
    }
}
