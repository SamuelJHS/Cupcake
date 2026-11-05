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
package com.example.cupcake

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.Opsi
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen

/** Membagikan ringkasan pesanan ke aplikasi lain (WhatsApp, email, dll.). */
private fun shareOrder(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_order)
        )
    )
}

/**
 * Daftar layar (rute) di aplikasi beserta judulnya di top bar.
 * Urutan alur: Start -> Prosesor -> Ram -> TipeHardisk -> UkuranHardisk -> Monitor -> Gpu -> Summary
 */
enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Prosesor(title = R.string.pilih_prosesor),
    Ram(title = R.string.pilih_ram),
    TipeHardisk(title = R.string.pilih_tipe_hardisk),
    UkuranHardisk(title = R.string.pilih_ukuran_hardisk),
    Monitor(title = R.string.pilih_monitor),
    Gpu(title = R.string.pilih_gpu),
    Summary(title = R.string.order_summary)
}

private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
}

/**
 * Composable yang menampilkan top bar dan tombol kembali jika bisa navigasi mundur.
 */
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

/**
 * Membuat satu layar pilihan komponen (dipakai berulang untuk prosesor, RAM, dst.)
 * supaya kode NavHost tidak panjang dan berulang.
 */
private fun NavGraphBuilder.layarPilihan(
    screen: CupcakeScreen,
    nextScreen: CupcakeScreen,
    options: List<Opsi>,
    onSelect: (String) -> Unit,
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    composable(route = screen.name) {
        val uiState by viewModel.uiState.collectAsState()
        SelectOptionScreen(
            subtotal = uiState.price,
            options = options,
            onSelectionChanged = onSelect,
            onNextButtonClicked = { navController.navigate(nextScreen.name) },
            onCancelButtonClicked = {
                cancelOrderAndNavigateToStart(viewModel, navController)
            },
            modifier = Modifier.fillMaxHeight()
        )
    }
}

/**
 * Composable utama aplikasi: mengatur navigasi antar layar.
 */
@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CupcakeScreen.valueOf(
        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
    )

    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Pilih jenis perangkat: Desktop / Notebook
            composable(route = CupcakeScreen.Start.name) {
                StartOrderScreen(
                    jenisOptions = DataSource.jenisOptions,
                    onNextButtonClicked = {
                        viewModel.setJenis(it)
                        navController.navigate(CupcakeScreen.Prosesor.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            // 2-7. Layar pilihan komponen
            layarPilihan(
                screen = CupcakeScreen.Prosesor,
                nextScreen = CupcakeScreen.Ram,
                options = DataSource.prosesorOptions,
                onSelect = { viewModel.setProsesor(it) },
                viewModel = viewModel,
                navController = navController
            )
            layarPilihan(
                screen = CupcakeScreen.Ram,
                nextScreen = CupcakeScreen.TipeHardisk,
                options = DataSource.ramOptions,
                onSelect = { viewModel.setRam(it) },
                viewModel = viewModel,
                navController = navController
            )
            layarPilihan(
                screen = CupcakeScreen.TipeHardisk,
                nextScreen = CupcakeScreen.UkuranHardisk,
                options = DataSource.tipeHardiskOptions,
                onSelect = { viewModel.setTipeHardisk(it) },
                viewModel = viewModel,
                navController = navController
            )
            layarPilihan(
                screen = CupcakeScreen.UkuranHardisk,
                nextScreen = CupcakeScreen.Monitor,
                options = DataSource.ukuranHardiskOptions,
                onSelect = { viewModel.setUkuranHardisk(it) },
                viewModel = viewModel,
                navController = navController
            )
            layarPilihan(
                screen = CupcakeScreen.Monitor,
                nextScreen = CupcakeScreen.Gpu,
                options = DataSource.monitorOptions,
                onSelect = { viewModel.setMonitor(it) },
                viewModel = viewModel,
                navController = navController
            )
            layarPilihan(
                screen = CupcakeScreen.Gpu,
                nextScreen = CupcakeScreen.Summary,
                options = DataSource.gpuOptions,
                onSelect = { viewModel.setGpu(it) },
                viewModel = viewModel,
                navController = navController
            )

            // 8. Ringkasan + perkiraan harga
            composable(route = CupcakeScreen.Summary.name) {
                val context = androidx.compose.ui.platform.LocalContext.current
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}