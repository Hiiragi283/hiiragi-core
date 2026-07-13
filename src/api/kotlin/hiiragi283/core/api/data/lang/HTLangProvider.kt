package hiiragi283.core.api.data.lang

import hiiragi283.core.api.data.advancement.AdvancementKey
import hiiragi283.core.api.data.advancement.descKey
import hiiragi283.core.api.data.advancement.titleKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toLanguageKey
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.data.LanguageProvider

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[LanguageProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTLangProvider(output: PackOutput, val modId: String, val langType: HTLangType) : LanguageProvider(output, modId, langType.name.lowercase()) {
    companion object {
        @JvmField
        val BUCKET_PATTERN: HTLangPatternProvider = HTLangPatternProvider.create("%s Bucket", "%s入りバケツ")
    }

    /**
     * [HTHasTranslationKey.translationKey]に基づいて翻訳名を追加します。
     */
    fun add(translatable: HTHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    /**
     * 進捗の翻訳名を追加します。
     * @param title 進捗のタイトル名
     * @param desc 進捗の説明
     */
    protected fun add(key: AdvancementKey, title: String, desc: String) {
        add(key.titleKey, title)
        add(key.descKey, desc)
    }

    /**
     * エンチャントの翻訳名を追加します。
     * @param value エンチャントの翻訳名
     * @param desc エンチャントの説明
     */
    @JvmName("addEnchantment")
    protected fun add(key: ResourceKey<Enchantment>, value: String, desc: String) {
        add(key.toLanguageKey(), value)
        add(key.toLanguageKey("desc"), desc)
    }

    /**
     * 液体の翻訳名を登録します。
     */
    fun addFluid(content: HTFluidContent, value: String) {
        add(content.typeHolder, value)
        add(content.fluidTag, value)

        val bucketName: String = BUCKET_PATTERN.translate(langType, value)
        add(content.bucketHolder, bucketName)
        add(content.bucketTag, bucketName)
    }
}
