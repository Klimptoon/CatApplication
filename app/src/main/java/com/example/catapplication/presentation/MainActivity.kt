package com.example.catapplication.presentation

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.example.catapplication.di.CatApplication
import com.example.catapplication.domain.CatModel


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as CatApplication).appComponent

        val factory = appComponent.mainViewModelFactory()

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            AppNavHost(viewModel)
        }
    }
}

@Composable
fun AppNavHost(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                viewModel,
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToDetails = { cat ->
                    val encodedUrl = Uri.encode(cat.url)
                    navController.navigate("details/${cat.id}/$encodedUrl")
                }
            )
        }
        composable("favorites") {
            FavoritesScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(
            "details/{catId}/{catUrl}",
            arguments = listOf(
                navArgument("catId") { type = NavType.StringType },
                navArgument("catUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val catId = backStackEntry.arguments?.getString("catId") ?: return@composable
            val catUrl = backStackEntry.arguments?.getString("catUrl") ?: return@composable

            DetailsScreen(catId = catId, catUrl = catUrl, onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun DetailsScreen(catId: String, catUrl: String, onBack: () -> Unit = {}) {

    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = catUrl),
            contentDescription = "Cat image",
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Id of this cat - $catId",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToFavorites: () -> Unit,
    onNavigateToDetails: (CatModel) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.statusBars.asPaddingValues())) {

        Button(onClick = onNavigateToFavorites, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Go to favorites")
        }

        Button(onClick = { viewModel.loadCats() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Update list")
        }

        Spacer(modifier = Modifier.width(16.dp))

        CatList(viewModel = viewModel, onClick = onNavigateToDetails)
    }

}

@Composable
fun FavoritesScreen(viewModel: MainViewModel, onBack: () -> Unit = {}) {
    val favorites by viewModel.favorite.collectAsState()
    val isEmpty = favorites.isEmpty()

    if (isEmpty) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onBack) {
                Text(text = "Return to Main", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Favorites list is empty, return and add cats",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(favorites) { index, item ->
                CatFavoriteItem(cat = item, viewModel)
            }
        }
    }

    BackHandler {
        onBack()
    }

}

@Composable
fun CatList(viewModel: MainViewModel, onClick: (CatModel) -> Unit) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val cats = (state as UiState.Success).data
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(cats) { index, item ->
                    CatItem(cat = item, viewModel = viewModel, onClick = { onClick(item) })
                }
            }
        }

        is UiState.Error -> {
            val message = (state as UiState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Ошибка: $message", color = Color.Red)
            }
        }
    }
}
