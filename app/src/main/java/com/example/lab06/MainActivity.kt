package com.example.lab06

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab06.ui.theme.Lab06Theme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab06Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }


}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") { ListScreen(navController = navController) }
        composable("form") { FormScreen(navController = navController) }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Lab06Theme {
        MainScreen(
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add task",
                        modifier = Modifier.scale(1.5f)
                    )
                },
                onClick = {
                    navController.navigate("form")
                }
            )
        },
        topBar = {
            AppTopBar(
                navController = navController,
                title = "List",
                showBackIcon = false,
                route = "form"
            )
        },
        content = {

            LazyColumn(modifier = Modifier.padding(it)) {
                items(items = todoTasks()) { item ->
                    ListItem(item = item)
                }
            }
        }

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormScreen(navController: NavController) {
    var title by remember {
        mutableStateOf("")
    }
    val datePickerState = rememberDatePickerState()
    val showDialog = rememberSaveable { mutableStateOf(false) }
    var expandedS by remember { mutableStateOf(false) }
    var expandedP by remember { mutableStateOf(false) }
    var state by remember {
        mutableStateOf(false)
    }
    var priority by remember {
        mutableStateOf(Priority.Low)
    }
    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Form",
                showBackIcon = true,
                route = "list"
            )
        },
    content = {

        TextField(value = title , onValueChange ={title=it} ,modifier = Modifier.padding((100.dp)))
        Text(text = "Title: ", modifier = Modifier
            .fillMaxWidth()
            .padding(100.dp))
        Button(onClick = { showDialog.value=!showDialog.value }, modifier = Modifier.padding(horizontal = 0.dp, vertical = 160.dp)) {
            Text(text = "Select date")
        }
        Button(onClick = { expandedS=!expandedS },modifier = Modifier.padding(horizontal = 0.dp, vertical = 220.dp)) {
            Text(text = "Select state")
        }
        Button(onClick = { expandedP=!expandedP },modifier = Modifier.padding(horizontal = 0.dp, vertical = 280.dp)) {
            Text(text = "Select priority")
        }

        DropdownMenu(expanded = expandedS, onDismissRequest = { expandedS=false  }, modifier = Modifier.fillMaxWidth()) {
            DropdownMenuItem(text = { Text(text = "doing") }, onClick = { state=false; expandedS=false })
            DropdownMenuItem(text = { Text(text = "done") }, onClick = { state=true; expandedS=false })
        }

        DropdownMenu(expanded = expandedP, onDismissRequest = { expandedS=false  }, modifier = Modifier.fillMaxWidth()) {
            DropdownMenuItem(text = { Text(text = "Low") }, onClick = { priority=Priority.Low; expandedP=false })
            DropdownMenuItem(text = { Text(text = "Medium") }, onClick = {priority=Priority.Medium; expandedP=false })
            DropdownMenuItem(text = { Text(text = "High") }, onClick = { priority=Priority.High; expandedP=false })
        }
        if (showDialog.value) {
        DatePickerDialog(
            modifier = Modifier.padding(110.dp),
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        ){
            DatePicker(state = datePickerState)
        }}


    }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    title: String,
    showBackIcon: Boolean,
    route: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackIcon) {
                IconButton(onClick = { navController.navigate(route) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (route !== "form") {
                OutlinedButton(
                    onClick = { navController.navigate("list") }
                )
                {
                    Text(
                        text = "Zapisz",

                        //fontSize = 18.dp
                    )
                }
            } else {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "")
                }
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
var todolist = listOf(TodoTask("Programming", LocalDate.of(2024, 4, 18), false, Priority.Low),
    TodoTask("Teaching", LocalDate.of(2024, 5, 12), false, Priority.High),
    TodoTask("Learning", LocalDate.of(2024, 6, 28), true, Priority.Low),
    TodoTask("Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium),)

@RequiresApi(Build.VERSION_CODES.O)
fun todoTasks(): List<TodoTask> {
    return todolist
}

enum class Priority() {
    High, Medium, Low
}

data class TodoTask(
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority
)

@SuppressLint("SuspiciousIndentation")
@Composable
fun ListItem(item: TodoTask, modifier: Modifier = Modifier) {

    var color = androidx.compose.ui.graphics.Color.Green
        if (item.isDone){
        color = androidx.compose.ui.graphics.Color.Red
        }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .background(color)
            .heightIn(120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Text(text = "Title: ${item.title}",
            modifier= modifier
                .background(color)
                .fillMaxSize()
                .padding(8.dp),
            fontWeight = FontWeight.Bold,
            color= androidx.compose.ui.graphics.Color.Black)
        Text(text = "Priority: ${item.priority}",
            modifier= modifier
                .background(color)
                .fillMaxSize()
                .padding(8.dp),
            fontWeight = FontWeight.Bold,
            color= androidx.compose.ui.graphics.Color.Black)

        Text(text="Deadline: ${item.deadline}",
            modifier= modifier
                .background(color)
                .fillMaxSize()
                .padding(8.dp),
            fontWeight = FontWeight.Bold,
            color= androidx.compose.ui.graphics.Color.Black)
        //...
    }
}