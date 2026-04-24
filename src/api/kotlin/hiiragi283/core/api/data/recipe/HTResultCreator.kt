package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.util.toIorOrThrow
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction
import java.util.function.IntUnaryOperator

/**
 * [HTItemResult]と[HTFluidResult]に関するヘルパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTResultCreator(provider: HolderLookup.Provider) {
    private val itemGetter: HolderGetter<Item> = provider.lookupOrThrow(Registries.ITEM)

    //    Item    //

    fun create(item: ItemLike, amount: Int = 1, chance: Fraction = Fraction.ONE): HTItemResult = HTItemResult.create(item, amount, chance)

    fun create(stack: ItemStack, chance: Fraction = Fraction.ONE): HTItemResult = HTItemResult.create(stack, chance)

    /**
     * 指定した[プレフィックス][prefix]と[素材][material]から[HTItemResult]の新しいインスタンスを作成します。
     * @since 0.12.0
     */
    fun material(
        prefix: HTTagPrefix,
        material: HTMaterialLike,
        amount: Int = 1,
        chance: Fraction = Fraction.ONE,
    ): HTItemResult = HTItemResult.create(itemGetter.getOrThrow(prefix.itemTagKey(material)), amount, chance)

    /**
     * 指定した[部品][part]と[素材][material]から[HTItemResult]の新しいインスタンスを作成します。
     * @since 0.12.0
     */
    fun material(
        part: HTPartLike,
        material: HTMaterialLike,
        amount: Int = 1,
        chance: Fraction = Fraction.ONE,
    ): HTItemResult {
        val item: HTItemResourceType? = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, material).toResource()
        val holderSet: HolderSet<Item>? = part.tagPrefix?.itemTagKey(material)?.let(itemGetter::getOrThrow)
        return HTItemResult((item to holderSet).toIorOrThrow(), amount, chance)
    }

    //    Fluid    //

    fun create(fluid: Fluid, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(FluidStack(fluid, amount))

    fun create(fluid: HTFluidHolderLike<*>, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult =
        HTFluidResult.create(checkNotNull(fluid.toResource()) { "Cannot create fluid result from empty stack" }, amount)

    fun create(stack: FluidStack): HTFluidResult = HTFluidResult.create(stack)

    fun water(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.WATER, amount)

    fun lava(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.LAVA, amount)

    fun milk(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = create(VanillaFluidContents.MILK, amount)

    fun molten(material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidResult =
        material(HTFluidPart.MOLTEN, material, operator)

    fun material(part: HTFluidPart, material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidResult {
        val fluid: Fluid = HiiragiCoreAccess.INSTANCE.registeredFluids
            .getOrThrow(part, material)
            .get()
        val getter: HTPropertyGetter = HTMaterialManager.getInstance().getOrEmpty(material)
        return create(fluid, operator.applyAsInt(getter.getDefaultFluidAmount()))
    }
}
