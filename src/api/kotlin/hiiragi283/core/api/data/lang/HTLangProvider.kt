package hiiragi283.core.api.data.lang

import com.google.gson.JsonObject
import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.text.HTHasTranslationKey
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.tags.TagKey
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.Tags.*
import java.util.*

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
abstract class HTLangProvider(private val modId: String, val langType: HTLangType) : ResourceGenTask {
    private val map: MutableMap<String, String> = TreeMap()

    final override fun accept(manager: ResourceManager, sink: ResourceSink) {
        addTranslations()

        sink.addLang(
            modId.toId(langType.name),
            map.entries.fold(JsonObject()) { json: JsonObject, (key: String, value: String) ->
                json.addProperty(key, value)
                json
            },
        )
    }

    protected abstract fun addTranslations()

    //    Extensions    //

    /**
     * [HTHasTranslationKey.translationKey]に基づいて翻訳名を追加します。
     */
    fun add(key: HTHasTranslationKey, value: String) {
        this.add(key.translationKey, value)
    }

    /**
     * 進捗の翻訳名を追加します。
     * @param title 進捗のタイトル名
     * @param desc 進捗の説明
     */
    fun add(key: HTAdvancementKey, title: String, desc: String) {
        this.add(key.titleKey, title)
        this.add(key.descKey, desc)
    }

    /**
     * エンチャントの翻訳名を追加します。
     * @param title エンチャントの翻訳名
     * @param desc エンチャントの説明
     */
    @JvmName("setEnchantment")
    fun add(key: ResourceKey<Enchantment>, title: String, desc: String) {
        this.add(key.toDescriptionKey("enchantment"), title)
        this.add(key.toDescriptionKey("enchantment", "desc"), desc)
    }

    /**
     * 液体の翻訳名を登録します。
     */
    fun add(content: HTFluidContent, value: String) {
        this.add(content.typeHolder.get().descriptionId, value)
        this.add(content.bucketHolder, getBucketName(value))
        this.add(content.fluidTag, value)
    }

    protected abstract fun getBucketName(value: String): String

    fun add(tagKey: TagKey<*>, value: String) {
        this.add(getTagTranslationKey(tagKey), value)
    }

    fun add(key: String, value: String) {
        check(map.put(key, value) == null) { "Duplicate translation key: $key" }
    }

    //    English    //

    abstract class English(modId: String) : HTLangProvider(modId, HTLangTypes.EN_US) {
        final override fun getBucketName(value: String): String = "$value Bucket"
    }

    //    Japanese    //

    abstract class Japanese(modId: String) : HTLangProvider(modId, HTLangTypes.JA_JP) {
        final override fun getBucketName(value: String): String = "${value}入りバケツ"
    }
}
