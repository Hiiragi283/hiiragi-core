package hiiragi283.core.api.item.equipment

import hiiragi283.core.api.item.armor.HTArmorMaterial
import hiiragi283.core.api.item.tool.HTToolMaterial
import net.minecraft.world.item.crafting.Ingredient

/**
 * 装備品の素材を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see HTArmorMaterial
 * @see HTToolMaterial
 */
interface HTEquipmentMaterial {
    /**
     * この素材のエンチャント値を取得します。
     */
    fun getEnchantmentValue(): Int

    /**
     * この素材の修理に必要な[材料][Ingredient]を取得します。
     */
    fun getRepairIngredient(): Ingredient
}
