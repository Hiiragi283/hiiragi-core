package hiiragi283.core.common

import com.google.gson.JsonObject
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.serialization.value.HTEmptyValueInput
import hiiragi283.core.common.serialization.value.HTJsonValueInput
import hiiragi283.core.common.serialization.value.HTJsonValueOutput
import hiiragi283.core.common.serialization.value.HTTagValueInput
import hiiragi283.core.common.serialization.value.HTTagValueOutput
import hiiragi283.core.config.HCConfig
import hiiragi283.core.setup.HCMiscRegister
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag

class HiiragiCoreAccessImpl :
    HiiragiCoreAccess,
    HTMaterialContents {
    override lateinit var materialManager: HTMaterialManager

    override val materialContents: HTMaterialContents = this

    override fun getModIdPriorityList(): List<String> = HCConfig.COMMON.tagOutputPriority.get()

    override fun createInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput = when {
        jsonObject.isEmpty -> HTEmptyValueInput
        else -> HTJsonValueInput(provider, jsonObject)
    }

    override fun createOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput =
        HTJsonValueOutput(provider, jsonObject)

    override fun createInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput = when {
        compoundTag.isEmpty -> HTEmptyValueInput
        else -> HTTagValueInput(provider, compoundTag)
    }

    override fun createOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput =
        HTTagValueOutput(provider, compoundTag)

    //    HTMaterialContents    //

    override fun getVanillaTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>> = VanillaMaterialKeys.INGREDIENTS

    override fun getBlockTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTBlockHolderLike<*, *>> = HCMiscRegister.materialBlocks

    override fun getItemTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>> = HCMiscRegister.materialItems

    override fun getToolTable(): HTTable<HTToolType, HTMaterialKey, out HTItemHolderLike<*>> = HCMiscRegister.toolItems
}
