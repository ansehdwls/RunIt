package com.zoku.ui.theme

import com.zoku.ui.R


data class League(
    val id: Int,
    val name: String,
    val imageUrl: Int,
    val color : Long
){

}

val leagueList = listOf(
    League(id = 1, name = "알", imageUrl = R.drawable.egg_icon, color = 0xFF707070),
    League(id = 2, name = "나무늘보", imageUrl = R.drawable.sloth_icon, color = 0xFF9C774E),
    League(id = 3, name = "거북이", imageUrl = R.drawable.tuttle_icon, color = 0xFF67D663),
    League(id = 4, name = "토끼", imageUrl = R.drawable.rabbit_icon, color = 0xFFEBA2A2),
    League(id = 5, name = "말", imageUrl = R.drawable.horse_icon, color = 0xFFA51906),
    League(id = 6, name = "치타", imageUrl = R.drawable.cheetah_icon, color = 0xFFFF7700)
)