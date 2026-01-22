package hiiragi283.core.api.item.equipment

import hiiragi283.core.api.item.armor.HTArmorMaterial
import hiiragi283.core.api.item.tool.HTToolMaterial
import net.minecraft.world.item.crafting.Ingredient

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see HTArmorMaterial
 * @see HTToolMaterial
 */
interface HTEquipmentMaterial {
    fun getEnchantmentValue(): Int

    fun getRepairIngredient(): Ingredient
}
