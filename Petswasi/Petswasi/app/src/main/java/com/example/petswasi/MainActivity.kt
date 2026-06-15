package com.example.petswasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petswasi.data.repository.UserRepository
import com.example.petswasi.data.repository.PetRepository
import com.example.petswasi.ui.theme.PetswasiTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.example.petswasi.data.model.Pet

class MainActivity : ComponentActivity() {
    private val userRepository = UserRepository()
    private val petRepository = PetRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetswasiTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "welcome") {
                    composable("welcome") {
                        LaunchedEffect(Unit) {
                            val currentPets = petRepository.getAllPets()
                            if (currentPets.isEmpty()) {
                                val samplePets = listOf(
                                    Pet("1", "Mochi", "Gato Atigrado", "3 años", "Grande", "Un gato muy cariñoso y tranquilo.", "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=500"),
                                    Pet("2", "Max", "Husky Siberiano", "2 años", "Mediano", "Energético y juguetón, le encanta correr.", "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=500"),
                                    Pet("3", "Luna", "Labrador", "1 año", "Grande", "Muy obediente y amigable con los niños.", "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=500"),
                                    Pet("4", "Toby", "Pug", "4 años", "Pequeño", "Le encanta dormir y comer.", "https://images.unsplash.com/photo-1517849845537-4d257902454a?w=500"),
                                    Pet("5", "Rocky", "Boxer", "5 años", "Grande", "Un excelente guardián y protector.", "https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=500"),
                                    Pet("6", "Bella", "Golden Retriever", "2 años", "Grande", "Amorosa y muy inteligente.", "https://images.unsplash.com/photo-1552053831-71594a27632d?w=500"),
                                    Pet("7", "Coco", "Caniche", "3 años", "Pequeño", "Elegante y muy vivaz.", "https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?w=500"),
                                    Pet("8", "Simba", "Gato Naranja", "1 año", "Mediano", "Travieso y curioso.", "https://images.unsplash.com/photo-1574158622682-e40e69881006?w=500"),
                                    Pet("9", "Duke", "Pastor Alemán", "4 años", "Grande", "Valiente y leal.", "https://images.unsplash.com/photo-1589944173250-41efde93e32d?w=500"),
                                    Pet("10", "Daisy", "Beagle", "2 años", "Mediano", "Le encanta seguir rastros.", "https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?w=500"),
                                    Pet("11", "Bruno", "Bulldog", "3 años", "Mediano", "Perezoso pero muy tierno.", "https://images.unsplash.com/photo-1512446816042-444d641267d4?w=500"),
                                    Pet("12", "Mia", "Gata Blanca", "2 años", "Pequeño", "Independiente y sofisticada.", "https://images.unsplash.com/photo-1548546738-8509cb246ed3?w=500"),
                                    Pet("13", "Rex", "Rottweiler", "5 años", "Grande", "Fuerte y equilibrado.", "https://images.unsplash.com/photo-1567171466295-4afa5814522f?w=500"),
                                    Pet("14", "Zoey", "Shih Tzu", "1 año", "Pequeño", "Pequeña bola de pelos juguetona.", "https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=500"),
                                    Pet("15", "Cooper", "Dálmata", "3 años", "Grande", "Activo y con mucha energía.", "https://images.unsplash.com/photo-1517423568366-8b83523034fd?w=500"),
                                    Pet("16", "Nala", "Gata Carey", "4 años", "Mediano", "Tranquila y observadora.", "https://images.unsplash.com/photo-1513245543132-31f507417b26?w=500")
                                )
                                samplePets.forEach { petRepository.addPet(it) }
                            }
                        }

                        WelcomeScreen(
                            onLoginClick = { navController.navigate("login") },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }
                    composable("login") {
                        LoginScreen(userRepository, { nombre -> 
                            navController.navigate("home/$nombre") { popUpTo("welcome") { inclusive = true } }
                        }, { navController.popBackStack() })
                    }
                    composable("register") {
                        RegisterScreen(userRepository, { navController.popBackStack() })
                    }
                    composable("home/{nombre}") { backStackEntry ->
                        val nombre = backStackEntry.arguments?.getString("nombre") ?: "Usuario"
                        MainNavigationWrapper(nombre, userRepository, petRepository, navController)
                    }
                    composable("pet_detail/{petId}") { backStackEntry ->
                        val petId = backStackEntry.arguments?.getString("petId") ?: ""
                        PetDetailScreen(petId, petRepository, { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

@Composable
fun MainNavigationWrapper(nombre: String, userRepository: UserRepository, petRepository: PetRepository, navController: NavController) {
    var currentScreen by remember { mutableStateOf("inicio") }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = currentScreen == "inicio", onClick = { currentScreen = "inicio" })
                NavigationBarItem(icon = { Icon(Icons.Default.Notifications, null) }, label = { Text("Fundación") }, selected = currentScreen == "fundacion", onClick = { currentScreen = "fundacion" })
                NavigationBarItem(icon = { Icon(Icons.Default.Info, null) }, label = { Text("Acerca de") }, selected = currentScreen == "acerca", onClick = { currentScreen = "acerca" })
                NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil") }, selected = currentScreen == "perfil", onClick = { currentScreen = "perfil" })
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentScreen) {
                "inicio" -> HomeScreenContent(nombre, petRepository) { petId -> navController.navigate("pet_detail/$petId") }
                "fundacion" -> FoundationsScreen(nombre, userRepository)
                "acerca" -> AboutScreen(nombre, userRepository)
                "perfil" -> ProfileScreen(nombre) { navController.navigate("welcome") { popUpTo(0) } }
            }
        }
    }
}

@Composable
fun HomeScreenContent(nombre: String, petRepository: PetRepository, onPetClick: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    
    // Estados de filtros
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedSize by remember { mutableStateOf("Todos") }
    var selectedGender by remember { mutableStateOf("Todos") }
    var onlyVaccinated by remember { mutableStateOf(false) }

    var petsRealtime by remember { mutableStateOf(listOf<Pet>()) }
    LaunchedEffect(Unit) { petRepository.listenPets { petsRealtime = it } }
    
    val filteredPets = petsRealtime.filter { pet ->
        val matchesSearch = pet.name.contains(searchText, ignoreCase = true) || 
                          pet.breed.contains(searchText, ignoreCase = true)
        
        val matchesCategory = when (selectedCategory) {
            "Cachorro" -> pet.age.contains("mes", ignoreCase = true) || pet.age.contains("1 año", ignoreCase = true)
            "Adulto" -> !pet.age.contains("mes", ignoreCase = true) && !pet.age.contains("1 año", ignoreCase = true)
            else -> true
        }

        val matchesSize = if (selectedSize == "Todos") true else pet.size.equals(selectedSize, ignoreCase = true)
        val matchesGender = if (selectedGender == "Todos") true else true // Por ahora no tenemos género en el modelo Pet

        matchesSearch && matchesCategory && matchesSize
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE0E7E7)).padding(20.dp)) {
        Text("Hola, $nombre", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            Text("Cercado, Cochabamba", fontSize = 14.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(20.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchText, 
                onValueChange = { searchText = it }, 
                placeholder = { Text("Buscar mascota...") }, 
                leadingIcon = { Icon(Icons.Default.Search, null) }, 
                modifier = Modifier.weight(1f), 
                shape = RoundedCornerShape(15.dp), 
                colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                onClick = { showFilters = !showFilters },
                modifier = Modifier.size(50.dp).background(Color.White, RoundedCornerShape(10.dp))
            ) {
                Icon(Icons.Default.Tune, null)
            }
        }

        if (showFilters) {
            FilterSection(
                selectedCategory = selectedCategory,
                onCategorySelect = { selectedCategory = it },
                selectedSize = selectedSize,
                onSizeSelect = { selectedSize = it },
                selectedGender = selectedGender,
                onGenderSelect = { selectedGender = it },
                onlyVaccinated = onlyVaccinated,
                onVaccinatedChange = { onlyVaccinated = it },
                onClear = {
                    selectedCategory = "Todos"
                    selectedSize = "Todos"
                    selectedGender = "Todos"
                    onlyVaccinated = false
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("${filteredPets.size} animales rescatados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            filteredPets.forEach { pet ->
                PetCardHorizontal(pet.name, pet.breed, pet.age, pet.size, pet.imageUrl) { onPetClick(pet.id) }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun FilterSection(
    selectedCategory: String, onCategorySelect: (String) -> Unit,
    selectedSize: String, onSizeSelect: (String) -> Unit,
    selectedGender: String, onGenderSelect: (String) -> Unit,
    onlyVaccinated: Boolean, onVaccinatedChange: (Boolean) -> Unit,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text("Categorias", fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FilterChipCustom("Todos", selectedCategory == "Todos") { onCategorySelect("Todos") }
                FilterChipCustom("Cachorro", selectedCategory == "Cachorro") { onCategorySelect("Cachorro") }
                FilterChipCustom("Adulto", selectedCategory == "Adulto") { onCategorySelect("Adulto") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tamaño", fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FilterChipCustom("Todos", selectedSize == "Todos") { onSizeSelect("Todos") }
                FilterChipCustom("Pequeño", selectedSize == "Pequeño") { onSizeSelect("Pequeño") }
                FilterChipCustom("Grande", selectedSize == "Grande") { onSizeSelect("Grande") }
            }
            TextButton(onClick = onClear, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Limpiar filtros", color = Color(0xFF76D1D1))
            }
        }
    }
}

@Composable
fun FilterChipCustom(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() }.width(90.dp),
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color(0xFFB9F3F3) else Color.White,
        border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null
    ) {
        Text(text = text, modifier = Modifier.padding(vertical = 8.dp), textAlign = TextAlign.Center, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun PetDetailScreen(petId: String, petRepository: PetRepository, onBack: () -> Unit) {
    var pet by remember { mutableStateOf<Pet?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(petId) {
        val allPets = petRepository.getAllPets()
        pet = allPets.find { it.id == petId }
    }

    if (showSuccess) {
        AlertDialog(onDismissRequest = { showSuccess = false; onBack() }, title = { Text("¡Solicitud Enviada!") }, text = { Text("La fundación se pondrá en contacto contigo pronto para iniciar el proceso de adopción.") }, confirmButton = { Button(onClick = { showSuccess = false; onBack() }) { Text("Aceptar") } })
    }

    pet?.let { p ->
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
                AsyncImage(model = p.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                IconButton(onClick = onBack, modifier = Modifier.padding(top = 40.dp, start = 16.dp).background(Color.White.copy(alpha = 0.5f), CircleShape)) { Icon(Icons.Default.ArrowBack, null) }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Text(p.name, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(p.breed, fontSize = 18.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    DetailChip("Edad", p.age)
                    DetailChip("Tamaño", p.size)
                    DetailChip("Sexo", "Macho") 
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(p.description, fontSize = 15.sp, color = Color.DarkGray, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(40.dp))
                Button(onClick = { showSuccess = true }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76D1D1))) {
                    Text("Adoptar ahora", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailChip(label: String, value: String) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFB9F3F3).copy(alpha = 0.3f))) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun WelcomeScreen(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f).align(Alignment.TopCenter)) {
            AsyncImage(model = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?q=80&w=1000", contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.White), startY = 400f)))
            Box(modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp).size(100.dp).background(Color(0xFFB9F3F3), CircleShape).padding(10.dp), contentAlignment = Alignment.Center) {
                AsyncImage(model = "https://cdn-icons-png.flaticon.com/512/1864/1864514.png", contentDescription = null)
            }
        }
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp).align(Alignment.BottomCenter).padding(bottom = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Encuentra un amigo para toda la vida", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76D1D1))) { Text("Iniciar Sesión", fontWeight = FontWeight.Bold) }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB9F3F3))) { Text("Registrarse", color = Color(0xFF4A4A4A), fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun LoginScreen(userRepository: UserRepository, onLoginSuccess: (String) -> Unit, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(model = "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?q=80&w=1000", contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = 0.3f)
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = "https://cdn-icons-png.flaticon.com/512/1864/1864514.png", contentDescription = null, modifier = Modifier.size(80.dp).padding(bottom = 16.dp))
            Text("Iniciar Sesión", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            TextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            errorMessage?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp)) }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { scope.launch { isLoading = true; val nombre = userRepository.login(email, password); isLoading = false; if (nombre != null) onLoginSuccess(nombre) else errorMessage = "Error" } }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76D1D1))) { if (isLoading) CircularProgressIndicator(color = Color.White) else Text("Entrar") }
            TextButton(onClick = onBack) { Text("Volver") }
        }
    }
}

