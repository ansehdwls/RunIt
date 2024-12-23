package com.zoku.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.repository.AttendanceRepository
import com.zoku.data.repository.DataStoreRepository
import com.zoku.data.repository.ExpRepository
import com.zoku.data.repository.GroupRepository
import com.zoku.network.model.response.AttendanceDay
import com.zoku.network.model.response.ExpDataHistory
import com.zoku.network.model.response.GroupList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankViewModel @Inject constructor(
    private val expRepository : ExpRepository,
    private val groupRepository: GroupRepository,
    private val attendanceRepository: AttendanceRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel()
{

    private val _currentExp = MutableStateFlow(0)
    val currentExp: StateFlow<Int> = _currentExp

    private val _allExpHistoryDataList = MutableStateFlow<List<ExpDataHistory>>(emptyList())
    val allExpHistoryDataList : StateFlow<List<ExpDataHistory>> = _allExpHistoryDataList

    private val _groupInfoList = MutableStateFlow<GroupList>(GroupList(emptyList(), 1, 1))
    val groupInfo : StateFlow<GroupList> = _groupInfoList

    private val _attendanceWeekInfo = MutableStateFlow<List<AttendanceDay>>(emptyList())
    val attendanceWeekInfo : StateFlow<List<AttendanceDay>> = _attendanceWeekInfo

    private val _userName = MutableStateFlow<String>("")
    val userName : StateFlow<String> = _userName

    init {
        viewModelScope.launch {
            with(dataStoreRepository){
                userName.collect{
                    _userName.value = it
                }
            }
        }
        getAllExpHistory()
        getWeekExp()
        getAttendance()
    }

    fun getAllExpHistory(){
        viewModelScope.launch {
            when(val result = expRepository.getAllExpHistory()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _allExpHistoryDataList.value = result.data.data


                }

                is NetworkResult.Error -> {
                    Log.d("확인", "실패, 에러 ${result}")

                }

                is NetworkResult.Exception -> {
                    Log.d("확인", "서버 연결 에러")
                }
            }
        }
    }

    fun getWeekExp() {
        viewModelScope.launch {
            when(val result = expRepository.getWeekExpHistory()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _currentExp.value = result.data.data.toInt()
                }

                is NetworkResult.Error -> {
                    Log.d("확인", "실패, 에러 ${result}")

                }

                is NetworkResult.Exception -> {
                    Log.d("확인", "서버 연결 에러")
                }
            }
        }
    }

    fun getGroupList(rankType : String = ""){
        viewModelScope.launch {

            dataStoreRepository.groupId.collect{ groupId ->
                if(groupId != 0){
                    when(val result = groupRepository.getGroupInfo(groupId,rankType)){
                        is NetworkResult.Success -> {
                            Log.d("확인", " 성공 ${result}")
                            _groupInfoList.value = result.data.data
                        }

                        is NetworkResult.Error -> {
                            Log.d("확인", "실패, 에러 ${result}")

                        }

                        is NetworkResult.Exception -> {
                            Log.d("확인", "서버 연결 에러")
                        }
                    }
                }
            }

        }
    }
    fun getAttendance(){
        viewModelScope.launch {
            when(val result = attendanceRepository.getAttendanceWeek()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _attendanceWeekInfo.value = result.data.data
                }

                is NetworkResult.Error -> {
                    Log.d("확인", "실패, 에러 ${result}")

                }

                is NetworkResult.Exception -> {
                    Log.d("확인", "서버 연결 에러")
                }
            }
        }
    }
}