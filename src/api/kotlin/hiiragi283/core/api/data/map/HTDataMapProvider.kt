package hiiragi283.core.api.data.map

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

abstract class HTDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    protected lateinit var provider: HolderLookup.Provider
        private set
    protected val inputCreator: HTIngredientCreator = HTIngredientCreator

    final override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider
        gatherInternal()
    }

    protected abstract fun gatherInternal()

    protected fun furnaceFuel(builderAction: Builder<FurnaceFuel, Item>.() -> Unit) {
        builder(NeoForgeDataMaps.FURNACE_FUELS).apply(builderAction)
    }

    //    Extensions    //

    protected fun <T : Any, R : Any> Builder<T, R>.addHolder(holder: HTIdLike, value: T, vararg conditions: ICondition): Builder<T, R> =
        add(holder.getId(), value, false, *conditions)

    // Item
    protected fun <T : Any> Builder<T, Item>.addItem(item: ItemLike, value: T, vararg conditions: ICondition): Builder<T, Item> =
        this.addHolder(item.toItemLike(), value, *conditions)

    protected fun <T : Any> Builder<T, Item>.add(
        prefix: HTTagPrefix,
        material: HTMaterialLike,
        value: T,
        vararg conditions: ICondition,
    ): Builder<T, Item> = add(prefix.itemTagKey(material), value, false, *conditions)
}