@Composable
fun RegisterScreen(userRepository: UserRepository, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Crear Cuenta", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { userRepository.createUsuario(System.currentTimeMillis().toString(), email, nombre, password); onBack() }, modifier = Modifier.fillMaxWidth()) { Text("Registrarse y Guardar") }
        TextButton(onClick = onBack) { Text("Volver") }
    }
}

@Composable
fun FoundationsScreen(usuarioNombre: String, userRepository: UserRepository) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE0E7E7)).padding(20.dp).verticalScroll(rememberScrollState())) {
        Text("Fundaciones", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Contacta a fundaciones para adopción o rescate", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(20.dp))
        FoundationCard("Fundación RUGIMOS", 142, "Rescate y rehabilitación de animales en Cochabamba.", "rugimos@gmail.com", "70012345", usuarioNombre, userRepository)
        Spacer(modifier = Modifier.height(15.dp))
        FoundationCard("Patitas Felices", 87, "Fundación dedicada a la adopción responsable de mascotas.", "patitas@fundacion.org", "70067890", usuarioNombre, userRepository)
        Spacer(modifier = Modifier.height(15.dp))
        FoundationCard("Hogar Animal Bolivia", 215, "Albergue y cuidado de animales sin hogar en Bolivia.", "hogar@animalbolivia.org", "70098765", usuarioNombre, userRepository)
    }
}

