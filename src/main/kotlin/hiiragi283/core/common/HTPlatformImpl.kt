package hiiragi283.core.common

import hiiragi283.core.setup.HCDataComponents
import hiiragi283.lib.HTPlatform
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.registry.toLike
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

class HTPlatformImpl : HTPlatform() {
    companion object {
        @JvmStatic
        private val modIdComparator: Comparator<HTIdLike> by lazy {
            Comparator
                .comparingInt { id: HTIdLike ->
                    val modIds: List<String> = emptyList() // HCConfig.COMMON.tagOutputPriority.get()
                    when (val priority: Int = modIds.indexOf(id.namespace)) {
                        -1 -> modIds.size
                        else -> priority
                    }
                }.thenBy(HTIdLike::namespace)
        }

        @JvmField
        val DEFAULT_POTION_HANDLER: HTPotionFluidManager.Handler = object : HTPotionFluidManager.Handler {
            override fun get(getter: DataComponentGetter): HTBottleType? = getter.get(HCDataComponents.BOTTLE_TYPE)

            override fun set(builder: DataComponentMap.Builder, bottleType: HTBottleType) {
                builder.set(HCDataComponents.BOTTLE_TYPE, bottleType)
            }

            override fun set(builder: DataComponentPatch.Builder, bottleType: HTBottleType) {
                builder.set(HCDataComponents.BOTTLE_TYPE, bottleType)
            }
        }
    }

    override fun <T> getContentsFromItem(instance: T): BottledPotionContents? where T : TypedInstance<Item>, T : DataComponentGetter {
        val bottleType: HTBottleType = DEFAULT_POTION_HANDLER[instance] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(instance)
        return BottledPotionContents(contents, bottleType)
    }

    override fun <T> getContentsFromFluid(instance: T): BottledPotionContents? where T : TypedInstance<Fluid>, T : DataComponentGetter {
        val handler: HTPotionFluidManager.Handler = when {
            instance.`is`(Tags.Fluids.WATER) -> return BottledPotionContents(Potions.WATER)
            else -> HTPotionFluidManager.getFluidHandler(instance.typeHolder().value()) ?: DEFAULT_POTION_HANDLER
        }
        val bottleType: HTBottleType = handler[instance] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(instance)
        return BottledPotionContents(contents, bottleType)
    }

    override fun fillFluidPatch(fluid: Fluid, contents: BottledPotionContents, builder: DataComponentPatch.Builder) {
        builder.set(DataComponents.POTION_CONTENTS, contents.contents)
        val handler: HTPotionFluidManager.Handler = HTPotionFluidManager.getFluidHandler(fluid) ?: DEFAULT_POTION_HANDLER
        handler[builder] = contents.bottleType
    }

    override fun fillItemPatch(contents: BottledPotionContents, builder: DataComponentPatch.Builder) {
        builder.set(DataComponents.POTION_CONTENTS, contents.contents)
        DEFAULT_POTION_HANDLER[builder] = contents.bottleType
    }

    override fun <T : Any> getFirstHolder(holders: Iterable<Holder<T>>): SupplierWithId<T> = holders.asSequence().map(Holder<T>::toLike).sortedWith(modIdComparator).first()
}
