package hiiragi283.core.internal.item

import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.getOrDefault
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTMaterialItem(private val getter: HTPropertyGetter, properties: Properties) : Item(properties) {
    override fun getCreatorModId(itemStack: ItemStack): String = getter.getOrDefault(HTMaterialPropertyKeys.ORIGIN_MOD_ID)
}
