package hiiragi283.core.api.material.property

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.world.item.Item

/**
 * 素材に基づいた鍛冶型レシピを表すクラスです。
 * @param template 鍛冶型となるアイテム
 * @param base 材料となる素材
 * @param allowCrafting クラフトレシピも追加するかどうか
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
@JvmRecord
data class HTSmithingRecipeProperty(val template: SupplierWithId<Item>, val base: HTMaterialKey, val allowCrafting: Boolean)
