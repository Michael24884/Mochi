package com.izanaminightz.mochi.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.izanaminightz.mochi.android.model.MangaChapters
import com.izanaminightz.mochi.android.ui.theme.MochiTheme
import com.izanaminightz.mochi.android.ui.view.*
import com.izanaminightz.mochi.android.ui.view.Welcome.LoggedInPage
import com.izanaminightz.mochi.android.ui.view.Welcome.WelcomePage1
import com.izanaminightz.mochi.android.ui.view.Welcome.WelcomePage2
import com.izanaminightz.mochi.android.ui.view.Welcome.WelcomePage3
import com.izanaminightz.mochi.domain.models.MangadexFeedData
import com.izanaminightz.mochi.domain.models.UserModel
import com.izanaminightz.mochi.presentation.reader.ReaderDataModel
import com.izanaminightz.mochi.utils.MochiHelper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MochiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    StartScreen()
                }

            }
        }
    }
}

sealed class WelcomeScreen(val id: Int) {
    object PageOne: WelcomeScreen(1)

}

sealed class Screens(val title: String) {
    object DiscoveryScreen: Screens(title = "Discovery")
    object DetailScreen: Screens(title = "Detail/{mangaID}")
    object FeedScreen: Screens(title = "Feed/{mangaID}")
    object SearchScreen: Screens(title = "Search")
    object Reader: Screens(title = "Reader")
    object List: Screens(title = "List")
}

sealed class BottomNavScreens(val title: String) {
    object Home: BottomNavScreens("Discovery")
    object List: BottomNavScreens("List")
}

private val bottomScreenLists = listOf(
    BottomNavScreens.Home,
    BottomNavScreens.List,
)



@OptIn(ExperimentalPagerApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun StartScreen() {

    val showBottomTab by remember {
        mutableStateOf(false)
    }

    val navController = rememberAnimatedNavController()

    val completedOnboarding = MochiHelper().completedOnboarding()

    @Composable
     fun currentRoute(): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }

   Scaffold(
       bottomBar = {
            val d = navController.currentBackStackEntryAsState().value?.destination?.route
           if ((d?.contains("Welcome") == false))
               if ((!d.contains("Logged")))
           BottomNavigation() {
               val navBackStackEntry by navController.currentBackStackEntryAsState()
               val currentDestination = navBackStackEntry?.destination


               bottomScreenLists.forEach { screen ->
                   BottomNavigationItem(
                       selected = currentDestination?.hierarchy?.any { it.route == screen.title } == true,
                       onClick = {
                           navController.navigate(screen.title) {

                               // Pop up to the start destination of the graph to
                               // avoid building up a large stack of destinations
                               // on the back stack as users select items
                               popUpTo(navController.graph.findStartDestination().id) {
                                   saveState = true
                               }
                               // Avoid multiple copies of the same destination when
                               // reselecting the same item
                               launchSingleTop = true
                               // Restore state when reselecting a previously selected item
                               restoreState = true
                           }
                       },
                       icon = {
                           Icon(when(screen) {
                                BottomNavScreens.Home -> Icons.Filled.Home
                               BottomNavScreens.List -> Icons.Sharp.AccountBox }, screen.title)
                       },
                       label = {
                           Text(
                               text = when (screen) {
                                   BottomNavScreens.Home -> "Discovery"
                                   BottomNavScreens.List -> "My List"
                               }
                           )
                       }
                   )
               }
           }
       }
   ) {
       AnimatedNavHost(
           navController = navController,
//        startDestination = Screens.DiscoveryScreen.title
           startDestination = if (completedOnboarding) Screens.DiscoveryScreen.title else "Welcome/1",
       ) {

           //Welcome
           composable("Welcome/1") {
               WelcomePage1(navController)
           }

           composable("Welcome/2") {
               WelcomePage2(navController)
           }

           composable("Welcome/3") {
               WelcomePage3(navController)
           }

           composable("LoggedInPage/user={user}",
               enterTransition = {
                   when (initialState.destination.route) {
                       "Welcome/2" ->
                           slideIntoContainer(AnimatedContentScope.SlideDirection.Right)
                       else -> null
                   }
               },
               arguments = listOf(
                   navArgument("user") { type = NavType.StringType },

                   )) {
               val user = Json.decodeFromString<UserModel>(it.arguments!!.getString("user")!!)
               LoggedInPage(navController, user)
           }




           //Main

           composable(Screens.DiscoveryScreen.title) {
               DiscoveryScreen(navController)
           }

           composable(Screens.DetailScreen.title, arguments = listOf(
               navArgument("mangaID") { type = NavType.StringType }
           )) { DetailScreen(navController = navController, mangaID = it.arguments!!.getString("mangaID")!! )}

           composable(Screens.FeedScreen.title, arguments = listOf(
               navArgument("mangaID") { type = NavType.StringType }
           )) {
               FeedScreen(navController, mangaID = it.arguments!!.getString("mangaID")!! )
           }

           composable(Screens.SearchScreen.title) {
               SearchScreen(navController)
           }

           composable(Screens.Reader.title) {
               val chapters = navController.previousBackStackEntry?.arguments?.getSerializable("chapters")

               Reader(data = chapters as ReaderDataModel?, navController)
           }

           composable(Screens.List.title) {
               ListScreen(navController)
           }

       }
   }
}