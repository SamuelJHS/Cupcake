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
 * Satu pilihan komponen.
 * [nama] ditampilkan ke pengguna, [harga] adalah tambahan harga dalam Rupiah.
 */
data class Opsi(val nama: String, val harga: Int)

/**
 * Daftar semua pilihan beserta harganya (asumsi harga toko, silakan disesuaikan).
 */
object DataSource {

    /** Harga dasar perangkat (belum termasuk komponen pilihan). */
    val jenisOptions = listOf(
        Opsi("Desktop", 3_000_000),
        Opsi("Notebook", 4_500_000)
    )

    val prosesorOptions = listOf(
        Opsi("Intel Core i3", 0),
        Opsi("AMD Ryzen 5", 1_400_000),
        Opsi("Intel Core i5", 1_500_000),
        Opsi("Intel Core i7", 3_500_000)
    )

    val ramOptions = listOf(
        Opsi("8 GB", 0),
        Opsi("16 GB", 600_000),
        Opsi("32 GB", 1_500_000)
    )

    val tipeHardiskOptions = listOf(
        Opsi("HDD", 0),
        Opsi("SSD SATA", 300_000),
        Opsi("SSD NVMe", 500_000)
    )

    val ukuranHardiskOptions = listOf(
        Opsi("256 GB", 0),
        Opsi("512 GB", 400_000),
        Opsi("1 TB", 900_000)
    )

    val monitorOptions = listOf(
        Opsi("14 inci", 0),
        Opsi("15.6 inci", 300_000),
        Opsi("17 inci", 700_000)
    )

    val gpuOptions = listOf(
        Opsi("Onboard (bawaan prosesor)", 0),
        Opsi("NVIDIA GTX 1650", 2_500_000),
        Opsi("NVIDIA RTX 3050", 4_000_000)
    )

    /**
     * Mencari harga opsi berdasarkan [nama] di dalam [daftar].
     * Mengembalikan 0 jika belum ada yang dipilih / nama tidak ditemukan.
     */
    fun harga(daftar: List<Opsi>, nama: String): Int =
        daftar.firstOrNull { it.nama == nama }?.harga ?: 0
}
