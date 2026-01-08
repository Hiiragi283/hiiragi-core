package hiiragi283.core.api.material.attribute

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@JvmInline
value class HTBaseIngredientMaterialAttribute(val ingredient: TagKey<Item>) : HTMaterialAttribute
