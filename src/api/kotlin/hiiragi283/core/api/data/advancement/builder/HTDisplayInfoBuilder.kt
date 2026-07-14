@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.data.advancement.AdvancementKey
import hiiragi283.core.api.data.advancement.descKey
import hiiragi283.core.api.data.advancement.titleKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.toOptional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * [DisplayInfo]のビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@HTBuilderMarker
class HTDisplayInfoBuilder {
    companion object {
        @JvmStatic
        inline fun create(key: AdvancementKey, builderAction: HTDisplayInfoBuilder.() -> Unit): DisplayInfo {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTDisplayInfoBuilder()
                .apply {
                    titleText += translatableText(key.titleKey)
                    descText += translatableText(key.descKey)
                    builderAction()
                }.build()
        }
    }

    var iconStack: ItemStack by HTDelegates.onceInitialize()
    val titleText = TextHolder()
    val descText = TextHolder()
    var backGround: ResourceLocation? = null
    var type: AdvancementType = AdvancementType.TASK
    var showToast: Boolean = true
    var showChat: Boolean = true
    var hidden: Boolean = false

    fun build(): DisplayInfo = DisplayInfo(
        iconStack,
        titleText.text,
        descText.text,
        backGround.toOptional(),
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
