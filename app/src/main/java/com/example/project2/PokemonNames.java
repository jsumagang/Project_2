package com.example.project2;
import java.util.Arrays;
import java.util.List;

public class PokemonNames {
    public static List<String> getStarterPokemon() {
        return Arrays.asList(
                // Generation I (Kanto)
                "bulbasaur", "charmander", "squirtle",

                // Generation II (Johto)
                "chikorita", "cyndaquil", "totodile",

                // Generation III (Hoenn)
                "treecko", "torchic", "mudkip",

                // Generation IV (Sinnoh)
                "turtwig", "chimchar", "piplup",

                // Generation V (Unova)
                "snivy", "tepig", "oshawott",

                // Generation VI (Kalos)
                "chespin", "fennekin", "froakie",

                // Generation VII (Alola)
                "rowlet", "litten", "popplio",

                // Generation VIII (Galar)
                "grookey", "scorbunny", "sobble",

                // Generation IX (Paldea)
                "sprigatito", "fuecoco", "quaxly"
        );
    }
    public static List<String> getPseudoLegendaryPokemon() {
        return Arrays.asList(
                // Generation I (Kanto)
                "dragonite",

                // Generation II (Johto)
                "tyranitar",

                // Generation III (Hoenn)
                "salamence",
                "metagross",

                // Generation IV (Sinnoh)
                "garchomp",

                // Generation V (Unova)
                "hydreigon",

                // Generation VI (Kalos)
                "dragapult",

                // Generation VII (Alola)
                "kommo-o",

                // Generation VIII (Galar)
                "duraludon",

                // Generation IX (Paldea)
                "iron Hands"
        );
    }
    public static List<String> getLegendaryPokemon() {
        return Arrays.asList(
                // Generation I (Kanto)
                "articuno", "zapdos", "moltres", "mewtwo", "mew",

                // Generation II (Johto)
                "raikou", "entei", "suicune", "lugia", "ho-oh",

                // Generation III (Hoenn)
                "registeel", "regice", "regirock", "kyogre", "groudon", "rayquaza", "deoxys",

                // Generation IV (Sinnoh)
                "dialga", "palkia", "giratina", "regigigas", "uxie", "mesprit", "azelf", "cresselia", "manaphy", "phione", "darkrai", "shaymin", "arceus",

                // Generation V (Unova)
                "victini", "cobalion", "terrakion", "virizion", "tornadus", "thundurus", "landorus", "reshiram", "zekrom", "kyurem", "meloetta", "genesect",

                // Generation VI (Kalos)
                "xerneas", "yveltal", "zygarde", "diancie", "hoopa", "volcanion",

                // Generation VII (Alola)
                "cosmog", "cosmoem", "solgaleo", "lunala", "necrozma", "magearna", "marshadow", "zeraora",

                // Generation VIII (Galar)
                "zacian", "zamazenta", "eternatus", "kubfu", "urshifu", "zarude", "regieleki", "regidrago", "calyrex",

                // Generation IX (Paldea)
                "koraidon", "miraidon", "wyrdeer", "enamorus"
        );
    }
}
