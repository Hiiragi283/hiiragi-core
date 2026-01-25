package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.NonNullList
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * [材料][Ingredient]を保持する抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTIngredientHolder {
    @JvmName("addItem")
    operator fun plusAssign(item: ItemLike) {
        this.plusAssign(Ingredient.of(item))
    }

    @JvmName("addItems")
    operator fun plusAssign(items: Iterable<ItemLike>) {
        items
            .map(::ItemStack)
            .map(Ingredient::ItemValue)
            .stream()
            .let(Ingredient::fromValues)
            .let(this::plusAssign)
    }

    @JvmName("addTag")
    operator fun plusAssign(tagKey: TagKey<Item>) {
        this.plusAssign(Ingredient.of(tagKey))
    }

    @JvmName("addTags")
    operator fun plusAssign(tagKeys: Iterable<TagKey<Item>>) {
        tagKeys
            .map(Ingredient::TagValue)
            .stream()
            .let(Ingredient::fromValues)
            .let(this::plusAssign)
    }

    @JvmName("addMaterialTag")
    operator fun plusAssign(pair: Pair<HTTagPrefix, HTMaterialLike>) {
        val (tagPrefix: HTTagPrefix, material: HTMaterialLike) = pair
        this.plusAssign(tagPrefix.itemTagKey(material))
    }

    @JvmName("addMaterialTags")
    operator fun plusAssign(pair: Pair<Iterable<HTTagPrefix>, Iterable<HTMaterialLike>>) {
        val (prefixes: Iterable<HTTagPrefix>, materials: Iterable<HTMaterialLike>) = pair
        prefixes
            .flatMap { prefix: HTTagPrefix -> materials.map(prefix::itemTagKey) }
            .let(this::plusAssign)
    }

    abstract operator fun plusAssign(ingredient: Ingredient)

    /**
     * 単一の[材料][Ingredient]のみを保持する[HTIngredientHolder]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    class Single : HTIngredientHolder() {
        lateinit var ingredient: Ingredient
            private set

        fun orEmpty(): Ingredient = when {
            ::ingredient.isInitialized -> ingredient
            else -> Ingredient.EMPTY
        }

        override operator fun plusAssign(ingredient: Ingredient) {
            check(!::ingredient.isInitialized) { "Ingredient has already been initialized" }
            this.ingredient = ingredient
        }
    }

    /**
     * 複数の[材料][Ingredient]を保持する[HTIngredientHolder]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    class Multiple : HTIngredientHolder() {
        private val ingredients: MutableList<Ingredient> = mutableListOf()

        override operator fun plusAssign(ingredient: Ingredient) {
            ingredients += ingredient
        }

        fun toList(): List<Ingredient> = ingredients

        fun toNonNull(): NonNullList<Ingredient> = NonNullList.copyOf(ingredients)
    }
}
