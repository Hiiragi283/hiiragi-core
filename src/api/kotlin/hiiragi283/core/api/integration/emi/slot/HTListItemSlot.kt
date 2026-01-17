package hiiragi283.core.api.integration.emi.slot

import dev.emi.emi.api.stack.EmiIngredient
import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.monad.unwrap
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.item.HTItemResourceFactory
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * EMIにおける入力スロットで複数のアイテムを表示させるための[HTItemSlot]の実装クラスです。
 *
 * GTCEuに感謝！
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
class HTListItemSlot private constructor(private val content: Either<TagKey<Item>, List<HTItemResourceType>>, private val count: Int) :
    HTItemSlot.Basic(),
    HTDataSerializable.Empty {
        constructor(ingredient: HTItemIngredient) : this(ingredient.unwrap(), ingredient.getRequiredAmount())

        constructor(tagKey: TagKey<Item>, count: Int) : this(Either.Left(tagKey), count)

        constructor(resources: List<HTItemResourceType>, count: Int) : this(Either.Right(resources), count)

        constructor(stacks: List<ItemStack>) : this(stacks.mapNotNull(ItemStack::toResource), stacks.maxOfOrNull(ItemStack::getCount) ?: 1)

        fun getIngredient(): EmiIngredient = content.map(
            { tagKey: TagKey<Item> -> tagKey.toEmi(count) },
            { resources: List<HTItemResourceType> -> resources.map { it.toStack(count) }.map(ItemStack::toEmi).let(EmiIngredient::of) },
        )

        override fun setResource(resource: HTItemResourceType?) {}

        override fun setAmount(amount: Int) {}

        override fun getAmount(): Int = count

        override fun getResource(): HTItemResourceType? {
            val resources: List<HTItemResourceType> = content
                .mapLeft { tagKey: TagKey<Item> ->
                    BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).mapNotNull(HTItemResourceFactory::create)
                }.unwrap()
            if (resources.isEmpty()) return null
            val index: Int = ((System.currentTimeMillis() / 1000) % resources.size).toInt()
            return resources[index]
        }

        override fun getCapacity(resource: HTItemResourceType?): Int = Int.MAX_VALUE

        override fun isValid(resource: HTItemResourceType): Boolean = true
    }
