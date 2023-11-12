package com.debduttapanda.j3use

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.debduttapanda.j3lib.EventBusDescription
import com.debduttapanda.j3lib.MyScreen
import com.debduttapanda.j3lib.NotificationService
import com.debduttapanda.j3lib.Route
import com.debduttapanda.j3lib.WirelessViewModel
import com.debduttapanda.j3lib.rememberNotifier
import com.debduttapanda.j3lib.rememberStringState
import com.debduttapanda.j3lib.wvm
import com.debduttapanda.j3use.ui.theme.J3useTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            J3useTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.splash.full
                    ){
                        MyScreen(
                            navController,
                            Routes.splash,
                            wirelessViewModel = { wvm<SplashViewModel>() }
                        ) {
                            SplashPage()
                        }
                        MyScreen(
                            navController,
                            Routes.login,
                            wirelessViewModel = { wvm<LoginViewModel>() }
                        ) {
                            LoginPage()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashPage(){
    Text("Splash Page")
}

@Composable
fun LoginPage(
    name: State<String> = rememberStringState(id = MyDataIds.Name),
    notifier: NotificationService = rememberNotifier()
){
    Column{
        Text("Login Page")
        Text(name.value)
        Button(onClick = {
            notifier.notify(MyDataIds.ClickMe)
        }) {
            Text("Click Me")
        }
    }
}

class SplashViewModel: WirelessViewModel(){
    override fun eventBusDescription(): EventBusDescription? {
        return null
    }

    override fun onBack() {

    }

    override fun onNotification(id: Any?, arg: Any?) {

    }

    override fun onStart() {

    }
    init {
        viewModelScope.launch {
            delay(2000)
            navigation {
                navigate(Routes.login.full)
            }
        }
    }
}

class LoginViewModel: WirelessViewModel(){
    private val name = mutableStateOf("My name is J3")
    override fun eventBusDescription(): EventBusDescription? {
        return null
    }

    override fun onBack() {

    }

    override fun onNotification(id: Any?, arg: Any?) {
        when(id){
            MyDataIds.ClickMe->{
                toast("Clicked")
            }
        }
    }

    override fun onStart() {

    }

    init {
        toast("Sample Toast")
        controller.resolver.addAll(
            MyDataIds.Name to name
        )
    }
}

enum class MyDataIds{
    Name,
    ClickMe
}

object Routes{
    val splash = Route("splash")
    val login = Route("login")
}