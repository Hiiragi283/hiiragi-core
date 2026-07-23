package hiiragi283.core.api.experience

import com.google.common.primitives.Ints

sealed interface HTExpAmount {
    fun getPoint(expRatio: Int): Long

    fun getFluidAmount(expRatio: Int): Int

    @JvmInline
    value class Point(val value: Long) : HTExpAmount {
        override fun getPoint(expRatio: Int): Long = value

        override fun getFluidAmount(expRatio: Int): Int = Ints.saturatedCast(value * expRatio)
    }

    @JvmInline
    value class FluidAmount(val value: Int) : HTExpAmount {
        override fun getPoint(expRatio: Int): Long = value.toLong() / expRatio

        override fun getFluidAmount(expRatio: Int): Int = value
    }
}
