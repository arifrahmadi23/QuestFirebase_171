package com.example.firebase.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.Repository.MahasiswaRepository
import com.example.firebase.model.Mahasiswa
import kotlinx.coroutines.launch

class InsertViewModel(
    private val mhs: MahasiswaRepository
): ViewModel(){

    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())
        private set
    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set

    fun updateState(mahasiswaEvent: MahasiswaEvent){
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent
        )
    }

    fun validateFields(): Boolean{
        val event = uiEvent.insertUiEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
            judulSkripsi = if (event.judulSkripsi.isNotEmpty()) null else "Judul Skripsi Tidak Boleh Kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            jenisKelamin = if (event.jenisKelamin.isNotEmpty()) null else "Jenis kelamin tidak boleh kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",
            kelas = if (event.kelas.isNotEmpty()) null else "Kelas tidak boleh kosong",
            angkatan = if (event.angkatan.isNotEmpty()) null else "Angkatan tidak boleh kosong",
            dosen1 = if (event.dosen1.isNotEmpty()) null else "Dosen 1 tidak boleh kosong",
            dosen2 = if (event.dosen2.isNotEmpty()) null else "Dosen 2 tidak boleh kosong",

            )
        uiEvent = uiEvent.copy(isEntryValid = errorState)
        return errorState.isValid()
    }
    fun insertMhs(){
        if (validateFields()){
            viewModelScope.launch {
                uiState = FormState.Loading
                try {
                    mhs.insertMahasiswa(uiEvent.insertUiEvent.toMhsModel())
                    uiState = FormState.Success("Data berhasil disimpan")
                }catch (e:Exception){
                    uiState = FormState.Error("Data gagal disimpan")
                }
            }
        }else {
            uiState = FormState.Error("Data tidak valid")
        }
    }
    fun resetForm(){
        uiEvent = InsertUiState()
        uiState = FormState.Idle
    }

    fun resetSnackBarMessage(){
        uiState = FormState.Idle
    }
}


sealed class FormState{
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val message: String) : FormState()
    data class Error(val message: String) : FormState()
}
data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
)

data class FormErrorState(
    val nim: String? = null,
    val judulSkripsi: String? = null,
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null,
    val dosen1: String? = null,
    val dosen2: String? = null
){
    fun isValid(): Boolean{
        return nim == null && judulSkripsi == null && nama == null && jenisKelamin == null &&
                alamat == null && kelas == null && angkatan == null && dosen1 == null && dosen2 == null
    }
}

data class MahasiswaEvent(
    val nim: String = "",
    val judulSkripsi: String = "",
    val nama: String = "",
    val jenisKelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = "",
    val dosen1: String = "",
    val dosen2: String = "",
)

//Menyimpan input form ke dalam entity
fun MahasiswaEvent.toMhsModel(): Mahasiswa = Mahasiswa(
    nim = nim,
    judulSkripsi = judulSkripsi,
    nama = nama,
    jenisKelamin = jenisKelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan,
    dosen1 = dosen1,
    dosen2 = dosen2,
)