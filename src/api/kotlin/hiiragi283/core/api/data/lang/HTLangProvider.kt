package hiiragi283.core.api.data.lang

import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.data.advancement.descKey
import hiiragi283.core.api.data.advancement.titleKey
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.data.LanguageProvider
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[LanguageProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTLangProvider(output: PackOutput, val modId: String, val langType: HTLangType) :
    LanguageProvider(output, modId, langType.name.lowercase()) {
    // Material
    /**
     * @since 0.8.0
     */
    fun addMaterials() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE.entries) {
            if (key.getNamespace() != modId) continue
            // Block
            for ((prefix: HTTagPrefix, item: HTHasTranslationKey) in HTMaterialContentsAccess.INSTANCE.getBlockMap(key)) {
                val name: String = translate(langType, prefix, propertyMap) ?: continue
                add(item, name)
            }
            // Item
            for ((prefix: HTTagPrefix, item: HTHasTranslationKey) in HTMaterialContentsAccess.INSTANCE.getItemMap(key)) {
                val name: String = translate(langType, prefix, propertyMap) ?: continue
                add(item, name)
            }
            // Tool
            for ((toolType: HTToolType, item: HTHasTranslationKey) in HTMaterialContentsAccess.INSTANCE.getToolMap(key)) {
                val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: continue
                add(item, toolType.langPattern.translate(langType, materialName))
            }
        }
    }

    /**
     * @since 0.8.0
     */
    fun translate(type: HTLangType, prefix: HTTagPrefix, propertyMap: HTPropertyMap): String? =
        propertyMap.getOrDefault(HTMaterialPropertyKeys.CUSTOM_LANG_NAME)[prefix]?.getTranslatedName(type) ?: run {
            val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
            prefix.getOrDefault(HTTagPropertyKeys.LANG_PATTERN).translate(type, materialName)
        }

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
        add(content.fluidTag, value)
    }

    protected abstract fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String)

    //    English    //

    /**
     * 英語向けの[HTLangProvider]の抽象クラスです。
     */
    abstract class English(output: PackOutput, modid: String) : HTLangProvider(output, modid, HTLangTypes.EN_US) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.bucketHolder, "$value Bucket")
        }
    }

    //    Japanese    //

    /**
     * 日本語向けの[HTLangProvider]の抽象クラスです。
     */
    abstract class Japanese(output: PackOutput, modid: String) : HTLangProvider(output, modid, HTLangTypes.JA_JP) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.bucketHolder, "${value}入りバケツ")
        }
    }
}
