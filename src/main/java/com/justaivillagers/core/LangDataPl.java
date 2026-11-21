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
                        Map.entry("settings.help", "Dlaczego nie działa?.."),
                        Map.entry("settings.help.tip", "Instrukcja krok po kroku: instalacja Ollamy, uruchomienie serwera, pobranie modelu"),
                        Map.entry("help.page", "Strona %s z %s"),
                        Map.entry("help.prev", "Wstecz"),
                        Map.entry("help.next", "Dalej"),

        Map.entry("help.title", "Jak skonfigurować rozmowę z mieszkańcami"),
        Map.entry("help.copied", "Skopiowano"),
        Map.entry("help.intro", "Aby mieszkańcy mogli ci odpowiadać, mod korzysta z sieci neuronowej uruchamianej bezpośrednio na twoim komputerze przez program Ollama. Wszystko działa lokalnie i bezpłatnie; internet potrzebny jest tylko raz, do pobrania plików."),
        Map.entry("help.step1", "## Krok 1. Zainstaluj program Ollama\nWejdź na ollama.com/download i zainstaluj program dla swojego systemu:\n#copy|skopiuj link do strony|https://ollama.com/download\n• Windows: pobierz plik OllamaSetup.exe, uruchom go i poczekaj na koniec instalacji.\n• macOS: pobierz archiwum, rozpakuj je i przeciągnij ikonę Ollama do folderu „Programy”.\n• Linux: otwórz terminal i wykonaj polecenie: curl -fsSL https://ollama.com/install.sh | sh\n#copy|skopiuj polecenie pobierania dla Linuksa|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Krok 2. Uruchom serwer\nProgram musi działać w tle podczas gry:\n• Windows: po instalacji Ollama uruchamia się sama (w prawym dolnym rogu, obok zegara, pojawia się ikona lamy). Jeśli jej nie ma, znajdź Ollama w wyszukiwaniu Windows i uruchom go.\n• macOS: otwórz Ollama z folderu „Programy”.\n• Linux: wpisz w terminalu sudo systemctl start ollama\nRozwiązanie zapasowe: aby uruchomić lokalny serwer ollama, wpisz w terminalu swojego systemu polecenie ollama serve.\n#copy|skopiuj polecenie|ollama serve"),
        Map.entry("help.step3", "## Krok 3. Pobierz „mózg” (model językowy)\n1. W grze otwórz menu ustawień moda (domyślnie klawisz O).\n2. Wybierz odpowiedni model z listy. Wskazówka: na pierwsze uruchomienie i słabsze komputery najlepiej nadaje się Qwen2.5 0.5B (waży tylko 398 MB i odpowiada najszybciej).\n3. Naciśnij „Pobierz” i poczekaj, aż status zmieni się na „Zapisano”."),
        Map.entry("help.step4", "## Krok 4. Sprawdź połączenie i zacznij grę\n1. W tym samym menu ustawień naciśnij przycisk „Sprawdź”.\n2. Jeśli pojawi się status „Połączono”, wszystko jest skonfigurowane poprawnie! Zamknij menu, podejdź do dowolnego mieszkańca i naciśnij klawisz rozmowy.\n(Pole „Adres serwera” zostaw domyślnie: http://127.0.0.1:11434. Zmieniają je tylko zaawansowani użytkownicy, jeśli sieć neuronowa działa na innym komputerze)."),
        Map.entry("help.ifnot", "## Częste problemy i ich rozwiązania\n• Błąd „Brak połączenia”\nProgram Ollama nie jest uruchomiony lub jest zablokowany. Sprawdź, czy ikona lamy jest w zasobniku systemowym obok zegara. Jeśli jest, a błędy nie znikają, upewnij się, że antywirus lub zapora Windows nie blokują Ollamy.\n• Błąd „Model nie został pobrany”\nPołączenie z programem jest, ale nie wczytano sieci neuronowej. Wróć do Kroku 3 i naciśnij „Pobierz”.\n• Mieszkańcy myślą zbyt długo\nTwój komputer ma trudności z wybraną siecią neuronową. Wybierz w ustawieniach mniejszy model (na przykład 0.5B lub 1.5B) i zmniejsz maksymalną długość odpowiedzi."),
                        Map.entry("help.close", "Zamknij"),
                        Map.entry("settings.ollama.copy", "Kopiuj link"),
                        Map.entry("settings.ollama.command.copy", "Kopiuj polecenie"),
                        Map.entry("settings.ollama.cancel", "Anuluj pobieranie"),
                        Map.entry("settings.ollama.cancel.tip", "Zatrzymaj pobieranie instalatora"),
                        Map.entry("settings.ollama.copied", "Link skopiowany - wklej go w pasku adresu przeglądarki."),
                        Map.entry("settings.ollama.command.copied", "Polecenie skopiowane - wklej je w PowerShell lub cmd."),
                        Map.entry("settings.ollama.command.tip", "Kopiuje polecenie uruchamiające serwer Ollama"),
                        Map.entry("settings.ollama.paste.hint", "Wklej skopiowane polecenie w PowerShell lub cmd (Win+R, wpisz cmd, Enter) i naciśnij Enter. Potem kliknij Sprawdź."),
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

        Map.entry("settings.spontaneous.tip", "Mieszkaniec czasem sam odezwie się do ciebie, gdy stoisz obok. Wyłącz, jeśli nie chcesz niespodziewanych tekstów."),
        Map.entry("settings.group.tip", "Wieczorem dwóch mieszkańców może zacząć rozmawiać ze sobą, a ty to słyszysz. Wyłącz, jeśli chcesz tylko rozmów z tobą."),
        Map.entry("settings.reactions.tip", "Mieszkaniec burknie krótko, gdy go uderzysz. Wyłącz, aby na cios milczał."),
        Map.entry("settings.fallback.tip", "Jeśli model nie odpowie lub serwer jest niedostępny, mieszkaniec powie gotowe zdanie zamiast milczeć. Wyłącz, aby przy błędzie milczał."),
        Map.entry("settings.debug.tip", "Zapisuje w logu gry wysłane zapytanie i otrzymaną odpowiedź. Przydatne tylko do szukania błędów; w normalnej grze zostaw wyłączone."),
        Map.entry("settings.temperature.tip", "Jak bardzo mieszkaniec urozmaica słowa. Nisko — przewidywalnie i na temat, wysoko — żywiej, ale czasem nie w punkt."),
        Map.entry("settings.maxTokens.tip", "Ile tekstu mieści się w jednej odpowiedzi. Wyżej — mieszkaniec powie więcej, ale odpowiedź potrwa dłużej."),
        Map.entry("settings.speechPace.tip", "Jak szybko odpowiedź pojawia się w oknie rozmowy, litera po literze. 0 pokazuje całość od razu, wyżej — wolniej i żywiej."),
        Map.entry("settings.history.tip", "Ile ostatnich kwestii mieszkaniec pamięta w rozmowie. Więcej — lepszy kontekst, ale każda odpowiedź trwa dłużej."),
        Map.entry("settings.key.dialog.tip", "Klawisz, którym zaczynasz rozmowę z mieszkańcem. Kliknij wiersz i naciśnij wybrany klawisz."),
        Map.entry("settings.key.settings.tip", "Klawisz, który otwiera to menu ustawień. Kliknij wiersz i naciśnij wybrany klawisz."),
                        Map.entry("settings.debug", "Zapisuj prompty"),
                        Map.entry("settings.timeout", "Limit czasu"),
                        Map.entry("settings.history", "Długość pamięci"),
                        Map.entry("settings.key.dialog", "Rozmowa z osadnikiem"),
                        Map.entry("settings.key.settings", "Ustawienia moda"),
                        Map.entry("settings.key.press", "naciЕ›nij klawisz..."),
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
