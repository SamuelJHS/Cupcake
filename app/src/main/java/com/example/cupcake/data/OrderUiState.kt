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
package com.example.cupcake.data

/**
 * State pesanan komputer/notebook yang sedang dirakit pelanggan.
 * Semua field berisi NAMA opsi yang dipilih (harganya dicari di [DataSource]).
 */
data class OrderUiState(
    /** Jenis perangkat: "Desktop" atau "Notebook" */
    val jenis: String = "",
    /** Contoh: "Intel Core i5" */
    val prosesor: String = "",
    /** Contoh: "16 GB" */
    val ram: String = "",
    /** Contoh: "SSD NVMe" */
    val tipeHardisk: String = "",
    /** Contoh: "512 GB" */
    val ukuranHardisk: String = "",
    /** Contoh: "15.6 inci" */
    val monitor: String = "",
    /** Contoh: "NVIDIA GTX 1650" */
    val gpu: String = "",
    /** Perkiraan total harga, sudah terformat Rupiah, mis. "Rp7.500.000" */
    val price: String = ""
)
