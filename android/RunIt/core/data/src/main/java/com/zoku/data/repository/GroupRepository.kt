package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.network.model.response.GroupResponse

interface GroupRepository {

    suspend fun getGroupInfo(groupId : Int,
                             rankType : String ) : NetworkResult<GroupResponse>

}