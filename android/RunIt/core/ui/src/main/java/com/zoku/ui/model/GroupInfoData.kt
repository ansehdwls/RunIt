package com.zoku.ui.model

import com.zoku.ui.R

data class GroupInfoData(
    val image: Int,
    val name: String
) {
    companion object {
        val DEFAULT = listOf(
            GroupInfoData(
                R.drawable.ic_egg,
                "알"
            ),
            GroupInfoData(
                R.drawable.ic_sloth,
                "나무늘보"
            ),
            GroupInfoData(
                R.drawable.ic_turtle,
                "거북이"
            ),
            GroupInfoData(
                R.drawable.ic_rabbit,
                "토끼"
            ),
            GroupInfoData(
                R.drawable.ic_horse,
                "말"
            ),
            GroupInfoData(
                R.drawable.ic_cheetah,
                "치타"
            )
        )
    }
}
