package hiiragi283.core.api.data.recipe.result

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.result.HTResourceRecipeResult
import hiiragi283.core.api.storage.resource.HTResourceFactory
import hiiragi283.core.api.storage.resource.HTResourceType
import net.minecraft.tags.TagKey

/**
 * [HTResourceRecipeResult]を作成するインターフェースです。
 * @param TYPE 種類のクラス
 * @param RESOURCE [HTResourceType.Registered]を継承したクラス
 * @param STACK [HTResourceRecipeResult]の完成品のクラス
 * @param RESULT [HTResourceRecipeResult]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
abstract class HTResultCreator<
    TYPE : Any,
    RESOURCE : HTResourceType.Registered<TYPE>,
    STACK : Any,
    RESULT : HTResourceRecipeResult<TYPE, RESOURCE, STACK>,
> {
    /**
     * @since 0.7.0
     */
    protected abstract fun resourceFactory(): HTResourceFactory<TYPE, STACK, RESOURCE>

    /**
     * デフォルトの数量を取得します。
     */
    protected fun defaultAmount(): Int = resourceFactory().getDefaultAmount()

    /**
     * 指定した[contents]と[amount]から[RESULT]を作成します。
     */
    protected abstract fun create(contents: Ior<RESOURCE, TagKey<TYPE>>, amount: Int): RESULT

    // Type
    fun create(type: TYPE, amount: Int = defaultAmount()): RESULT = create(resourceFactory().fromTypeOrThrow(type), amount)

    // Resource
    fun create(resource: RESOURCE, amount: Int = defaultAmount()): RESULT = create(Ior.Left(resource), amount)

    // TagKey
    fun create(tagKey: TagKey<TYPE>, amount: Int = defaultAmount()): RESULT = create(Ior.Right(tagKey), amount)

    // Both
    fun create(type: TYPE, tagKey: TagKey<TYPE>, amount: Int = defaultAmount()): RESULT =
        create(resourceFactory().fromTypeOrThrow(type), tagKey, amount)

    fun create(resource: RESOURCE, tagKey: TagKey<TYPE>, amount: Int = defaultAmount()): RESULT = create(Ior.Both(resource, tagKey), amount)
}
