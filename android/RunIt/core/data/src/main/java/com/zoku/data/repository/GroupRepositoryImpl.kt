package com.zoku.data.repository

import com.zoku.data.ApiHandler
import com.zoku.data.NetworkResult
import com.zoku.network.api.GroupApi
import com.zoku.network.model.response.GroupResponse
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupApi: GroupApi
) : GroupRepository, ApiHandler{
    override suspend fun getGroupInfo(groupId : Int): NetworkResult<GroupResponse> {
        return handleApi {
            groupApi.getUserGroup(groupId)
        }
    }
}