package com.changrojasalex1168165.simonsays.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.changrojasalex1168165.simonsays.database.GameViewModel
import com.changrojasalex1168165.simonsays.views.MainGameComponent
import com.changrojasalex1168165.simonsays.views.MainMenuComponent
import com.changrojasalex1168165.simonsays.views.ScoreBoardComponent
import com.changrojasalex1168165.simonsays.views.SettingsComponent

@Composable
fun NavManager(viewModel: GameViewModel, navController: NavHostController) {


    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MainMenuComponent(navController, viewModel)
        }

        composable("game/{option}/{name}",
            arguments = listOf(navArgument("option"){type = NavType.IntType},
                navArgument("name"){type = NavType.StringType}
            )
        ) { backStackEntry ->
            MainGameComponent(navController,viewModel, backStackEntry.arguments?.getInt("option"), backStackEntry.arguments?.getString("name"))
        }

        composable("scoreboards") {
            ScoreBoardComponent(navController, viewModel)
        }

        composable("options") {
            SettingsComponent()
        }
    }

}