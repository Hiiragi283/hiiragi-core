package hiiragi283.core.api.data.holder

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.Registries
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
    /**
     * 指定した[item]を追加します。
     */
    @JvmName("addItem")
    operator fun plusAssign(item: ItemLike) {
        this.plusAssign(Ingredient.of(item))
    }

    /**
     * 指定した[items]を単一の材料として追加します。
     */
    @JvmName("addItems")
    operator fun plusAssign(items: Iterable<ItemLike>) {
        items
            .map(::ItemStack)
            .map(Ingredient::ItemValue)
            .stream()
            .let(Ingredient::fromValues)
            .let(this::plusAssign)
    }

    /**
     * 指定した[rawTagKey]を追加します。
     */
    @JvmName("addRawTag")
    operator fun plusAssign(rawTagKey: RawTagKey) {
        this.plusAssign(rawTagKey.create(Registries.ITEM))
    }

    /**
     * 指定した[rawTagKeys]を単一の材料として追加します。
     */
    @JvmName("addRawTags")
    operator fun plusAssign(rawTagKeys: Iterable<RawTagKey>) {
        rawTagKeys.map { it.create(Registries.ITEM) }.let(this::plusAssign)
    }

    /**
     * 指定した[tagKey]を追加します。
     */
    @JvmName("addTag")
    operator fun plusAssign(tagKey: TagKey<Item>) {
        this.plusAssign(Ingredient.of(tagKey))
    }

    /**
     * 指定した[tagKeys]を単一の材料として追加します。
     */
    @JvmName("addTags")
    operator fun plusAssign(tagKeys: Iterable<TagKey<Item>>) {
        tagKeys
            .map(Ingredient::TagValue)
            .stream()
            .let(Ingredient::fromValues)
            .let(this::plusAssign)
    }

    /**
     * 指定した[pair]から，材料を追加します。
     */
    @JvmName("addMaterialTag")
    operator fun plusAssign(pair: Pair<HTTagPrefix, HTMaterialLike>) {
        val (tagPrefix: HTTagPrefix, material: HTMaterialLike) = pair
        this.plusAssign(tagPrefix.materialTag(material))
    }

    /**
     * 指定した[pair]から，複数の[TagKey]を単一の材料として追加します。
     */
    @JvmName("addMaterialTags")
    operator fun plusAssign(pair: Pair<Iterable<HTTagPrefix>, Iterable<HTMaterialLike>>) {
        val (prefixes: Iterable<HTTagPrefix>, materials: Iterable<HTMaterialLike>) = pair
        prefixes
            .flatMap { prefix: HTTagPrefix -> materials.map(prefix::materialTag) }
            .let(this::plusAssign)
    }

    /**
     * @since 0.10.0
     */
    @JvmName("addToolTags")
    operator fun plusAssign(toolType: HTToolType) {
        this.plusAssign(toolType.toolTags)
    }

    /**
     * 指定した[ingredient]を追加します。
     */
    abstract operator fun plusAssign(ingredient: Ingredient)

    /**
     * 単一の[材料][Ingredient]のみを保持する[HTIngredientHolder]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    class Single : HTIngredientHolder() {
        /**
         * 保持している単一の[材料][Ingredient]
         */
        lateinit var ingredient: Ingredient
            private set

        /**
         * 保持している[Ingredient]を返します。
         * @return 値が初期化されていない場合は[Ingredient.EMPTY]
         */
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

        /**
         * [List]に変換します。
         */
        fun toList(): List<Ingredient> = ingredients

        /**
         * [NonNullList]に変換します。
         */
        fun toNonNull(): NonNullList<Ingredient> = NonNullList.copyOf(ingredients)
    }
}
