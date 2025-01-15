package com.example.firebase.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.Repository.MahasiswaRepository
import com.example.firebase.model.Mahasiswa
import com.example.firebase.navigasi.DestinasiDetail
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class DetailUiState{
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    object Error : DetailUiState()
    object Loading : DetailUiState()
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mhs: MahasiswaRepository
): ViewModel(){
    var mhsUiState : DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set
    private val _nim: String = checkNotNull(savedStateHandle[DestinasiDetail.NIM])

    init {
        getMhsByNim()
    }

    fun getMhsByNim(){
        viewModelScope.launch {
            mhs.getMahasiswaByNim(_nim)
                .onStart {
                    mhsUiState = DetailUiState.Loading
                }
                .catch { exception ->
                    mhsUiState = DetailUiState.Error
                }
                .collect{ mahasiswa ->
                    mhsUiState = DetailUiState.Success(mahasiswa)
                }
        }
    }
}

