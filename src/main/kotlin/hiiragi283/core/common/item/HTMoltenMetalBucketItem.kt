package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.fluid.HTMoltenMetalFluid
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.util.HTMoltenMetalHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

class HTMoltenMetalBucketItem(private val fluid: HTMoltenMetalFluid, properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {
    override fun getName(stack: ItemStack): Component = HTMoltenMetalHelper
        .getMoltenMetal(stack)
        ?.let { HCTranslation.MOLTEN_METAL_BUCKET.translate(it) }
        ?: super.getName(stack)

    //    HTSubCreativeTabContents    //

    override fun addItems(
        baseItem: HTItemHolderLike<*>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        consumer: Consumer<ItemStack>,
    ) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in HTMaterialManager.INSTANCE.entries) {
            if (!HTMoltenMetalHelper.isEnabled(definition, false)) continue
            consumer.accept(HTMoltenMetalHelper.createBucket(key))
        }
    }

    override fun shouldAddDefault(): Boolean = false
}
