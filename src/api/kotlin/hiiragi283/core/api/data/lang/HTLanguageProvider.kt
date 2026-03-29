package hiiragi283.core.api.data.lang

import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

abstract class HTLanguageProvider(output: PackOutput, modid: String, val langType: HTLangType) :
    LanguageProvider(output, modid, langType.name) {
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
     * 液体の翻訳名を登録します。
     */
    fun addFluid(content: HTFluidContent, value: String) {
        add(content.getFluidType().descriptionId, value)
        add(content.fluidTag, value)

        val bucketName: String = BUCKET_PATTERN.translate(langType, value)
        add(content.getBucket(), bucketName)
        add(content.bucketTag, bucketName)
    }
}
