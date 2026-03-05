package br.com.isa.jokenpo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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

    var pokemonSelected by remember { mutableStateOf(starters.first()) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PokeHeader()
        AreaBatalha()
        Spacer(modifier = Modifier.weight(1f))
        PokemonOptionsList("Faça sua jogada de mestre", starters, pokemonSelected) {
            pokemonSelected = it
        }
    }
}

@Composable
fun PokeHeader(
) {
    Image(
        painter = painterResource(id = R.drawable.logo_pokemon),
        contentDescription = "Logo Pokemon",
        modifier = Modifier.size(270.dp)
    )
}

//val são valores imutaveis (não é possivel alterar) ao contario de var
//val possui um tempo de resposta melhor
data class Pokemon(
    val name: String,
    val imageRes: Int = R.drawable.pokeball_unselected,
)

//variavel global
val starters = listOf(
    Pokemon("Bulbassaur", R.drawable.bulbassaur),
    Pokemon("Charmander", R.drawable.charmander),
    Pokemon("Squirtle", R.drawable.squirtle)
)



@Composable
fun AreaBatalha(){

    Row {
        PokemonCard(starters.random())
        PokemonCard(null)
    }

}



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
            colorFilter = if (isSystemInDarkTheme() && !select) ColorFilter.tint(Color.White) else null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(pokemon.name, fontSize = 18.sp)

    }
}

@Composable
fun PokemonOptionsList(
    label: String,
    pokemons: List<Pokemon>,
    pokemonSelected: Pokemon,
    onSelected: (Pokemon) -> Unit
) {
    Text(
        label,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(8.dp)
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        pokemons.forEach { pokemon ->
            PokemonOption(
                pokemon,
                select = pokemon == pokemonSelected
            ) {
                onSelected(pokemon)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonStarterScreenPreview() {
    JokenpoTheme() {
        JokenpoScreen()
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon?) {
    if(pokemon == null) {
        PokeCardItemDefault()
    } else {
        PokemonCardItem(pokemon)
    }
}

@Composable
fun PokemonCardItem(
    pokemon: Pokemon,
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(pokemon.imageRes),
            contentDescription = pokemon.name,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(pokemon.name, fontSize = 18.sp)

    }
}

@Composable
fun PokeCardItemDefault() {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.pokeball_unselected),
            contentDescription = "",
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("-")

    }
}
