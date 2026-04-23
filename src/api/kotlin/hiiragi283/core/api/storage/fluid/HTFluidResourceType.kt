package hiiragi283.core.api.storage.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.fluid.createFluidStack
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.text.Text
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [液体][Fluid]向けの[DataComponent]の実装クラスです。
 * @param stack 内部で保持しているスタック
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
class HTFluidResourceType private constructor(private val stack: FluidStack) : HTResourceType.DataComponent<Fluid> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResourceType> =
            FluidStack.fixedAmountCodec(HTConst.DEFAULT_FLUID_AMOUNT).xmap(::HTFluidResourceType, HTFluidResourceType::stack)

        @JvmField
        val MAP_CODEC: MapCodec<HTFluidResourceType> = MapCodec.assumeMapUnsafe(CODEC)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResourceType> =
            FluidStack.STREAM_CODEC.map(::HTFluidResourceType, HTFluidResourceType::stack)

        /**
         * 指定した[stack]を[HTFluidResourceType]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は`null`
         */
        @JvmStatic
        fun of(stack: FluidStack): HTFluidResourceType? = when {
            stack.isEmpty -> null
            else -> HTFluidResourceType(stack.copyWithAmount(1))
        }
    }

    /**
     * 保持している[液体][type]の[FluidType]を返します。
     */
    fun fluidType(): FluidType = stack.fluidType

    // FluidStack

    /**
     * @since 0.13.0
     */
    fun isOf(stack: FluidStack): Boolean = stack.`is`(this.typeHolder())

    fun toStack(amount: Int): FluidStack = stack.copyWithAmount(amount)

    override fun equals(other: Any?): Boolean = when (other) {
        is HTFluidResourceType -> FluidStack.isSameFluidSameComponents(this.stack, other.stack)
        else -> false
    }

    override fun hashCode(): Int = FluidStack.hashFluidAndComponents(stack)

    override fun toString(): String = stack.toString()

    operator fun component1(): Holder<Fluid> = typeHolder()

    operator fun component2(): DataComponentPatch = componentsPatch()

    //    HTResourceType    //

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getText(): Text = stack.hoverName

    override fun typeHolder(): Holder<Fluid> = stack.fluidHolder

    override fun getComponents(): DataComponentMap = stack.components
}

//    Extensions    //

/**
 * この[Fluid][this]を[HTFluidResourceType]に変換します。
 * @param patch コンポーネントの差分
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun Fluid?.toResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResourceType? =
    createFluidStack(this, patch = patch).toResource()

/**
 * この[FluidStack][this]を[HTFluidResourceType]に変換します。
 * @return [isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun FluidStack.toResource(): HTFluidResourceType? = HTFluidResourceType.of(this)

/**
 * この[FluidStack][this]を[HTFluidResourceType]と数量に展開します。
 * @return [isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun FluidStack.toResourcePair(): Pair<HTFluidResourceType, Int>? {
    val resource: HTFluidResourceType = this.toResource() ?: return null
    return resource to this.amount
}

/**
 * この[HTFluidResourceType][this]を[FluidStack]に変換します
 * @return この[HTFluidResourceType]が`null`の場合は[FluidStack.EMPTY]
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun HTFluidResourceType?.toStackOrEmpty(amount: Int): FluidStack = this?.toStack(amount) ?: FluidStack.EMPTY
