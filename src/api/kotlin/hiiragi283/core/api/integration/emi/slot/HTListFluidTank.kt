package hiiragi283.core.api.integration.emi.slot

import dev.emi.emi.api.stack.EmiIngredient
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.monad.unwrap
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.fluid.HTFluidResourceFactory
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.toResource
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * EMIにおける入力スロットで複数の液体を表示させるための[HTFluidTank]の実装クラスです。
 *
 * GTCEuに感謝！
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
class HTListFluidTank private constructor(val content: Either<TagKey<Fluid>, List<HTFluidResourceType>>, private val amount: Int) :
    HTFluidTank.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
        constructor(ingredient: HTFluidIngredient) : this(ingredient.unwrap(), ingredient.getRequiredAmount())

        constructor(tagKey: TagKey<Fluid>, amount: Int) : this(Either.Left(tagKey), amount)

        constructor(resources: List<HTFluidResourceType>, amount: Int) : this(Either.Right(resources), amount)

        constructor(stacks: List<FluidStack>) : this(
            stacks.mapNotNull(FluidStack::toResource),
            stacks.maxOfOrNull(FluidStack::getAmount) ?: 1,
        )

        fun getIngredient(): EmiIngredient = content.map(
            { tagKey: TagKey<Fluid> -> tagKey.toEmi(amount) },
            { resources: List<HTFluidResourceType> -> resources.map { it.toStack(amount) }.map(FluidStack::toEmi).let(EmiIngredient::of) },
        )

        override fun setResource(resource: HTFluidResourceType?) {}

        override fun setAmount(amount: Int) {}

        override fun getAmount(): Int = this.amount

        override fun getResource(): HTFluidResourceType? {
            val resources: List<HTFluidResourceType> = content
                .mapLeft { tagKey: TagKey<Fluid> ->
                    BuiltInRegistries.FLUID.getTagOrEmpty(tagKey).mapNotNull(HTFluidResourceFactory::fromHolder)
                }.unwrap()
            if (resources.isEmpty()) return null
            val index: Int = ((System.currentTimeMillis() / 1000) % resources.size).toInt()
            return resources[index]
        }

        override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

        override fun isValid(resource: HTFluidResourceType): Boolean = true
    }
