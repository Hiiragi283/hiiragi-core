package hiiragi283.core.api.data.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStackTemplate

/**
 * [HTItemResult]と[HTFluidResult]に関するヘルパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
object HTResultCreator {
    //    Item    //

    @JvmStatic
    fun create(item: ItemLike, count: Int = 1): HTItemResult = HTItemResult.create {
        this.item = item.asItem()
        this.count = count
    }

    @JvmStatic
    fun create(template: ItemStackTemplate): HTItemResult = HTItemResult.create {
        item = template.typeHolder().value()
        patch = template.components()
        count = template.count()
    }

    //    Fluid    //

    @JvmStatic
    fun create(fluid: Fluid, amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): HTFluidResult = HTFluidResult(FluidStackTemplate(fluid, amount))
}
