@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.data.HolderAcceptor
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IntersectionIngredient
import net.neoforged.neoforge.registries.holdersets.AndHolderSet
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

/**
 * [Ingredient]および[HTItemIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
class IngredientBuilder {
    private var contents: Either<ICustomIngredient, HolderSet<Item>> by HTDelegates.onceInitialize()
    var count: Int = 1

    operator fun ICustomIngredient.unaryPlus() {
        contents = Either.Left(this)
    }

    @JvmName("unaryPlusCompound")
    operator fun List<Ingredient>.unaryPlus() {
        +CompoundIngredient(this)
    }

    operator fun HolderSet<Item>.unaryPlus() {
        contents = Either.Right(this)
    }

    inline fun items(builderAction: HolderAcceptor.ItemSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.ItemSetBuilder().apply(builderAction).build()
    }

    fun build(): Ingredient = contents.fold(ICustomIngredient::toVanilla, ::resolveHolderSet)

    private fun resolveHolderSet(holderSet: HolderSet<Item>): Ingredient = when (holderSet) {
        is AndHolderSet<Item> -> holderSet.components.map(::resolveHolderSet).let { IntersectionIngredient(it).toVanilla() }
        else -> Ingredient.fromValues(resolveToValues(holderSet).stream())
    }

    private fun resolveToValues(holderSet: HolderSet<Item>): List<Ingredient.Value> = when (holderSet) {
        is HolderSet.Named<Item> -> listOf(Ingredient.TagValue(holderSet.key()))
        is HolderSet.Direct<Item> -> holderSet.map(::ItemStack).map(Ingredient::ItemValue)
        is OrHolderSet<Item> -> holderSet.components.flatMap(::resolveToValues)
        else -> listOf()
    }

    fun buildSized(): HTItemIngredient = HTItemIngredient(build(), count)
}
