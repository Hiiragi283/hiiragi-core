package hiiragi283.lib.data.recipe

import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.lib.tag.RawTagKey
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.CompoundIngredient

class HTIngredientCreator(val itemGetter: HolderGetter<Item>) {
    constructor(provider: HolderGetter.Provider) : this(provider.lookupOrThrow(Registries.ITEM))

    // Item
    @JvmName("createItem")
    fun create(item: ItemLike): Ingredient = Ingredient.of(item)

    @JvmName("createItems")
    fun create(vararg items: ItemLike): Ingredient = Ingredient.of(*items)

    @JvmName("createItems")
    fun create(items: Collection<ItemLike>): Ingredient = Ingredient.of(items.stream())

    // Tag
    @JvmName("createTag")
    fun create(tagKey: TagKey<Item>): Ingredient = itemGetter.getOrThrow(tagKey).let(::create)

    @JvmName("createTag")
    fun create(tagKey: RawTagKey): Ingredient = create(tagKey.create(Registries.ITEM))

    @JvmName("createTag")
    fun create(prefix: HTTagPrefix, material: HTMaterialKey): Ingredient = create(prefix.materialTag(material))

    @JvmName("createTags")
    fun create(tagKeys: Iterable<TagKey<Item>>): Ingredient = when (tagKeys.count()) {
        1 -> tagKeys.first().let(::create)
        else -> tagKeys.map(itemGetter::getOrThrow).map(::create).let { CompoundIngredient(it) }.toVanilla()
    }

    fun create(holderSet: HolderSet<Item>): Ingredient = Ingredient.of(holderSet)

    // Custom
}
