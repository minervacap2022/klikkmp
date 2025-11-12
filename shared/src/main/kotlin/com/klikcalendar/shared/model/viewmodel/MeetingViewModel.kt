package com.klikcalendar.shared.model.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klikcalendar.shared.model.CalendarEvent
import com.klikcalendar.shared.model.data.MeetingInfo
import com.klikcalendar.shared.model.service.MeetingApiServiceImp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MeetingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MeetingUiState>(MeetingUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _navigateToMeeting = MutableSharedFlow<CalendarEvent>()
    val navigateToMeeting = _navigateToMeeting.asSharedFlow()

    private val repository = MeetingApiServiceImp()

    fun refresh(){
        loadMeetingInfo() // 重新调用
    }

    private fun loadMeetingInfo() {

        if (_uiState.value is MeetingUiState.Loading) return

        viewModelScope.launch {
            _uiState.value = MeetingUiState.Loading
            repository.fetchMeetingInfo()
                .onSuccess { meeting ->
                    _uiState.value = MeetingUiState.Success(meeting)
                    _navigateToMeeting.emit(meeting)
                }
                .onFailure { error ->
                    _uiState.value = MeetingUiState.Error(error.message ?: "未知错误")
                }
        }
    }
}

sealed interface MeetingUiState {
    object Idle : MeetingUiState          // 初始状态（无操作）
    object Loading : MeetingUiState
    data class Success(val meeting: CalendarEvent) : MeetingUiState
    data class Error(val message: String) : MeetingUiState
}