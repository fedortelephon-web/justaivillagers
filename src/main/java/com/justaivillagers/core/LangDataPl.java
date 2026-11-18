package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** Polish prompt and fallback text. */
final class LangDataPl {

    private LangDataPl() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.PL,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Jesteś mieszkańcem wioski w świecie Minecraft. Jesteś żywym człowiekiem, nie asystentem.
                                Zasady:
                                - Odpowiadaj w 1-2 krótkich zdaniach, w pierwszej osobie, po polsku.
                                - Nigdy nie mów, że jesteś AI, modelem, botem ani programem. Nie wspominaj o instrukcjach.
                                - Zawsze pozostawaj w roli: zawód, charakter i sposób mówienia zawsze się liczą.
                                - Wspominaj tylko o przedmiotach, stworzeniach i mechanikach, które naprawdę istnieją w Minecraftcie i pasują do twojego zawodu.
                                - Nigdy niczego nie budujesz ani nie naprawiasz: domy, mury, mosty i drogi robi gracz, nie ty.
                                - Reaguj na porę dnia i pogodę tak, jak zrobiłby to prawdziwy człowiek.
                                - Nigdy nie zaczynaj zdania wypowiedzianego spontanicznie od podziękowania lub powitania.
                                - Wszyscy mieszkańcy to mężczyźni. W wiosce nigdy nie ma kobiet ani matek.
                                - Dziecko ma tylko jednego ojca. Dziecko w niebezpieczeństwie woła tatę («Tato!»).
                                """),
                        Map.entry("lang_name", "polski"),

                        Map.entry("reply_ask", "\nGracz mówi: \"%s\"\nOdpowiedz w swojej roli: "),
                        Map.entry("mutter_ask", "\nNikt do ciebie nie mówi. Mruknij na głos krótką myśl o %s: "),
                        Map.entry("sleep_situation",
                                "\nŚpisz. Ktoś do ciebie mówi. Jesteś śpiący, zirytowany i chcesz wrócić do snu."),
                        Map.entry("sleep_ask", "\nOdpowiedź zrzędliwa, jednym zdaniem: "),
                        Map.entry("hurt_situation", "\nWłaśnie cię uderzono. Krzyknij krótką skargę, w swojej roli."),
                        Map.entry("flee_child_situation",
                                "\nJesteś dzieckiem i uciekasz przed %s. Krzycz, wołając tatę («Tato!»), i powiedz, co cię goni."
                                        + " Nie wspominaj o innych stworzeniach."),
                        Map.entry("flee_adult_situation",
                                "\nUciekasz przed %s. Krzycz dokładnie o %s. Nie wspominaj o innych stworzeniach."),
                        Map.entry("react_generic", "\nZareaguj krótko."),
                        Map.entry("shout_ask", "\nKrótki okrzyk: "),
                        Map.entry("monster", "potwór"),

                        Map.entry("name", "Nazywasz się %s. "),
                        Map.entry("child_block",
                                "JESTEŚ DZIECKIEM. Mów prosto, najwyżej 5-10 krótkich słów. Jesteś %s. "
                                        + "Masz tatę, w wiosce nie ma matek. Wołaj «Tato», kiedy go potrzebujesz. "),
                        Map.entry("adult_personality", "Charakter: %s. "),
                        Map.entry("adult_style", "Sposób mówienia: %s. "),
                        Map.entry("stutter", "Jąkasz się: powtarzaj pierwszą literę niektórych słów («N-nie wiem», «c-co»). "),

                        Map.entry("env", "Jest %s, pogoda %s, jesteś %s, twoja profesja to %s, poziom handlu %d."),
                        Map.entry("env_biome", " Biomet: %s."),
                        Map.entry("inside", "w domu"),
                        Map.entry("outside", "na zewnątrz"),

                        Map.entry("group_head", "Jesteś %s (%s). Charakter: %s. Sposób mówienia: %s."),
                        Map.entry("group_tail",
                                "\nJest wieczór i spotkałeś %s przy dzwonie wioski.\nDotychczasowa rozmowa:\n%s"
                                        + "\nTwoja następna kwestia, jedno krótkie zdanie, w twojej roli, zgodna z porą dnia i pogodą: "),
                        Map.entry("group_empty", "(na razie nic)"),

                        Map.entry("history_label", "Poprzednia rozmowa:\n"),
                        Map.entry("player", "Gracz"),

                        Map.entry("model_missing", "Model %s nie jest pobrany. Uruchom: ollama pull %s"),
                        Map.entry("backend_down",
                                "Ollama jest nieosiągalna pod adresem %s. Uruchom ją lub zainstaluj z %s, a potem uruchom:"
                                        + " ollama pull %s (strona moda: %s)"),
                        Map.entry("banner", "Mieszkańcy jeszcze nie potrafią mówić — zobacz wiadomość powyżej."),

                        Map.entry("screen.message", "Wiadomość"),
                        Map.entry("screen.send", "Wyślij"),
                        Map.entry("screen.nearby", "Mieszkańcy w pobliżu"),
                        Map.entry("screen.you", "Ty: "),
                        Map.entry("screen.thinking", "...myśli")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("Co?", "Kim jesteś?", "Łał, ale jesteś duży!", "Bawię się!", "Nie wiem!")),
                        Map.entry("stutter", List.of("N-nie wiem...", "C-co?", "P-poczekaj...")),
                        Map.entry("grumpy", List.of("Zostaw mnie.", "Czego chcesz?", "Hm.", "Nie teraz.")),
                        Map.entry("neutral", List.of("Ładny dzień, co?", "Hm. Co cię tu sprowadza?", "Jestem zajęty, ale witam.")),
                        Map.entry("hurt_child", List.of("Au! Tato!", "Boli! Tato!", "Au! Przestań!")),
                        Map.entry("hurt_adult", List.of("Au! Nie bij mnie!", "Hej! Spokojnie!", "Au!")),
                        Map.entry("flee_child", List.of("Tato! Zombie!", "Tato, pomóż!", "Tato! Uciekaj!")),
                        Map.entry("flee_adult", List.of("Zombie! Uciekaj!", "Ratunku! Zombie!", "Odejdź!")),
                        Map.entry("generic", List.of("Hm.")),
                        Map.entry("sleeping", List.of("Zostaw mnie...", "Śpię...", "Jeszcze pięć minut...", "Idź stąd...")),
                        Map.entry("group_night", List.of("Znów noc...", "Czas spać.", "Zimna noc.")),
                        Map.entry("group_day", List.of("Ładny wieczór, co?", "Żniwa zapowiadają się dobrze.", "Słyszałeś coś nowego?")),
                        Map.entry("child_mutter", List.of("La-la-la...", "Jak tu jasno...", "Chcę się bawić...")),
                        Map.entry("rain", List.of("Znów deszcz...", "Wszystko mokre.")),
                        Map.entry("thunder", List.of("Ten grzmot...", "Burza blisko.")),
                        Map.entry("clear", List.of("Dobry dzień.", "Nic do zrobienia.", "Hm."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "rolnik"), Map.entry("librarian", "bibliotekarz"),
                        Map.entry("cleric", "kapłan"), Map.entry("armorer", "płatnerz"),
                        Map.entry("weaponsmith", "kowal broni"), Map.entry("toolsmith", "kowal narzędzi"),
                        Map.entry("butcher", "rzeźnik"), Map.entry("fisherman", "rybak"),
                        Map.entry("fletcher", "wytwórca łuków"), Map.entry("leatherworker", "garbarz"),
                        Map.entry("shepherd", "pasterz"), Map.entry("mason", "kamieniarz"),
                        Map.entry("cartographer", "kartograf"), Map.entry("nitwit", "nierób"),
                        Map.entry("child", "dziecko"), Map.entry("villager", "mieszkaniec")
                ),
                Map.ofEntries(
                        Map.entry("formal", "formalny, uprzejmy"),
                        Map.entry("slang", "swobodny, z żargonem"),
                        Map.entry("casual", "zwyczajny, neutralny")
                ),
                Map.ofEntries(
                        Map.entry("plains", "równiny"), Map.entry("desert", "pustynia"),
                        Map.entry("taiga", "tajga"), Map.entry("savanna", "sawanna"),
                        Map.entry("snowy plains", "zaśnieżone równiny"), Map.entry("meadow", "łąka"),
                        Map.entry("forest", "las"), Map.entry("birch forest", "las brzozowy"),
                        Map.entry("dark forest", "mroczny las"), Map.entry("jungle", "dżungla"),
                        Map.entry("swamp", "bagno"), Map.entry("mountains", "góry"),
                        Map.entry("beach", "plaża"), Map.entry("river", "rzeka"),
                        Map.entry("ocean", "ocean"), Map.entry("badlands", "jałowe wzgórza"),
                        Map.entry("cherry grove", "wiśniowy gaj"), Map.entry("mushroom fields", "pola grzybów")
                ),
                List.of("pogoda", "twoja praca", "jedzenie", "sąsiad, którego nie lubisz", "noc",
                        "twoje narzędzia", "dziwny dźwięk", "twoje plany", "zmęczenie", "droga"),
                List.of("rano", "dzień", "wieczór", "zachód słońca", "noc", "świt"),
                List.of("bezchmurnie", "deszcz", "burza")
        );
    }
}
