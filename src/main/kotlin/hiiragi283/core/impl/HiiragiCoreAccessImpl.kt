package hiiragi283.core.impl

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.config.HCConfig
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.util.HTPluginLoader
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.fml.ModList
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack

class HiiragiCoreAccessImpl : HiiragiCoreAccess() {
    companion object {
        @JvmStatic
        private val modIdComparator: Comparator<HTIdLike> by lazy {
            Comparator
                .comparingInt { id: HTIdLike ->
                    val modIds: List<String> = HCConfig.COMMON.tagOutputPriority.get()
                    when (val priority: Int = modIds.indexOf(id.namespace)) {
                        -1 -> modIds.size
                        else -> priority
                    }
                }.thenBy(HTIdLike::namespace)
        }

        @JvmStatic
        private val pluginComparator = compareBy(HTMaterialPlugin::priority).thenComparing(HTMaterialPlugin::getId, HTComparators.ID)

        @JvmField
        val DEFAULT_POTION_HANDLER: HTPotionFluidManager.Handler = object : HTPotionFluidManager.Handler {
            override fun get(holder: DataComponentHolder): HTBottleType? = holder.get(HCDataComponents.BOTTLE_TYPE)

            override fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType) {
                holder.set(HCDataComponents.BOTTLE_TYPE, bottleType)
            }
        }
    }

    override val materialPlugins: Sequence<HTMaterialPlugin> by lazy {
        HTPluginLoader
            .collectPlugins<HTMaterialPlugin>()
            .filter {
                val modId: String = it.namespace
                modId in HTConst.getBuiltInIdSet(HiiragiCoreAPI.MOD_ID) || ModList.get().isLoaded(modId)
            }.sortedWith(pluginComparator)
    }

    override val partManager: Map<String, HTPart> by lazy {
        buildMap {
            forEachPlugin("Register Part") { plugin: HTMaterialPlugin ->
                plugin.registerPart { name: String, idPattern: String, getter: HTPropertyGetter ->
                    val part = HTPart(name, idPattern, getter)
                    check(this.put(name, part) == null) { "Duplicated part registration: $name" }
                    part
                }
            }
        }
    }

    override fun getContents(resource: HTFluidResourceType): BottledPotionContents? {
        val handler: HTPotionFluidManager.Handler = when {
            resource.isOf(Tags.Fluids.WATER) -> return BottledPotionContents(Potions.WATER)
            else -> HTPotionFluidManager.getFluidHandler(resource.typeHolder()) ?: DEFAULT_POTION_HANDLER
        }
        val bottleType: HTBottleType = handler[resource] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(resource)
        return BottledPotionContents(contents, bottleType)
    }

    override fun getContents(resource: HTItemResourceType): BottledPotionContents? {
        val bottleType: HTBottleType = DEFAULT_POTION_HANDLER[resource] ?: return null
        val contents: PotionContents = HTPotionHelper.getPotion(resource)
        return BottledPotionContents(contents, bottleType)
    }

    override fun setContents(stack: FluidStack, contents: BottledPotionContents) {
        HTPotionHelper.setPotion(stack, contents.contents)
        val handler: HTPotionFluidManager.Handler = HTPotionFluidManager.getFluidHandler(stack.fluidHolder) ?: DEFAULT_POTION_HANDLER
        handler[stack] = contents.bottleType
    }

    override fun setContents(stack: ItemStack, contents: BottledPotionContents) {
        HTPotionHelper.setPotion(stack, contents.contents)
        DEFAULT_POTION_HANDLER[stack] = contents.bottleType
    }

    override fun <T : Any> getFirstHolder(holders: Iterable<Holder<T>>): SimpleSupplierWithKey<T> = holders.asSequence().map(Holder<T>::toLike).sortedWith(modIdComparator).first()
}
