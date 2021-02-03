package fbo.costa.yesorno.repository

import fbo.costa.yesorno.data.api.ApiService
import fbo.costa.yesorno.data.model.Ans
import fbo.costa.yesorno.util.DataState
import fbo.costa.yesorno.util.NetworkMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MainRepository
@Inject
constructor(private val apiService: ApiService, private val networkMapper: NetworkMapper) {

    fun getAns(): Flow<DataState<Ans>> = flow {
        emit(DataState.Loading)
        delay(1000)

        val response = apiService.getAns()
        if (response.isSuccessful) {
            response.body()?.let { _ansApiEntity ->
                val ans = networkMapper.mapFromEntityModel(_ansApiEntity)
                emit(DataState.Success(ans))
            }
        } else {
            emit(DataState.Error("Unsuccessful: ${response.message()}"))
        }
    }
}
