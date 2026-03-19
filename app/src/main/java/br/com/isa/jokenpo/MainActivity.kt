package br.com.isa.jokenpo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.isa.jokenpo.ui.theme.JokenpoTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JokenpoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    JokenpoScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun JokenpoScreen(modifier: Modifier = Modifier) {

    val defaultPokemon = Pokemon("-", R.drawable.pokeball_unselected)
    var pokemonSorting by remember { mutableStateOf(defaultPokemon) }
    var pokemonSelected by remember { mutableStateOf(defaultPokemon) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PokeHeader()
        Spacer(modifier = Modifier.weight(1f))
        BattleArena(pokemonSelected, pokemonSorting)
        Spacer(modifier = Modifier.weight(1f))
        BattleResult(pokemonSelected, pokemonSorting)
        Poketext()
        PokemonOptionsList(starters, pokemonSelected) {
            pokemonSelected = it
            pokemonSorting = sortearPokemon()
        }
    }
}

fun sortearPokemon(): Pokemon{
    val index = Random.nextInt(starters.size)
    return starters[index]
}

@Composable
fun BattleArena(player: Pokemon, computer: Pokemon) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        PokemonCard(player, "Você")
        PokemonCard(computer, "Computador")
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Image(
            painter = painterResource(pokemon.imageRes),
            contentDescription = pokemon.name,
            modifier = Modifier.size(130.dp)
        )
        Text(
            pokemon.name.uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(label, fontSize = 18.sp)
    }
}

@Composable
fun BattleResult(pokemonSelected: Pokemon, pokemonSorting: Pokemon) {
    val resultado = when {
        pokemonSelected.name == "-" || pokemonSorting.name == "-" -> ""
        pokemonSelected.name == pokemonSorting.name -> "Empate!"
        pokemonSelected.name == "Bulbasaur" && pokemonSorting.name == "Squirtle" -> "Você venceu!"
        pokemonSelected.name == "Charmander" && pokemonSorting.name == "Bulbasaur" -> "Você venceu!"
        pokemonSelected.name == "Squirtle" && pokemonSorting.name == "Charmander" -> "Você venceu!"
        else -> "Computador venceu!"
    }

    Text(
        text = resultado,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFE53935)
    )
}

@Composable
fun Poketext() {
        Text(
            text = "Faça sua jogada de mestre",
            fontSize = 18.sp,
        )
}

@Composable
fun PokeHeader() {
    Image(
        painter = painterResource(R.drawable.logo_pokemon),
        contentDescription = "Logo Pokemon",
        modifier = Modifier.height(100.dp)
    )
}

//val são valores imutaveis (não é possivel alterar) ao contario de var
//val possui um tempo de resposta melhor
data class Pokemon(
    val name: String,
    val imageRes: Int
)

//variavel global
val starters = listOf(
    Pokemon("Bulbassaur", R.drawable.bulbassaur),
    Pokemon("Charmander", R.drawable.charmander),
    Pokemon("Squirtle", R.drawable.squirtle)
)


@Composable
fun PokemonOption(
    pokemon: Pokemon,
    select: Boolean,
    onSelected: (Pokemon) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onSelected(pokemon) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (select)
                painterResource(R.drawable.pokeball_selected)
            else painterResource(R.drawable.pokeball_unselected),
            contentDescription = "",
            modifier = Modifier.size(40.dp),
            colorFilter = if (isSystemInDarkTheme() && !select)
                ColorFilter.tint(Color.White) else null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(pokemon.name)

    }
}

@Composable
fun PokemonOptionsList(
    pokemons: List<Pokemon>,
    pokemonSelected: Pokemon,
    onSelected: (Pokemon) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        pokemons.forEach { pokemon ->
            PokemonOption(
                pokemon,
                pokemon == pokemonSelected
            ) {
                onSelected(pokemon)
            }

        }
    }
}


@Preview
@Composable
private fun PokemonStarterScreenPreview() {
    JokenpoTheme() {
        JokenpoScreen()
    }
}