@Composable
fun FoundationCard(name: String, count: Int, desc: String, email: String, phone: String, user: String, repo: UserRepository) {
    var expanded by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Pets, null, modifier = Modifier.size(40.dp), tint = Color(0xFF76D1D1))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("$count animales rescatados", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF76D1D1))
            }
            if (expanded) {
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Text(desc, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Email, null, modifier = Modifier.size(16.dp), tint = Color(0xFF76D1D1)); Spacer(modifier = Modifier.width(5.dp)); Text(email, fontSize = 12.sp, color = Color.Gray) }
                Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Phone, null, modifier = Modifier.size(16.dp), tint = Color(0xFF76D1D1)); Spacer(modifier = Modifier.width(5.dp)); Text(phone, fontSize = 12.sp, color = Color.Gray) }
                Spacer(modifier = Modifier.height(15.dp))
                Text("Enviar mensaje", fontWeight = FontWeight.Bold)
                TextField(value = msg, onValueChange = { msg = it }, placeholder = { Text("Escribe tu consulta...") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFFF1F5F5), unfocusedContainerColor = Color(0xFFF1F5F5)))
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { if (msg.isNotBlank()) { repo.enviarMensaje(name, msg, user); msg = ""; expanded = false } }, modifier = Modifier.fillMaxWidth().height(45.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76D1D1)), shape = RoundedCornerShape(15.dp)) { Text("Enviar", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun AboutScreen(nombre: String, userRepository: UserRepository) {
    var donacionesRealtime by remember { mutableStateOf(listOf<com.example.petswasi.data.model.Donation>()) }
    LaunchedEffect(Unit) { userRepository.listenDonaciones { donacionesRealtime = it } }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE0E7E7)).padding(20.dp).verticalScroll(rememberScrollState())) {
        Text("Acerca de", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Conoce nuestra misión y el impacto que generamos", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Pets, null, tint = Color(0xFFE6A38C), modifier = Modifier.size(24.dp)); Spacer(modifier = Modifier.width(10.dp)); Text("Nuestra misión", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Somos una plataforma dedicada a conectar animales rescatados con familias amorosas. Trabajamos junto a fundaciones bolivianas para facilitar procesos de adopción responsable.", fontSize = 14.sp, lineHeight = 20.sp, color = Color.DarkGray)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth()) {
            StatCard(Modifier.weight(1f), "444", "Rescatados", Icons.Default.Pets, Color(0xFF76D1D1))
            Spacer(modifier = Modifier.width(15.dp))
            StatCard(Modifier.weight(1f), "3", "Aliadas", Icons.Default.AccountBalance, Color(0xFF76D1D1))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Donaciones recientes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                donacionesRealtime.take(5).forEach { DonationRow(it.usuario, "Reciente", it.monto) }
                val total = donacionesRealtime.sumOf { it.monto.replace("Bs. ", "").replace(",", "").toIntOrNull() ?: 0 }
                Surface(color = Color(0xFF76D1D1), shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text("Total Recaudado", color = Color.White, fontWeight = FontWeight.Bold); Text("Bs. $total", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        var selectedAmount by remember { mutableStateOf("") }
        var showConfirmation by remember { mutableStateOf(false) }
        if (showConfirmation) {
            AlertDialog(onDismissRequest = { showConfirmation = false }, title = { Text("Confirmar Donación") }, text = { Text("¿Deseas donar $selectedAmount?") }, confirmButton = { Button(onClick = { userRepository.registrarDonacion(selectedAmount, nombre); showConfirmation = false; selectedAmount = "" }) { Text("Confirmar") } }, dismissButton = { TextButton(onClick = { showConfirmation = false }) { Text("Cancelar") } })
        }
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Proporcionar ayuda", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    DonationChip("Bs. 50", selectedAmount == "Bs. 50") { selectedAmount = "Bs. 50" }
                    DonationChip("Bs. 100", selectedAmount == "Bs. 100") { selectedAmount = "Bs. 100" }
                    DonationChip("Bs. 200", selectedAmount == "Bs. 200") { selectedAmount = "Bs. 200" }
                }
                Button(onClick = { if (selectedAmount.isNotEmpty()) showConfirmation = true }, modifier = Modifier.fillMaxWidth(), enabled = selectedAmount.isNotEmpty(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76D1D1))) { Text("Donar ahora") }
            }
        }
    }
}

