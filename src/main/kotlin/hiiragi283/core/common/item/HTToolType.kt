package hiiragi283.core.common.item

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.item.HTEquipmentMaterial
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

interface HTToolType<TOOL : Item> :
    HTLangPatternProvider,
    StringRepresentable {
    fun getToolTags(): List<TagKey<Item>>

    fun createItem(
        register: HTDeferredItemRegister,
        material: HTEquipmentMaterial,
        name: String = "${material.asMaterialName()}_$serializedName",
    ): HTDeferredItem<TOOL>
}
