package com.zoku.ui.model

import com.zoku.ui.R

data class ExpInfoData(
    val image : Int,
    val text : String
){
    companion object{
        val DEFAULT = listOf(
            ExpInfoData(
                image = R.drawable.ic_exp_attend,
                text = "출석점수\n+10xp"
            ),
            ExpInfoData(
                image = R.drawable.ic_exp_run,
                text = "100m 뛸 때마다\n+1xp"
            )
        )
    }
}
