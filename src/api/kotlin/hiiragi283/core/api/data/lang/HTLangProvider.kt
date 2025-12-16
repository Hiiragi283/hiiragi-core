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

sealed class HTLangProvider(output: PackOutput, modid: String, val langType: HTLanguageType) :
    LanguageProvider(output, modid, langType.name.lowercase()) {
    // HTHasTranslationKey
    fun add(translatable: HTHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    // Registry
    protected fun addAdvancement(key: HTAdvancementKey, title: String, desc: String) {
        add(key.titleKey, title)
        add(key.descKey, desc)
    }

    protected fun addEnchantment(key: ResourceKey<Enchantment>, value: String, desc: String) {
        add(key.toDescriptionKey("enchantment"), value)
        add(key.toDescriptionKey("enchantment", "desc"), desc)
    }

    fun addFluid(content: HTFluidContent<*, *, *>, value: String) {
        add(content.typeHolder.get().descriptionId, value)
        addFluidBucket(content, value)
        add(content.getFluidTag(), value)
    }

    protected abstract fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String)

    //    English    //

    abstract class English(output: PackOutput, modid: String) : HTLangProvider(output, modid, HTLanguageType.EN_US) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.bucket, "$value Bucket")
        }
    }

    //    Japanese    //

    abstract class Japanese(output: PackOutput, modid: String) : HTLangProvider(output, modid, HTLanguageType.JA_JP) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.bucket, "${value}入りバケツ")
        }
    }
}
