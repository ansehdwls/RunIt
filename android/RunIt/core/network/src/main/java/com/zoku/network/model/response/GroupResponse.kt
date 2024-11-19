package com.zoku.network.model.response

data class GroupResponse(
    val data : GroupList,
    val message: String
)

data class GroupList(
    val userInfos : List<GroupMember>,
    val rank : Int,
    val leagueRank : Int
)

data class GroupMember(
    val userName: String,
    val imageUrl: String,
    val score: String,
    val rankDiff : Int
)