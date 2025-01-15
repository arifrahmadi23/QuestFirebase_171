package com.example.firebase.Repository

import com.example.firebase.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface MahasiswaRepository {
    suspend fun getAllMahasiswa(): Flow<List<Mahasiswa>>
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)
    suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa)
    suspend fun deleteMahasiswa (mahasiswa: Mahasiswa)
    suspend fun getMahasiswaByNim(nim: String) :Flow<Mahasiswa>
}