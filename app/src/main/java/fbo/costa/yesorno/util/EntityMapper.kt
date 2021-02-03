package fbo.costa.yesorno.util

interface EntityMapper<EntityModel, DomainModel> {
    fun mapFromEntityModel(entityModel: EntityModel): DomainModel
}
