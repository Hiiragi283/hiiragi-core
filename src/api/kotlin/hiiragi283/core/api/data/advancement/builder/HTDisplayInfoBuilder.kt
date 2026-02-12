package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.data.holder.HTItemStackHolder
import hiiragi283.core.api.text.translatableText
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import java.util.Optional

/**
 * [DisplayInfo]のビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTDisplayInfoBuilder {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        fun create(builderAction: HTDisplayInfoBuilder.() -> Unit): DisplayInfo = HTDisplayInfoBuilder().apply(builderAction).build()
    }

    val iconStack = HTItemStackHolder()
    val titleText = TextHolder(HTAdvancementKey::titleKey)
    val descText = TextHolder(HTAdvancementKey::descKey)
    var backGround: ResourceLocation? = null
    var type: AdvancementType = AdvancementType.TASK
    var showToast: Boolean = true
    var showChat: Boolean = true
    var hidden: Boolean = false

    fun build(): DisplayInfo = DisplayInfo(
        iconStack.stack,
        titleText.text,
        descText.text,
        Optional.ofNullable(backGround),
        type,
        showToast,
        showChat,
        hidden,
    )

    inner class TextHolder(private val factory: (HTAdvancementKey) -> String) {
        lateinit var text: Component
            private set

        operator fun plusAssign(key: HTAdvancementKey) {
            this.plusAssign(key.let(factory).let(::translatableText))
        }

        operator fun plusAssign(text: Component) {
            check(!::text.isInitialized) { "Text has already been initialized" }
            this.text = text
        }
    }
}
