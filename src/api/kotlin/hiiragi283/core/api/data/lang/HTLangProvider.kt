package hiiragi283.core.api.data.lang

import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.data.advancement.descKey
import hiiragi283.core.api.data.advancement.titleKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toDescriptionKey
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
sealed class HTLangProvider(output: PackOutput, modid: String, val langType: HTLanguageType) :
    LanguageProvider(output, modid, langType.name.lowercase()) {
    // HTHasTranslationKey
    /**
     * [HTHasTranslationKey.translationKey]に基づいて翻訳名を追加します。
     */
    fun add(translatable: HTHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    // Registry

    /**
     * 進捗の翻訳名を追加します。
     * @param title 進捗のタイトル名
     * @param desc 進捗の説明
     */
    protected fun addAdvancement(key: HTAdvancementKey, title: String, desc: String) {
        add(key.titleKey, title)
        add(key.descKey, desc)
    }

    /**
     * エンチャントの翻訳名を追加します。
     * @param value エンチャントの翻訳名
     * @param desc エンチャントの説明
     */
    protected fun addEnchantment(key: ResourceKey<Enchantment>, value: String, desc: String) {
        add(key.toDescriptionKey("enchantment"), value)
        add(key.toDescriptionKey("enchantment", "desc"), desc)
    }

    /**
     * 液体の翻訳名を登録します。
     */
    fun addFluid(content: HTFluidContent<*, *, *>, value: String) {
        add(content.typeHolder.get().descriptionId, value)
        addFluidBucket(content, value)
        add(content.getFluidTag(), value)
    }

    protected abstract fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String)

    //    English    //

    /**
     * 英語向けの[HTLangProvider]の抽象クラスです。
     */
    abstract class English(output: PackOutput, modid: String) : HTLangProvider(output, modid, HTLanguageType.EN_US) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.bucket, "$value Bucket")
        }
    }

    //    Japanese    //

    /**
     * 日本語向けの[HTLangProvider]の抽象クラスです。
     */
    abstract class Japanese(output: PackOutput, modid: String) : HTLangProvider(output, modid, HTLanguageType.JA_JP) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.bucket, "${value}入りバケツ")
        }
    }
}
