@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.HolderSetBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient

@HTBuilderMarker
class IngredientBuilder {
    private var contents: Either<ICustomIngredient, HolderSet<Item>> by HTDelegates.onceInitialize()
    var count: Int = 1

    operator fun ICustomIngredient.unaryPlus() {
        contents = Either.Left(this)
    }

    operator fun HolderSet<Item>.unaryPlus() {
        contents = Either.Right(this)
    }

    inline fun holderSet(builderAction: HolderSetBuilder<Item>.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderSetBuilder<Item>().apply(builderAction).build()
    }

    inline fun items(builderAction: ItemSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +ItemSetBuilder().apply(builderAction).build()
    }

    @HTBuilderMarker
    class ItemSetBuilder {
        private val items: MutableSet<Item> = mutableSetOf()

        operator fun ItemLike.unaryPlus() {
            items += this.asItem()
        }

        @Suppress("DEPRECATION")
        fun build(): HolderSet<Item> = HolderSet.direct(Item::builtInRegistryHolder, items)
    }

    fun build(): Ingredient = contents.fold(ICustomIngredient::toVanilla, Ingredient::of)

    fun buildSized(): HTItemIngredient = HTItemIngredient(build(), count)
}
