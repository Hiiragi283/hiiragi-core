@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.advancement.builder

import hiiragi283.core.api.data.advancement.AdvancementKey
import hiiragi283.core.api.data.advancement.descKey
import hiiragi283.core.api.data.advancement.titleKey
import hiiragi283.core.api.item.ItemInstanceBuilder
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.java
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class HTDisplayInfoBuilder {
    companion object {
        @JvmStatic
        inline fun create(key: AdvancementKey, builderAction: HTDisplayInfoBuilder.() -> Unit): DisplayInfo {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTDisplayInfoBuilder().apply {
                titleText = translatableText(key.titleKey)
                descText = translatableText(key.descKey)
                builderAction()
            }.build()
        }
    }

    @PublishedApi internal var icon: ItemStack by HTDelegates.onceInitialize()
    var titleText: Text by HTDelegates.onceInitialize()
    var descText: Text by HTDelegates.onceInitialize()
    var backGround: Option<ResourceLocation> by HTDelegates.optionalOnceInitialize()
    var type: AdvancementType by HTDelegates.onceInitialize { AdvancementType.TASK }
    var showToast: Boolean = true
    var showChat: Boolean = true
    var hidden: Boolean = false

    fun build(): DisplayInfo = DisplayInfo(
        icon,
        titleText,
        descText,
        backGround.java,
        type,
        showToast,
        showChat,
        hidden,
    )

    operator fun ItemStack.unaryPlus() {
        icon = this
    }

    inline fun icon(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        icon = ItemInstanceBuilder.buildStack(builderAction)
    }
}
