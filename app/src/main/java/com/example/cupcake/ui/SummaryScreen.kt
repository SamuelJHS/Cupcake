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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.components.FormattedPriceLabel
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * Layar ringkasan: menampilkan semua pilihan dan perkiraan total harga dari [orderUiState],
 * [onCancelButtonClicked] membatalkan pesanan, dan [onSendButtonClicked] membagikan
 * pesanan (subjek, isi) ke aplikasi lain.
 */
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Teks yang dibagikan ke aplikasi lain
    val orderSummary = stringResource(
        R.string.order_details,
        orderUiState.jenis,
        orderUiState.prosesor,
        orderUiState.ram,
        orderUiState.tipeHardisk,
        orderUiState.ukuranHardisk,
        orderUiState.monitor,
        orderUiState.gpu,
        orderUiState.price
    )
    val newOrder = stringResource(R.string.new_order)

    // Daftar baris ringkasan yang ditampilkan di layar
    val items = listOf(
        Pair(stringResource(R.string.label_jenis), orderUiState.jenis),
        Pair(stringResource(R.string.label_prosesor), orderUiState.prosesor),
        Pair(stringResource(R.string.label_ram), orderUiState.ram),
        Pair(stringResource(R.string.label_tipe_hardisk), orderUiState.tipeHardisk),
        Pair(stringResource(R.string.label_ukuran_hardisk), orderUiState.ukuranHardisk),
        Pair(stringResource(R.string.label_monitor), orderUiState.monitor),
        Pair(stringResource(R.string.label_gpu), orderUiState.gpu)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Dibuat bisa di-scroll karena barisnya cukup banyak
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items.forEach { item ->
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) }
                ) {
                    Text(stringResource(R.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderSummaryPreview() {
    CupcakeTheme {
        OrderSummaryScreen(
            orderUiState = OrderUiState(
                jenis = "Notebook",
                prosesor = "Intel Core i5",
                ram = "16 GB",
                tipeHardisk = "SSD NVMe",
                ukuranHardisk = "512 GB",
                monitor = "15.6 inci",
                gpu = "NVIDIA GTX 1650",
                price = "Rp11.300.000"
            ),
            onSendButtonClicked = { subject: String, summary: String -> },
            onCancelButtonClicked = {},
            modifier = Modifier.fillMaxHeight()
        )
    }
}