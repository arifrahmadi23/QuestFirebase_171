package com.example.firebase.navigasi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.view.DetailScreen
import com.example.firebase.ui.view.HomeScreen
import com.example.firebase.ui.view.InsertMahasiswaView

@Composable
fun PengelolaHalaman(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier
    ){
        composable(DestinasiHome.route){
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigate(DestinasiInsert.route)
                },
                onDetailClick = { nim ->
                    navController.navigate("${DestinasiDetail.route}/$nim") // Menambahkan path dengan argument nim
                }
            )
        }
        composable(DestinasiInsert.route){
            InsertMahasiswaView(
                onBack = { navController.popBackStack()},
                onNavigate = {
                    navController.navigate(DestinasiHome.route)
                }
            )
        }
        composable(DestinasiDetail.routeWithArg) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString(DestinasiDetail.NIM) ?: ""
            DetailScreen(
                onBack = { navController.popBackStack() },
//                onEditClick = {
//                    navController.navigate("${DestinasiUpdate.route}/$nim")
//                }
            )
        }

    }
}