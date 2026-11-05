/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.ui

import androidx.lifecycle.ViewModel
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale

/**
 * [OrderViewModel] menyimpan pilihan komponen komputer/notebook pelanggan
 * dan menghitung perkiraan total harganya.
 */
class OrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun setJenis(nama: String) = updateState { it.copy(jenis = nama) }

    fun setProsesor(nama: String) = updateState { it.copy(prosesor = nama) }

    fun setRam(nama: String) = updateState { it.copy(ram = nama) }

    fun setTipeHardisk(nama: String) = updateState { it.copy(tipeHardisk = nama) }

    fun setUkuranHardisk(nama: String) = updateState { it.copy(ukuranHardisk = nama) }

    fun setMonitor(nama: String) = updateState { it.copy(monitor = nama) }

    fun setGpu(nama: String) = updateState { it.copy(gpu = nama) }

    /** Mengosongkan semua pilihan (dipakai tombol Cancel). */
    fun resetOrder() {
        _uiState.value = OrderUiState()
    }

    /**
     * Mengubah state lalu langsung menghitung ulang harga,
     * jadi harga selalu sinkron dengan pilihan terbaru.
     */
    private fun updateState(ubah: (OrderUiState) -> OrderUiState) {
        _uiState.update { lama ->
            val baru = ubah(lama)
            baru.copy(price = hitungHarga(baru))
        }
    }

    /**
     * Total = harga dasar (desktop/notebook) + tambahan tiap komponen yang dipilih.
     * Komponen yang belum dipilih dihitung 0.
     */
    private fun hitungHarga(state: OrderUiState): String {
        val total =
            DataSource.harga(DataSource.jenisOptions, state.jenis) +
                    DataSource.harga(DataSource.prosesorOptions, state.prosesor) +
                    DataSource.harga(DataSource.ramOptions, state.ram) +
                    DataSource.harga(DataSource.tipeHardiskOptions, state.tipeHardisk) +
                    DataSource.harga(DataSource.ukuranHardiskOptions, state.ukuranHardisk) +
                    DataSource.harga(DataSource.monitorOptions, state.monitor) +
                    DataSource.harga(DataSource.gpuOptions, state.gpu)

        return formatRupiah(total)
    }

    private fun formatRupiah(jumlah: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        format.maximumFractionDigits = 0
        return format.format(jumlah)
    }
}
