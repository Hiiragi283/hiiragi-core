package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes

class HCDamageTypeTagsProvider(context: HTDataGenContext) :
    HTTagsProvider.DataGen<DamageType>(HiiragiCoreAPI.MOD_ID, Registries.DAMAGE_TYPE, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<DamageType>) {
        factory
            .apply(HiiragiCoreTags.DamageTypes.IS_SONIC)
            .add(DamageTypes.SONIC_BOOM)
    }
}
