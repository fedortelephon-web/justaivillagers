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

                        Map.entry("model_missing", "Model %s nie jest pobrany. Otwórz ustawienia klawiszem O i naciśnij Pobierz."),
                        Map.entry("backend_down", "Ollama nie działa. Zainstaluj i uruchom ją: %s - następnie otwórz ustawienia klawiszem O i naciśnij Sprawdź."),
                        Map.entry("banner", "Otwórz ustawienia klawiszem O."),
                        Map.entry("settings.status.nomodel", "Serwer działa, ale model %s nie jest pobrany"),
                        Map.entry("settings.check.tip", "Sprawdź wpisany powyżej adres i model, bez zapisywania"),
                        Map.entry("backend_down.repeat", "Ollama nadal nie działa, więc osadnicy nie mogą mówić. Ustawienia: klawisz O."),
                        Map.entry("settings.server.hint", "adres serwera"),
                        Map.entry("settings.server.tip", "Gdzie nasłuchuje Ollama lub zgodny serwer. Domyślnie http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "nazwa modelu"),
                        Map.entry("settings.model.custom.tip", "Dowolny model już pobrany w Ollama. Lista: ollama list"),
                        Map.entry("settings.model.tip", "%s - %s do pobrania, około %s RAM."),
                        Map.entry("settings.model.get.tip", "Pobierz model %s przez Ollama"),
                        Map.entry("settings.ollama.download.tip", "Pobierz instalator Ollama. Uruchom go sam - mod nie uruchamia programów."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "Najszybsza i najprostsza. Dobre do krótkich kwestii, często się gubi."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "Nieco składniejsza od najmniejszej, dobrze trzyma temat."),
                        Map.entry("settings.model.tip.gemma3_1b", "Dobry kompromis na słabym komputerze: naturalna mowa, mało RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Wyraźnie żywsza i gadatliwsza, ale czasem zbacza z tematu."),
                        Map.entry("settings.model.tip.gemma3_4b", "Najlepsza jakość mowy z listy. Potrzeba około 5 GB wolnego RAM."),

                        Map.entry("screen.message", "Wiadomość"),
                        Map.entry("screen.send", "Wyślij"),
                        Map.entry("screen.nearby", "Mieszkańcy w pobliżu"),
                        Map.entry("screen.you", "Ty: "),
                        Map.entry("screen.thinking", "...myśli"),
                        Map.entry("settings.title", "Just AI Villagers - ustawienia"),
                        Map.entry("settings.tab.model", "Model"),
                        Map.entry("settings.tab.features", "Zachowanie"),
                        Map.entry("settings.tab.language", "Język"),
                        Map.entry("settings.server", "Adres serwera"),
                        Map.entry("settings.model", "Model"),
                        Map.entry("settings.model.custom", "Własny"),
                        Map.entry("settings.model.needs", "pobieranie %s, potrzebuje około %s RAM"),
                        Map.entry("settings.model.download", "Pobierz model"),
                        Map.entry("settings.model.get", "Pobierz"),
                        Map.entry("settings.model.installed", "zainstalowany"),
                        Map.entry("settings.model.notinstalled", "nie pobrano"),
                        Map.entry("settings.model.downloading", "pobieranie: %s"),
                        Map.entry("settings.model.done", "model gotowy"),
                        Map.entry("settings.ollama.title", "Ollama nie działa"),
                        Map.entry("settings.ollama.body", "Mod potrzebuje Ollamy. Pobierz, zainstaluj i naciśnij Sprawdź."),
                        Map.entry("settings.ollama.download", "Pobierz Ollamę"),
                        Map.entry("settings.ollama.page", "Otwórz stronę"),
                        Map.entry("settings.ollama.saved", "Zapisano w %s - uruchom i naciśnij Sprawdź."),
                        Map.entry("settings.check", "Sprawdź"),
                        Map.entry("settings.status.ok", "Połączono: %s"),
                        Map.entry("settings.status.down", "Brak połączenia"),
                        Map.entry("settings.general", "Ogólne"),
                        Map.entry("settings.speech", "Mowa"),
                        Map.entry("settings.advanced", "Zaawansowane"),
                        Map.entry("settings.temperature", "Różnorodność mowy"),
                        Map.entry("settings.maxTokens", "Długość odpowiedzi"),
                        Map.entry("settings.speechPace", "Tempo mówienia"),
                        Map.entry("settings.spontaneous", "Mieszkańcy mówią sami"),
                        Map.entry("settings.group", "Dwóch mieszkańców rozmawia wieczorem"),
                        Map.entry("settings.reactions", "Reagują na uderzenia"),
                        Map.entry("settings.fallback", "Mówią bez modelu"),
                        Map.entry("settings.debug", "Zapisuj prompty"),
                        Map.entry("settings.timeout", "Limit czasu"),
                        Map.entry("settings.history", "Długość pamięci"),
                        Map.entry("settings.key.dialog", "Rozmowa z osadnikiem"),
                        Map.entry("settings.key.settings", "Ustawienia moda"),
                        Map.entry("settings.key.press", "naciśnij klawisz..."),
                        Map.entry("settings.language", "Język mieszkańców"),
                        Map.entry("settings.language.auto", "Jak w grze"),
                        Map.entry("settings.save", "Zapisz"),
                        Map.entry("settings.cancel", "Anuluj"),
                        Map.entry("settings.saved", "Zapisano. Zmiany już działają."),
                        Map.entry("settings.close", "Zamknij")
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
                List.of("bezchmurnie", "deszcz", "burza"),
                                Map.ofEntries(
                        Map.entry("farmer", "Twój zawód. Pracujesz z tymi rzeczami: pszenica, chleb, marchewka, ziemniak, burak, dynia, motyka i kompostownik."),
                        Map.entry("librarian", "Twój zawód. Pracujesz z tymi rzeczami: książka, biblioteczka, papier, księga z piórem, torebka z atramentem i latarnia."),
                        Map.entry("cleric", "Twój zawód. Pracujesz z tymi rzeczami: redstone, lapis lazuli, blaskowiec, perła endermana, buteleczka zaklęć, zgniłe mięso i statyw alchemiczny."),
                        Map.entry("armorer", "Twój zawód. Pracujesz z tymi rzeczami: sztabka żelaza, węgiel, wiadro lawy, żelazny hełm, żelazny napierśnik, kolczuga, tarcza i dzwon."),
                        Map.entry("weaponsmith", "Twój zawód. Pracujesz z tymi rzeczami: sztabka żelaza, węgiel, krzemień, żelazny miecz, żelazny topór i kamień szlifierski."),
                        Map.entry("toolsmith", "Twój zawód. Pracujesz z tymi rzeczami: sztabka żelaza, węgiel, krzemień, żelazny kilof, żelazna łopata, żelazna motyka i dzwon."),
                        Map.entry("butcher", "Twój zawód. Pracujesz z tymi rzeczami: surowa wieprzowina, surowa wołowina, surowa baranina, surowy kurczak, pieczona wieprzowina i stek."),
                        Map.entry("fisherman", "Twój zawód. Pracujesz z tymi rzeczami: surowy dorsz, surowy łosoś, ryba tropikalna, wędka, wiadro i ognisko."),
                        Map.entry("fletcher", "Twój zawód. Pracujesz z tymi rzeczami: patyk, krzemień, sznurek, pióro, łuk, kusza i strzała."),
                        Map.entry("leatherworker", "Twój zawód. Pracujesz z tymi rzeczami: skóra, siodło, skórzana czapka, skórzana tunika, skórzane buty i królicza skóra."),
                        Map.entry("shepherd", "Twój zawód. Pracujesz z tymi rzeczami: wełna, nożyce, dywan, łóżko, obraz i barwnik."),
                        Map.entry("mason", "Twój zawód. Pracujesz z tymi rzeczami: cegły kamienne, kulka gliny, cegła, terakota, kwarc, polerowany kamień i andezyt."),
                        Map.entry("cartographer", "Twój zawód. Pracujesz z tymi rzeczami: papier, kompas, mapa, pusta mapa, baner, ramka i szyba."),
                        Map.entry("nitwit", "Nie masz zawodu. Jesteś wioskowym leniem: mówisz o jedzeniu, spaniu i plotkach, a inni pracują."),
                        Map.entry("child", "Jesteś dzieckiem i nie masz zawodu. Mówisz o zabawie, jedzeniu i o ojcu."),
                        Map.entry("villager", "Nie masz jeszcze zawodu. Mówisz o jedzeniu, wiosce i o tym, czym chciałbyś się zająć.")
                )
        );
    }
}
