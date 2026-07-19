package hiiragi283.core.client.model

import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.PartDefinition
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * 参照 : [Mekanism - ModelPartData](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/model/ModelPartData.java)
 */
@OnlyIn(Dist.CLIENT)
@JvmRecord
data class HTModelPartBuilder(val name: String, val cubes: () -> CubeListBuilder, val pose: PartPose) {
    constructor(name: String, cubes: () -> CubeListBuilder) : this(name, cubes, PartPose.ZERO)

    fun addToDefinition(part: PartDefinition) {
        part.addOrReplaceChild(name, cubes(), pose)
    }

    fun getChild(part: ModelPart): ModelPart = part.getChild(name)
}
