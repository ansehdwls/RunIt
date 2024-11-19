package com.zoku.network.model.response

data class TestSumResponse(
    val data: TestSumData,
    val message: String
)

data class TestSumData(
    val x: Int,
    val y: Int,
)

data class TestSumErrorResponse(val errorCode: String, val message: String)