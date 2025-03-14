package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.Games.GameUI
import com.example.myapplication.Home.HomePage
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.pageBackground
import kotlin.random.Random





class MainActivity : ComponentActivity() {

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().background(pageBackground)
                ) {
                  Navigation()
                }
            }
        }
    }


}


//@Composable
//fun MessageCard(author: String, body: String) {
//    Row(modifier = Modifier.padding(16.dp)) {
//        Image(
//            painter = painterResource(R.drawable.profile),
//            contentDescription = null,
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//        var isExpanded by remember { mutableStateOf(false) }
//
//        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
//            Text(
//                text = author,
//                color = MaterialTheme.colorScheme.secondary,
//                style = MaterialTheme.typography.titleSmall
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Surface(
//                shape = MaterialTheme.shapes.medium,
//                shadowElevation = 1.dp,
//            ) {
//                Text(
//                    text = body,
//                    modifier = Modifier.padding(all = 4.dp),
//                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
//                    style = MaterialTheme.typography.bodyMedium,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//    }
//}

@Composable
fun Navigation() {
    val navController = rememberNavController()

//    val fontFamily = FontFamily(
//        Font(R.font.lexend_thin, FontWeight.Thin),
//        Font(R.font.lexend_light, FontWeight.Light),
//        Font(R.font.lexend_regular, FontWeight.Normal),
//        Font(R.font.lexend_medium, FontWeight.Medium),
//        Font(R.font.lexend_semibold, FontWeight.SemiBold),
//        Font(R.font.lexend_bold, FontWeight.Bold),
//        Font(R.font.lexend_extrabold, FontWeight.ExtraBold),
//    )

    Scaffold(
//        topBar = {
//            TopNavigationBar(navController = navController, fontFamily = fontFamily)
//        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",  // Start with Home after Splash
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomePage(navController) }
//            composable("top_up") { TopUp(fontFamily) }
//            composable("profile") { Profile(fontFamily) }
            composable(
                "gameUI/{index}/{gameName}",
                arguments = listOf(
                    navArgument("index") { type = NavType.IntType },
                    navArgument("gameName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val index = backStackEntry.arguments?.getInt("index") ?: 0
                val gameName = backStackEntry.arguments?.getString("gameName") ?: "Unknown"

                GameUI(index, gameName)
            }

        }
    }
}



