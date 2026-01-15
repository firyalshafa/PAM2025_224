package com.example.peyewaan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.peyewaan.uicontroller.view.auth.HalamanLogin
import com.example.peyewaan.uicontroller.view.auth.HalamanRegister
import com.example.peyewaan.uicontroller.view.dashboard.HalamanHome
import com.example.peyewaan.uicontroller.view.detail.HalamanDetail
import com.example.peyewaan.uicontroller.view.riwayat.HalamanEditOrder
import com.example.peyewaan.uicontroller.view.riwayat.HalamanRiwayat
import com.example.peyewaan.uicontroller.view.splash.HalamanSplash
import com.example.peyewaan.uicontroller.viewmodel.AppViewModelProvider
import com.example.peyewaan.uicontroller.viewmodel.OrderViewModel

@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {

        // 1) Splash
        composable("splash") {
            HalamanSplash(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 2) Login
        composable("login") {
            HalamanLogin(
                onRegisterClick = { navController.navigate("register") },
                onLoginSuccess = { idUser ->
                    navController.navigate("home/$idUser") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        // 3) Register
        composable("register") {
            HalamanRegister(
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        // 4) Home
        composable(
            route = "home/{idUser}",
            arguments = listOf(navArgument("idUser") { type = NavType.IntType })
        ) { backStackEntry ->
            val idUser = backStackEntry.arguments?.getInt("idUser") ?: 0

            HalamanHome(
                onDetailClick = { idAlat ->
                    navController.navigate("detail/$idAlat/$idUser")
                },
                onHistoryClick = {
                    navController.navigate("riwayat/$idUser")
                },
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        // 5) Detail
        composable(
            route = "detail/{idAlat}/{idUser}",
            arguments = listOf(
                navArgument("idAlat") { type = NavType.IntType },
                navArgument("idUser") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val idAlat = backStackEntry.arguments?.getInt("idAlat") ?: 0
            val idUser = backStackEntry.arguments?.getInt("idUser") ?: 0

            HalamanDetail(
                idAlat = idAlat,
                idUser = idUser,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRiwayat = { navController.navigate("riwayat/$idUser") }
            )
        }

        // 6) Riwayat (✅ ada Dashboard & Logout)
        composable(
            route = "riwayat/{idUser}",
            arguments = listOf(navArgument("idUser") { type = NavType.IntType })
        ) { backStackEntry ->
            val idUser = backStackEntry.arguments?.getInt("idUser") ?: 0

            val orderViewModel: OrderViewModel =
                viewModel(factory = AppViewModelProvider.Factory)

            HalamanRiwayat(
                idUser = idUser,
                onBackClick = { navController.popBackStack() },

                onEditClick = { idPesanan ->
                    navController.navigate("editOrder/$idPesanan/$idUser")
                },

                // ✅ balik ke dashboard
                onDashboardClick = {
                    navController.navigate("home/$idUser") {
                        launchSingleTop = true
                    }
                },

                // ✅ logout -> balik login dan bersihkan stack
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },

                viewModel = orderViewModel
            )
        }

        // 7) Edit Order (aku buat bawa idUser biar bisa balik ke riwayat yg benar)
        composable(
            route = "editOrder/{idOrder}/{idUser}",
            arguments = listOf(
                navArgument("idOrder") { type = NavType.IntType },
                navArgument("idUser") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val idOrder = backStackEntry.arguments?.getInt("idOrder") ?: 0
            val idUser = backStackEntry.arguments?.getInt("idUser") ?: 0

            val orderViewModel: OrderViewModel =
                viewModel(factory = AppViewModelProvider.Factory)

            HalamanEditOrder(
                idOrder = idOrder,
                onNavigateBack = { navController.popBackStack() },
                viewModel = orderViewModel
            )
        }
    }
}
