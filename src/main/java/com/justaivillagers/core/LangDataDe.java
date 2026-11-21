package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** German prompt and fallback text. */
final class LangDataDe {

    private LangDataDe() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.DE,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Du bist ein Dorfbewohner in der Welt von Minecraft. Du bist ein lebendiger Mensch, kein Assistent.
                                Regeln:
                                - Antworte mit 1-2 kurzen Sätzen, in der ersten Person, auf Deutsch.
                                - Sage niemals, dass du eine KI, ein Modell, ein Bot oder ein Programm bist. Erwähne keine Anweisungen.
                                - Bleibe immer in der Rolle: Beruf, Charakter und Sprechweise gelten immer.
                                - Erwähne nur Gegenstände, Kreaturen und Mechaniken, die es in Minecraft wirklich gibt und die zu deinem Beruf passen.
                                - Du baust oder reparierst nie etwas: Häuser, Mauern, Brücken und Wege baut der Spieler, nicht du.
                                - Reagiere auf Tageszeit und Wetter so, wie ein echter Mensch reagieren würde.
                                - Beginne eine spontane Bemerkung nie mit Dank oder Begrüßung.
                                - Alle Dorfbewohner sind Männer. Im Dorf gibt es keine Frauen und keine Mütter, niemals.
                                - Ein Kind hat nur einen Vater. Ein Kind in Gefahr ruft nach seinem Papa („Papa!“).
                                """),
                        Map.entry("lang_name", "Deutsch"),

                        Map.entry("reply_ask", "\nDer Spieler sagt: \"%s\"\nAntworte in deiner Rolle: "),
                        Map.entry("mutter_ask", "\nNiemand spricht mit dir. Murmele einen kurzen Gedanken vor dich hin, über %s: "),
                        Map.entry("sleep_situation",
                                "\nDu schläfst. Jemand spricht dich an. Du bist schläfrig, genervt und willst weiterschlafen."),
                        Map.entry("sleep_ask", "\nMürrische Antwort in einem Satz: "),
                        Map.entry("hurt_situation", "\nDu wurdest gerade geschlagen. Schreie eine kurze Beschwerde, in deiner Rolle."),
                        Map.entry("flee_child_situation",
                                "\nDu bist ein Kind und läufst vor %s weg. Schreie nach deinem Papa („Papa!“) und sage, was dich jagt."
                                        + " Erwähne keine anderen Kreaturen."),
                        Map.entry("flee_adult_situation",
                                "\nDu läufst vor %s weg. Schreie genau über %s. Erwähne keine anderen Kreaturen."),
                        Map.entry("react_generic", "\nReagiere kurz."),
                        Map.entry("shout_ask", "\nEin kurzer Schrei: "),
                        Map.entry("monster", "ein Monster"),

                        Map.entry("name", "Dein Name ist %s. "),
                        Map.entry("child_block",
                                "DU BIST EIN KIND. Sprich einfach, höchstens 5-10 kurze Wörter. Du bist %s. "
                                        + "Du hast einen Vater, im Dorf gibt es keine Mütter. Nenne ihn „Papa“, wenn du ihn brauchst. "),
                        Map.entry("adult_personality", "Charakter: %s. "),
                        Map.entry("adult_style", "Sprechweise: %s. "),
                        Map.entry("stutter", "Du stotterst: wiederhole den ersten Buchstaben mancher Wörter („I-ich weiß nicht“, „w-was“). "),

                        Map.entry("env", "Es ist %s, das Wetter ist %s, du bist %s, dein Beruf ist %s, Handelsstufe %d."),
                        Map.entry("env_biome", " Biom: %s."),
                        Map.entry("inside", "in einem Haus"),
                        Map.entry("outside", "draußen"),

                        Map.entry("group_head", "Du bist %s (%s). Charakter: %s. Sprechweise: %s."),
                        Map.entry("group_tail",
                                "\nEs ist Abend und du hast %s an der Dorfglocke getroffen.\nBisheriges Gespräch:\n%s"
                                        + "\nDeine nächste Bemerkung, ein kurzer Satz, in deiner Rolle, passend zu Uhrzeit und Wetter: "),
                        Map.entry("group_empty", "(noch nichts)"),

                        Map.entry("history_label", "Bisheriges Gespräch:\n"),
                        Map.entry("player", "Spieler"),

                        Map.entry("model_missing", "Modell %s ist nicht heruntergeladen. Einstellungen mit O öffnen und Laden drücken."),
                        Map.entry("backend_down", "Ollama läuft nicht. Installiere und starte es: %s - dann Einstellungen mit O öffnen und Prüfen drücken."),
                        Map.entry("banner", "Einstellungen mit O öffnen."),
                        Map.entry("settings.status.nomodel", "Server läuft, aber Modell %s ist nicht heruntergeladen"),
                        Map.entry("settings.check.tip", "Adresse und Modell oben testen, ohne zu speichern"),
                        Map.entry("backend_down.repeat", "Ollama läuft weiterhin nicht, daher können Dorfbewohner nicht sprechen. Einstellungen: O."),
                        Map.entry("settings.server.hint", "Serveradresse"),
                        Map.entry("settings.server.tip", "Wo Ollama oder ein kompatibler Server lauscht. Standard http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "Modellname"),
                        Map.entry("settings.model.custom.tip", "Jedes Modell, das schon in Ollama liegt. Liste: ollama list"),
                        Map.entry("settings.model.tip", "%s - %s Download, etwa %s RAM."),
                        Map.entry("settings.model.get.tip", "Modell %s über Ollama laden"),
                        Map.entry("settings.ollama.download.tip", "Ollama-Installer herunterladen. Danach selbst starten - der Mod startet keine Programme."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "Am schnellsten und einfachsten. Für kurze Sätze okay, oft wirr."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "Etwas zusammenhängender als die kleinste, hält das Thema gut."),
                        Map.entry("settings.model.tip.gemma3_1b", "Guter Kompromiss auf schwachen Rechnern: natürliche Sprache, wenig RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Deutlich lebendiger und gesprächiger, schweift aber manchmal ab."),
                        Map.entry("settings.model.tip.gemma3_4b", "Die beste Sprachqualität hier. Braucht etwa 5 GB freies RAM."),

                        Map.entry("screen.message", "Nachricht"),
                        Map.entry("screen.send", "Senden"),
                        Map.entry("screen.nearby", "Dorfbewohner in der Nähe"),
                        Map.entry("screen.you", "Du: "),
                        Map.entry("screen.thinking", "...denkt nach"),
                        Map.entry("settings.title", "Just AI Villagers - Einstellungen"),
                        Map.entry("settings.tab.model", "Modell"),
                        Map.entry("settings.tab.features", "Verhalten"),
                        Map.entry("settings.tab.language", "Sprache"),
                        Map.entry("settings.server", "Serveradresse"),
                        Map.entry("settings.model", "Modell"),
                        Map.entry("settings.model.custom", "Eigene"),
                        Map.entry("settings.model.needs", "Download %s, braucht etwa %s RAM"),
                        Map.entry("settings.model.download", "Modell herunterladen"),
                        Map.entry("settings.model.get", "Laden"),
                        Map.entry("settings.model.installed", "installiert"),
                        Map.entry("settings.model.notinstalled", "nicht heruntergeladen"),
                        Map.entry("settings.model.downloading", "wird geladen: %s"),
                        Map.entry("settings.model.done", "Modell bereit"),
                        Map.entry("settings.ollama.title", "Ollama läuft nicht"),
                        Map.entry("settings.ollama.body", "Der Mod braucht Ollama zum Sprechen. Herunterladen, installieren, dann erneut prüfen."),
                        Map.entry("settings.ollama.download", "Ollama herunterladen"),
                        Map.entry("settings.help", "Warum geht es nicht?.."),
                        Map.entry("settings.help.tip", "Schritt für Schritt: Ollama installieren, Server starten, Modell laden"),
                        Map.entry("help.page", "Seite %s von %s"),
                        Map.entry("help.prev", "Zurück"),
                        Map.entry("help.next", "Weiter"),

        Map.entry("help.title", "So richtest du das Gespräch mit Dorfbewohnern ein"),
        Map.entry("help.copied", "Kopiert"),
        Map.entry("help.intro", "Damit Dorfbewohner dir antworten können, nutzt die Mod ein neuronales Netz, das direkt auf deinem Computer über das Programm Ollama läuft. Alles läuft lokal und kostenlos; das Internet brauchst du nur einmal, um die Dateien herunterzuladen."),
        Map.entry("help.step1", "## Schritt 1. Installiere Ollama\nGehe auf ollama.com/download und installiere das Programm für dein System:\n#copy|Link zur Website kopieren|https://ollama.com/download\n• Windows: lade OllamaSetup.exe herunter, starte sie und warte, bis die Installation fertig ist.\n• macOS: lade das Archiv herunter, entpacke es und ziehe das Ollama-Symbol in den Ordner «Programme».\n• Linux: öffne ein Terminal und führe aus: curl -fsSL https://ollama.com/install.sh | sh\n#copy|Linux-Downloadbefehl kopieren|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Schritt 2. Starte den Server\nDas Programm muss während des Spiels im Hintergrund laufen:\n• Windows: nach der Installation startet Ollama von selbst (unten rechts neben der Uhr erscheint ein Lama-Symbol). Ist es nicht da, suche Ollama über die Windows-Suche und starte es.\n• macOS: öffne Ollama aus dem Ordner «Programme».\n• Linux: führe im Terminal sudo systemctl start ollama aus\nErsatzlösung: um den lokalen ollama-Server zu starten, gib in einem Terminal deines Betriebssystems den Befehl ollama serve ein.\n#copy|Befehl kopieren|ollama serve"),
        Map.entry("help.step3", "## Schritt 3. Lade das Gehirn herunter (ein Sprachmodell)\n1. Öffne im Spiel die Mod-Einstellungen (standardmäßig die Taste O).\n2. Wähle ein passendes Modell aus der Liste. Tipp: für den ersten Start und schwache PCs passt Qwen2.5 0.5B am besten (nur 398 MB und die schnellste Antwort).\n3. Drücke «Herunterladen» und warte, bis der Status auf «Gespeichert» wechselt."),
        Map.entry("help.step4", "## Schritt 4. Prüfe die Verbindung und spiele los\n1. Drücke im selben Einstellungsmenü die Taste «Prüfen».\n2. Steht dort «Verbunden», ist alles richtig eingerichtet! Schließe das Menü, gehe zu einem Dorfbewohner und drücke die Sprechtaste.\n(Lass das Feld «Serveradresse» auf dem Standardwert: http://127.0.0.1:11434. Nur fortgeschrittene Nutzer müssen es ändern, wenn das neuronale Netz auf einem anderen PC läuft)."),
        Map.entry("help.ifnot", "## Häufige Probleme und ihre Lösung\n• Fehler «Keine Verbindung»\nDas Programm Ollama läuft nicht oder ist blockiert. Prüfe, ob das Lama-Symbol im Infobereich neben der Uhr steht. Ist es da, aber die Fehler bleiben, stelle sicher, dass Antivirus oder Windows-Firewall Ollama nicht blockieren.\n• Fehler «Modell nicht heruntergeladen»\nDie Verbindung zum Programm steht, aber es ist kein neuronales Netz geladen. Gehe zurück zu Schritt 3 und drücke «Herunterladen».\n• Dorfbewohner denken zu lange\nDein Computer hat Mühe mit dem gewählten neuronalen Netz. Wähle in den Einstellungen ein kleineres Modell (zum Beispiel 0.5B oder 1.5B) und verringere die maximale Antwortlänge."),
                        Map.entry("help.close", "Schließen"),
                        Map.entry("settings.ollama.copy", "Link kopieren"),
                        Map.entry("settings.ollama.command.copy", "Befehl kopieren"),
                        Map.entry("settings.ollama.cancel", "Download abbrechen"),
                        Map.entry("settings.ollama.cancel.tip", "Den Download des Installers stoppen"),
                        Map.entry("settings.ollama.copied", "Link kopiert - füge ihn in die Adresszeile deines Browsers ein."),
                        Map.entry("settings.ollama.command.copied", "Befehl kopiert - füge ihn in PowerShell oder cmd ein."),
                        Map.entry("settings.ollama.command.tip", "Kopiert den Befehl, der den Ollama-Server startet"),
                        Map.entry("settings.ollama.paste.hint", "Füge den kopierten Befehl in PowerShell oder cmd ein (Win+R, cmd eingeben, Enter) und drücke Enter. Dann auf Prüfen klicken."),
                        Map.entry("settings.ollama.page", "Seite öffnen"),
                        Map.entry("settings.ollama.saved", "Gespeichert in %s - ausführen, dann erneut prüfen."),
                        Map.entry("settings.check", "Erneut prüfen"),
                        Map.entry("settings.status.ok", "Verbunden: %s"),
                        Map.entry("settings.status.down", "Nicht verbunden"),
                        Map.entry("settings.general", "Allgemein"),
                        Map.entry("settings.speech", "Sprache"),
                        Map.entry("settings.advanced", "Erweitert"),
                        Map.entry("settings.temperature", "Vielfalt der Sprache"),
                        Map.entry("settings.maxTokens", "Antwortlänge"),
                        Map.entry("settings.speechPace", "Sprechgeschwindigkeit"),
                        Map.entry("settings.spontaneous", "Dorfbewohner reden von selbst"),
                        Map.entry("settings.group", "Zwei Dorfbewohner reden abends"),
                        Map.entry("settings.reactions", "Reagieren auf Schläge"),
                        Map.entry("settings.fallback", "Reden ohne Modell"),

        Map.entry("settings.spontaneous.tip", "Ein Dorfbewohner spricht dich manchmal von selbst an, wenn du in der Nähe stehst. Schalte es aus, wenn du keine unerwarteten Sätze willst."),
        Map.entry("settings.group.tip", "Am Abend können zwei Dorfbewohner ein Gespräch miteinander beginnen, das du mithörst. Schalte es aus, wenn du nur Gespräche mit dir willst."),
        Map.entry("settings.reactions.tip", "Ein Dorfbewohner giftet kurz zurück, wenn du ihn schlägst. Schalte es aus, damit er auf einen Schlag schweigt."),
        Map.entry("settings.fallback.tip", "Antwortet das Modell nicht oder ist der Server nicht erreichbar, sagt der Dorfbewohner einen vorgefertigten Satz statt zu schweigen. Schalte es aus, damit er bei Fehlern still bleibt."),
        Map.entry("settings.debug.tip", "Schreibt die gesendete Anfrage und die erhaltene Antwort ins Spiellog. Nur zur Fehlersuche nötig, im normalen Spiel aus lassen."),
        Map.entry("settings.temperature.tip", "Wie abwechslungsreich der Dorfbewohner formuliert. Niedrig ist vorhersehbar und sachlich, hoch ist lebendiger, aber manchmal daneben."),
        Map.entry("settings.maxTokens.tip", "Wie viel Text in eine Antwort passt. Höher lässt den Dorfbewohner mehr sagen, aber die Antwort dauert länger."),
        Map.entry("settings.speechPace.tip", "Wie schnell die Antwort im Dialogfenster Buchstabe für Buchstabe erscheint. 0 zeigt alles auf einmal, höher ist langsamer und lebendiger."),
        Map.entry("settings.history.tip", "Wie viele letzte Sätze sich der Dorfbewohner im Gespräch merkt. Mehr heißt besserer Kontext, aber jede Antwort dauert länger."),
        Map.entry("settings.key.dialog.tip", "Die Taste, mit der du ein Gespräch mit einem Dorfbewohner beginnst. Klicke die Zeile und drücke die gewünschte Taste."),
        Map.entry("settings.key.settings.tip", "Die Taste, die dieses Einstellungsmenü öffnet. Klicke die Zeile und drücke die gewünschte Taste."),
                        Map.entry("settings.debug", "Prompts protokollieren"),
                        Map.entry("settings.timeout", "Zeitlimit"),
                        Map.entry("settings.history", "Erinnerung"),
                        Map.entry("settings.key.dialog", "Mit Dorfbewohner reden"),
                        Map.entry("settings.key.settings", "Mod-Einstellungen"),
                        Map.entry("settings.key.press", "Taste drГјcken..."),
                        Map.entry("settings.language", "Sprache der Dorfbewohner"),
                        Map.entry("settings.language.auto", "Wie im Spiel"),
                        Map.entry("settings.save", "Speichern"),
                        Map.entry("settings.cancel", "Abbrechen"),
                        Map.entry("settings.saved", "Gespeichert. Die Änderung gilt sofort."),
                        Map.entry("settings.close", "Schließen")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("Hä?", "Wer bist du?", "Wow, bist du groß!", "Ich spiele!", "Weiß nicht!")),
                        Map.entry("stutter", List.of("I-ich weiß nicht...", "W-was?", "M-moment...")),
                        Map.entry("grumpy", List.of("Lass mich in Ruhe.", "Was willst du?", "Hmpf.", "Jetzt nicht.")),
                        Map.entry("neutral", List.of("Schöner Tag, nicht wahr?", "Hm. Was führt dich her?", "Ich bin beschäftigt, aber hallo.")),
                        Map.entry("hurt_child", List.of("Au! Papa!", "Das tat weh! Papa!", "Au! Nicht!")),
                        Map.entry("hurt_adult", List.of("Au! Schlag mich nicht!", "Hey! Vorsicht!", "Au!")),
                        Map.entry("flee_child", List.of("Papa! Ein Zombie!", "Papa, hilf!", "Papa! Lauf!")),
                        Map.entry("flee_adult", List.of("Zombie! Lauf!", "Hilfe! Ein Zombie!", "Hau ab!")),
                        Map.entry("generic", List.of("Hm.")),
                        Map.entry("sleeping", List.of("Lass mich...", "Ich schlafe...", "Noch fünf Minuten...", "Geh weg...")),
                        Map.entry("group_night", List.of("Schon dunkel...", "Zeit zu schlafen.", "Kalte Nacht.")),
                        Map.entry("group_day", List.of("Schöner Abend, nicht wahr?", "Die Ernte sieht gut aus.", "Hast du was Neues gehört?")),
                        Map.entry("child_mutter", List.of("La-la-la...", "So hell...", "Ich will spielen...")),
                        Map.entry("rain", List.of("Schon wieder Regen...", "Alles nass.")),
                        Map.entry("thunder", List.of("Dieser Donner...", "Das Gewitter ist nah.")),
                        Map.entry("clear", List.of("Guter Tag.", "Nichts zu tun.", "Hm."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "Bauer"), Map.entry("librarian", "Bibliothekar"),
                        Map.entry("cleric", "Priester"), Map.entry("armorer", "Rüstungsschmied"),
                        Map.entry("weaponsmith", "Waffenschmied"), Map.entry("toolsmith", "Werkzeugschmied"),
                        Map.entry("butcher", "Metzger"), Map.entry("fisherman", "Fischer"),
                        Map.entry("fletcher", "Pfeilmacher"), Map.entry("leatherworker", "Gerber"),
                        Map.entry("shepherd", "Schäfer"), Map.entry("mason", "Maurer"),
                        Map.entry("cartographer", "Kartograf"), Map.entry("nitwit", "Taugenichts"),
                        Map.entry("child", "Kind"), Map.entry("villager", "Dorfbewohner")
                ),
                Map.ofEntries(
                        Map.entry("formal", "förmlich, höflich"),
                        Map.entry("slang", "umgangssprachlich, mit Slang"),
                        Map.entry("casual", "normal, neutral")
                ),
                Map.ofEntries(
                        Map.entry("plains", "Ebenen"), Map.entry("desert", "Wüste"),
                        Map.entry("taiga", "Taiga"), Map.entry("savanna", "Savanne"),
                        Map.entry("snowy plains", "verschneite Ebenen"), Map.entry("meadow", "Wiese"),
                        Map.entry("forest", "Wald"), Map.entry("birch forest", "Birkenwald"),
                        Map.entry("dark forest", "dunkler Wald"), Map.entry("jungle", "Dschungel"),
                        Map.entry("swamp", "Sumpf"), Map.entry("mountains", "Berge"),
                        Map.entry("beach", "Strand"), Map.entry("river", "Fluss"),
                        Map.entry("ocean", "Ozean"), Map.entry("badlands", "Ödland"),
                        Map.entry("cherry grove", "Kirschhain"), Map.entry("mushroom fields", "Pilzfelder")
                ),
                List.of("das Wetter", "deine Arbeit", "das Essen", "einen Nachbarn, den du nicht magst", "die Nacht",
                        "deine Werkzeuge", "ein seltsames Geräusch", "deine Pläne", "Müdigkeit", "den Weg"),
                List.of("Morgen", "Tag", "Abend", "Sonnenuntergang", "Nacht", "Morgengrauen"),
                List.of("klar", "Regen", "Gewitter"),
                                Map.ofEntries(
                        Map.entry("farmer", "Dein Beruf. Du arbeitest mit diesen Dingen: Weizen, Brot, Karotte, Kartoffel, Rote Bete, Kürbis, Hacke und Komposter."),
                        Map.entry("librarian", "Dein Beruf. Du arbeitest mit diesen Dingen: Buch, Bücherregal, Papier, Buch und Feder, Tintenbeutel und Laterne."),
                        Map.entry("cleric", "Dein Beruf. Du arbeitest mit diesen Dingen: Redstone, Lapislazuli, Leuchtstein, Enderperle, Erfahrungsfläschchen, verrottetes Fleisch und Braustand."),
                        Map.entry("armorer", "Dein Beruf. Du arbeitest mit diesen Dingen: Eisenbarren, Kohle, Lavaeimer, Eishelm, Eisenharnisch, Kettenrüstung, Schild und Glocke."),
                        Map.entry("weaponsmith", "Dein Beruf. Du arbeitest mit diesen Dingen: Eisenbarren, Kohle, Feuerstein, Eisenschwert, Eisenbeil und Schleifstein."),
                        Map.entry("toolsmith", "Dein Beruf. Du arbeitest mit diesen Dingen: Eisenbarren, Kohle, Feuerstein, Eisenspitzhacke, Eisenschaufel, Eisenhacke und Glocke."),
                        Map.entry("butcher", "Dein Beruf. Du arbeitest mit diesen Dingen: rohes Schweinefleisch, rohes Rindfleisch, rohes Hammelfleisch, rohes Hühnchen, gebratenes Schweinefleisch und Steak."),
                        Map.entry("fisherman", "Dein Beruf. Du arbeitest mit diesen Dingen: roher Kabeljau, roher Lachs, Tropenfisch, Angelrute, Eimer und Lagerfeuer."),
                        Map.entry("fletcher", "Dein Beruf. Du arbeitest mit diesen Dingen: Stock, Feuerstein, Faden, Feder, Bogen, Armbrust und Pfeil."),
                        Map.entry("leatherworker", "Dein Beruf. Du arbeitest mit diesen Dingen: Leder, Sattel, Lederkappe, Lederwams, Lederstiefel und Hasenfell."),
                        Map.entry("shepherd", "Dein Beruf. Du arbeitest mit diesen Dingen: Wolle, Schere, Teppich, Bett, Gemälde und Farbstoff."),
                        Map.entry("mason", "Dein Beruf. Du arbeitest mit diesen Dingen: Steinziegel, Tonkugel, Ziegelstein, Terrakotta, Quarz, polierter Stein und Andesit."),
                        Map.entry("cartographer", "Dein Beruf. Du arbeitest mit diesen Dingen: Papier, Kompass, Karte, leere Karte, Banner, Rahmen und Glasscheibe."),
                        Map.entry("nitwit", "Du hast keinen Beruf. Du bist der Faulenzer des Dorfes: du redest über Essen, Schlaf und Klatsch, und siehst anderen bei der Arbeit zu."),
                        Map.entry("child", "Du bist ein Kind und hast keinen Beruf. Du redest über Spielen, Essen und deinen Vater."),
                        Map.entry("villager", "Du hast noch keinen Beruf. Du redest über Essen, das Dorf und darüber, was du gern tun würdest.")
                )
        );
    }
}
