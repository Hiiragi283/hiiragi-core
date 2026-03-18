package hiiragi283.core.data.server

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tank.HTTankInteractionProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.common.data.tank.HTPotionTankInteraction
import hiiragi283.core.common.data.tank.HTSimpleTankInteraction
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.item.Items

class HCTankInteractionProvider(context: HTDataGenContext) : HTTankInteractionProvider(context, HiiragiCoreAPI.MOD_ID) {
    override fun gather() {
        val glassBottle: HTSimpleItemHolderLike = Items.GLASS_BOTTLE.toLike()
        // Experience
        tankInteraction(glassBottle, Items.EXPERIENCE_BOTTLE.toLike(), HCFluids.EXPERIENCE)
        // Honey Bottle
        tankInteraction(glassBottle, Items.HONEY_BOTTLE.toLike(), HCFluids.HONEY)
        // Mushroom Stew
        tankInteraction(Items.BOWL.toLike(), Items.MUSHROOM_STEW.toLike(), HCFluids.MUSHROOM_STEW)
        // Dragon Breath
        tankInteraction(glassBottle, Items.DRAGON_BREATH.toLike(), HCFluids.DRAGON_BREATH)
        // Potion Bottle
        unconditional(HTConst.MINECRAFT.toId("potion"), HTPotionTankInteraction)

        // Sponge
        tankInteraction(Items.SPONGE.toLike(), Items.WET_SPONGE.toLike(), VanillaFluidContents.WATER, HTConst.DEFAULT_FLUID_AMOUNT)
    }

    private fun tankInteraction(
        empty: HTSimpleItemHolderLike,
        filled: HTSimpleItemHolderLike,
        content: HTFluidContent,
        amount: Int = 250,
    ) {
        unconditional(
            filled.getId(),
            HTSimpleTankInteraction(
                empty,
                filled,
                content,
                amount,
                content.fluidTag.wrapOptional(),
            ),
        )
    }
}
