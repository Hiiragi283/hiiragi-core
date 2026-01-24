package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

/**
 * 単一の[ItemStack]を完成品にとるレシピ向けの[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTStackRecipeBuilder(prefix: String) : HTRecipeBuilder(prefix) {
    final override fun getPrimalId(): ResourceLocation = resultStack.getId()

    //    Result    //

    val resultStack: ResultStack = ResultStack()

    inner class ResultStack : HTIdLike {
        lateinit var stack: ItemStack
            private set

        @JvmName("setStack")
        operator fun plusAssign(stack: ItemStack) {
            this.stack = stack
        }

        @JvmName("setItem")
        operator fun plusAssign(item: ItemLike) {
            this.plusAssign(ItemStack(item))
        }

        @JvmName("setItem")
        operator fun plusAssign(pair: Pair<ItemLike, Int>) {
            val (item: ItemLike, count: Int) = pair
            this.plusAssign(ItemStack(item, count))
        }

        @JvmName("setResource")
        operator fun plusAssign(resource: HTItemResourceType) {
            this.plusAssign(resource.toStack())
        }

        @JvmName("setResource")
        operator fun plusAssign(pair: Pair<HTItemResourceType, Int>) {
            val (resource: HTItemResourceType, count: Int) = pair
            this.plusAssign(resource.toStack(count))
        }

        override fun getId(): ResourceLocation = stack.itemHolder.toLike().getId()
    }

    //    IngredientHolder    //

    abstract inner class IngredientHolder {
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
    }

    inner class SingleIngredientHolder : IngredientHolder() {
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

    inner class ListIngredientHolder : IngredientHolder() {
        val ingredients: List<Ingredient> get() = _ingredients
        private val _ingredients: MutableList<Ingredient> = mutableListOf()

        override operator fun plusAssign(ingredient: Ingredient) {
            _ingredients += ingredient
        }

        fun toNonNull(): NonNullList<Ingredient> = NonNullList.copyOf(_ingredients)
    }
}
