import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kmpweb.composeapp.generated.resources.Res
import kmpweb.composeapp.generated.resources.drawer_title
import kmpweb.composeapp.generated.resources.page_two_back_button
import kmpweb.composeapp.generated.resources.page_two_title
import org.jetbrains.compose.resources.stringResource


/**
 * enum values that represent the screens in the app
 */
enum class PhoneScreen(val route: String) {
    Start(route = "start"),
    Page2(route = "page 2")
}

@Composable
fun PhoneNavigation(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = PhoneScreen.Start.route,
    ) {
        composable(PhoneScreen.Start.route) {
            CollapsingToolbarPhoneContent(
                drawerState = drawerState,
                onPage2Click = {
                    navController.navigate(PhoneScreen.Page2.route)
                }
            )
        }
        composable(
            PhoneScreen.Page2.route,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
        ) {
            Page2(
                onBack = { navController.navigateUp() },
                modifier = Modifier.background(MaterialTheme.colors.background),
            )
        }
    }
}

@Composable
fun Page2(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(LocalSafePadding.current)) {
        Text(
            text = stringResource(Res.string.page_two_title),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        )
        Divider()
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = stringResource(Res.string.page_two_back_button)
            )
        }
    }
}

