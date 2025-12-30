package hiiragi283.core.api.data.recipe.result

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.result.HTResourceRecipeResult
import hiiragi283.core.api.storage.resource.HTResourceType
import net.minecraft.tags.TagKey

/**
 * [HTResourceRecipeResult]を作成するインターフェースです。
 * @param TYPE 種類のクラス
 * @param RESOURCE [HTResourceType]を継承したクラス
 * @param STACK [HTResourceRecipeResult]の完成品のクラス
 * @param RESULT [HTResourceRecipeResult]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
abstract class HTResultCreator<
    TYPE : Any,
    RESOURCE : HTResourceType<TYPE>,
    STACK : Any,
    RESULT : HTResourceRecipeResult<TYPE, RESOURCE, STACK>,
> {
    /**
     * デフォルトの数量を取得します。
     */
    protected abstract fun defaultAmount(): Int

    /**
     * 指定した[type]から[RESOURCE]を作成します。
     */
    protected abstract fun createResource(type: TYPE): RESOURCE

    /**
     * 指定した[contents]と[amount]から[RESULT]を作成します。
     */
    protected abstract fun create(contents: Ior<RESOURCE, TagKey<TYPE>>, amount: Int): RESULT

    // Type
    fun create(type: TYPE, amount: Int = defaultAmount()): RESULT = create(createResource(type), amount)

    // Resource
    fun create(resource: RESOURCE, amount: Int = defaultAmount()): RESULT = create(Ior.Left(resource), amount)

    // TagKey
    fun create(tagKey: TagKey<TYPE>, amount: Int = defaultAmount()): RESULT = create(Ior.Right(tagKey), amount)

    // Both
    fun create(type: TYPE, tagKey: TagKey<TYPE>, amount: Int = defaultAmount()): RESULT = create(createResource(type), tagKey, amount)

    fun create(resource: RESOURCE, tagKey: TagKey<TYPE>, amount: Int = defaultAmount()): RESULT = create(Ior.Both(resource, tagKey), amount)
}
