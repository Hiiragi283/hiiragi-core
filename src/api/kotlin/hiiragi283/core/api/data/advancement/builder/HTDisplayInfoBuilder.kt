package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.data.holder.HTItemStackHolder
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.resources.ResourceLocation

/**
 * [DisplayInfo]のビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTDisplayInfoBuilder {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(key: HTAdvancementKey, builderAction: HTDisplayInfoBuilder.() -> Unit): DisplayInfo = HTDisplayInfoBuilder()
            .apply {
                titleText += translatableText(key.titleKey)
                descText += translatableText(key.descKey)
            }.apply(builderAction)
            .build()
    }

    val iconStack = HTItemStackHolder()
    val titleText = TextHolder()
    val descText = TextHolder()
    var backGround: ResourceLocation? = null
    var type: AdvancementType = AdvancementType.TASK
    var showToast: Boolean = true
    var showChat: Boolean = true
    var hidden: Boolean = false

    fun build(): DisplayInfo = DisplayInfo(
        iconStack.stack,
        titleText.text,
        descText.text,
        backGround.wrapOptional(),
        type,
        showToast,
        showChat,
        hidden,
    )

    inner class TextHolder {
        lateinit var text: Text
            private set

        operator fun plusAssign(text: Text) {
            check(!::text.isInitialized) { "Text has already been initialized" }
            this.text = text
        }
    }
}
