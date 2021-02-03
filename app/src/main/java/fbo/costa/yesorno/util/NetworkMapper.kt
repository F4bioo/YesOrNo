package fbo.costa.yesorno.util

import fbo.costa.yesorno.data.model.Ans
import fbo.costa.yesorno.data.model.AnsApiEntity
import javax.inject.Inject

class NetworkMapper
@Inject
constructor(
) : EntityMapper<AnsApiEntity, Ans> {

    override fun mapFromEntityModel(entityModel: AnsApiEntity): Ans {
        return Ans(
            answer = entityModel.answer,
            forced = entityModel.forced,
            image = entityModel.image
        )
    }
}
