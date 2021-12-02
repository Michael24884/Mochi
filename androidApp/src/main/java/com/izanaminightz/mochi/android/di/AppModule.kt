package com.izanaminightz.mochi.android.di

import com.izanaminightz.mochi.android.ui.viewModel.MangadexDetailViewModel
import com.izanaminightz.mochi.android.ui.viewModel.MangadexHomeViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel


val appModule = module {
    viewModel {
        MangadexDetailViewModel(get())
    }

}

val appModule2 = module {
    viewModel {
        MangadexHomeViewModel(get())
    }

}