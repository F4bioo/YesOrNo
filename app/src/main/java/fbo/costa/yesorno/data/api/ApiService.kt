package fbo.costa.yesorno.data.api

import fbo.costa.yesorno.data.model.AnsApiEntity
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api")
    suspend fun getAns(
    ): Response<AnsApiEntity>
}