@Composable
fun DonationRow(user: String, date: String, amount: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column { Text(user, fontWeight = FontWeight.Bold, fontSize = 14.sp); Text(date, fontSize = 11.sp, color = Color.Gray) }
        Surface(color = Color(0xFFB9F3F3), shape = RoundedCornerShape(10.dp)) { Text(amount, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontSize = 13.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun ProfileScreen(nombre: String, onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE0E7E7)).padding(20.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Notificaciones", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.size(120.dp).background(Color(0xFFB9F3F3), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, modifier = Modifier.size(80.dp), tint = Color.White) }
        Spacer(modifier = Modifier.height(10.dp)); Text(nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Mis Adopciones", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        AdoptionItem("Max", "15 Marzo 2026")
        AdoptionItem("Luna", "3 Febrero 2026")
        Spacer(modifier = Modifier.height(20.dp))
        Text("Estadísticas", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Row(Modifier.fillMaxWidth()) { StatBox(Modifier.weight(1f), "2", "Adopciones"); Spacer(modifier = Modifier.width(15.dp)); StatBox(Modifier.weight(1f), "3", "Favoritos") }
        Spacer(modifier = Modifier.height(20.dp))
        ConfigItem(Icons.Default.Person, "Editar Perfil", "Actualiza tu informacion")
        ConfigItem(Icons.Default.Description, "Terminos", "Lee nuestras politicas")
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6B8B8)), shape = RoundedCornerShape(15.dp)) { Text("Cerrar Sesion", color = Color(0xFF4A4A4A), fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun AdoptionItem(name: String, date: String) {
    Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)), modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Column(modifier = Modifier.padding(15.dp)) { Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp); Text(date, fontSize = 12.sp, color = Color.Gray) }
    }
}

