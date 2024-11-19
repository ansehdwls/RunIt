package com.zoku.network.api

import com.zoku.network.model.response.GroupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupApi {

    @GET("api/group/users")
    suspend fun getUserGroup(
        @Query("groupId") groupId: Int,
        @Query("rankType") rankType: String
    ) : Response<GroupResponse>
}