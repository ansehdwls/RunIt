package com.zoku.watch.component


import com.zoku.watch.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.zoku.ui.BaseYellow


@Composable
fun StartButton(
    modifier : Modifier = Modifier,
    onClick : () -> Unit
){
    Button(
        onClick = { onClick()},
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = BaseYellow
        ),
        shape = CircleShape
    ) {
        Text(stringResource(R.string.start))

    }


}