@Composable
fun StatBox(modifier: Modifier, value: String, label: String) {
    Surface(modifier = modifier, color = Color(0xFFB9F3F3).copy(alpha = 0.6f), shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp); Text(label, fontSize = 12.sp, color = Color.Gray) }
    }
}

@Composable
fun ConfigItem(icon: ImageVector, title: String, subtitle: String) {
    Surface(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), color = Color(0xFFB9F3F3).copy(alpha = 0.3f), shape = RoundedCornerShape(15.dp)) {
        Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color(0xFF76D1D1), modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.width(15.dp))
            Column { Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp); Text(subtitle, fontSize = 12.sp, color = Color.Gray) }
        }
    }
}

@Composable
fun PetCardHorizontal(name: String, breed: String, age: String, size: String, imageUrl: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().height(160.dp).clickable { onClick() }) {
        Row {
            AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.width(140.dp).fillMaxHeight(), contentScale = ContentScale.Crop)
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Icon(Icons.Default.Favorite, null, tint = Color(0xFF76D1D1))
                }
                Text("$breed\n$age - $size", color = Color.Gray, fontSize = 14.sp)
                Button(onClick = onClick, modifier = Modifier.height(35.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB9F3F3)), shape = RoundedCornerShape(20.dp), contentPadding = PaddingValues(0.dp)) { Text("Ver detalle", color = Color(0xFF2D3142), fontSize = 14.sp) }
            }
        }
    }
}

@Composable
fun DonationChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, color = if (isSelected) Color(0xFF76D1D1) else Color(0xFFB9F3F3), shape = RoundedCornerShape(10.dp)) {
        Text(text, modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp), fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color(0xFF2D3142))
    }
}

@Composable
fun StatCard(modifier: Modifier, value: String, label: String, icon: ImageVector, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(15.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = Color.Gray.copy(0.5f), modifier = Modifier.size(24.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}
