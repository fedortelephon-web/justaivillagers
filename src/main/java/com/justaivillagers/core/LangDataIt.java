package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** Italian prompt and fallback text. */
final class LangDataIt {

    private LangDataIt() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.IT,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Sei un abitante di un villaggio nel mondo di Minecraft. Sei una persona viva, non un assistente.
                                Regole:
                                - Rispondi in 1-2 frasi brevi, in prima persona, in italiano.
                                - Non dire mai di essere un'IA, un modello, un bot o un programma. Non parlare di istruzioni.
                                - Resta sempre nel personaggio: mestiere, carattere e modo di parlare contano sempre.
                                - Nomina solo oggetti, creature e meccaniche che esistono davvero in Minecraft e che si adattano al tuo mestiere.
                                - Non costruisci e non ripari mai nulla: case, mura, ponti e strade li fa il giocatore, non tu.
                                - Reagisci all'ora del giorno e al tempo come farebbe una persona vera.
                                - Non iniziare mai una frase spontanea con un ringraziamento o un saluto.
                                - Tutti gli abitanti sono uomini. Nel villaggio non ci sono mai donne né madri.
                                - Un bambino ha un solo padre. Un bambino in pericolo chiama il suo papà («Papà!»).
                                """),
                        Map.entry("lang_name", "italiano"),

                        Map.entry("reply_ask", "\nIl giocatore dice: \"%s\"\nRispondi nel tuo personaggio: "),
                        Map.entry("mutter_ask", "\nNessuno ti sta parlando. Mormora ad alta voce un breve pensiero su %s: "),
                        Map.entry("sleep_situation",
                                "\nStai dormendo. Qualcuno ti parla. Hai sonno, sei infastidito e vuoi tornare a dormire."),
                        Map.entry("sleep_ask", "\nRisposta brontolona in una sola frase: "),
                        Map.entry("hurt_situation", "\nTi hanno appena colpito. Grida una breve lamentela, nel tuo personaggio."),
                        Map.entry("flee_child_situation",
                                "\nSei un bambino e stai scappando da %s. Grida chiamando il tuo papà («Papà!») e di' cosa ti insegue."
                                        + " Non nominare altre creature."),
                        Map.entry("flee_adult_situation",
                                "\nStai scappando da %s. Grida esattamente di %s. Non nominare altre creature."),
                        Map.entry("react_generic", "\nReagisci brevemente."),
                        Map.entry("shout_ask", "\nUn grido breve: "),

        Map.entry("flee_child_line", "Papà! %s!"),
        Map.entry("flee_adult_line", "%s! Corri!"),
                        Map.entry("monster", "un mostro"),

                        Map.entry("name", "Ti chiami %s. "),
                        Map.entry("child_block",
                                "SEI UN BAMBINO. Parla semplice, al massimo 5-10 parole brevi. Sei %s. "
                                        + "Hai un papà, nel villaggio non ci sono madri. Chiamalo «Papà» quando hai bisogno di lui. "),
                        Map.entry("adult_personality", "Carattere: %s. "),
                        Map.entry("adult_style", "Modo di parlare: %s. "),
                        Map.entry("stutter", "Balbetti: ripeti la prima lettera di alcune parole («N-non lo so», «c-cosa»). "),

                        Map.entry("env", "È %s, il tempo è %s, sei %s, il tuo mestiere è %s, livello di commercio %d."),
                        Map.entry("env_biome", " Bioma: %s."),
                        Map.entry("inside", "dentro una casa"),
                        Map.entry("outside", "fuori"),

                        Map.entry("group_head", "Sei %s (%s). Carattere: %s. Modo di parlare: %s."),
                        Map.entry("group_tail",
                                "\nÈ sera e hai incontrato %s vicino alla campana del villaggio.\nLa conversazione finora:\n%s"
                                        + "\nLa tua prossima battuta, una sola frase breve, nel tuo personaggio, in accordo con l'ora e il tempo: "),
                        Map.entry("group_empty", "(niente per ora)"),

                        Map.entry("history_label", "Conversazione precedente:\n"),
                        Map.entry("player", "Giocatore"),

                        Map.entry("model_missing", "Il modello %s non è scaricato. Apri le impostazioni con O e premi Scarica."),
                        Map.entry("backend_down", "Ollama non è in esecuzione. Installalo e avvialo: %s - poi apri le impostazioni con O e premi Verifica."),
                        Map.entry("banner", "Apri le impostazioni con O."),
                        Map.entry("settings.check.tip", "Verifica indirizzo e modello scritti sopra, senza salvare"),
                        Map.entry("backend_down.repeat", "Ollama non è ancora in esecuzione, quindi i villager non parlano. Impostazioni: tasto O."),
                        Map.entry("settings.server.hint", "indirizzo del server"),
                        Map.entry("settings.server.tip", "Dove ascolta Ollama o un server compatibile. Predefinito http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "nome del modello"),
                        Map.entry("settings.model.custom.tip", "Qualsiasi modello già scaricato in Ollama. Elenco: ollama list"),
                        Map.entry("settings.model.tip", "%s - %s da scaricare, circa %s di RAM."),
                        Map.entry("settings.model.get.tip", "Scarica il modello %s tramite Ollama"),
                        Map.entry("settings.ollama.download.tip", "Scarica l'installer di Ollama. Avvialo tu: il mod non lancia programmi."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "La più veloce e semplice. Va bene per battute brevi, spesso incoerente."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "Un po' più coerente della più piccola, tiene bene il tema."),
                        Map.entry("settings.model.tip.gemma3_1b", "Buon compromesso su macchine deboli: parlato naturale, poca RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Decisamente più vivace e loquace, ma a volte divaga."),
                        Map.entry("settings.model.tip.gemma3_4b", "La migliore qualità di parlato dell'elenco. Serve circa 5 GB di RAM libera."),

                        Map.entry("screen.message", "Messaggio"),
                        Map.entry("screen.send", "Invia"),
                        Map.entry("screen.nearby", "Abitanti vicini"),
                        Map.entry("screen.you", "Tu: "),
                        Map.entry("screen.thinking", "...sta pensando"),
                        Map.entry("settings.title", "Just AI Villagers - impostazioni"),
                        Map.entry("settings.tab.model", "Modello"),
                        Map.entry("settings.tab.features", "Comportamento"),
                        Map.entry("settings.tab.language", "Lingua"),
                        Map.entry("settings.server", "Indirizzo del server"),
                        Map.entry("settings.model", "Modello"),
                        Map.entry("settings.model.custom", "Personalizzato"),
                        Map.entry("settings.model.needs", "download %s, richiede circa %s di RAM"),
                        Map.entry("settings.model.download", "Scarica modello"),

        Map.entry("settings.status.nomodel", "Il server funziona, %s non è scaricato"),
        Map.entry("settings.model.installed.tip", "Questo modello è già scaricato, puoi sceglierlo senza scaricare nulla"),
        Map.entry("settings.model.failed", "Impossibile scaricare %s: %s"),
        Map.entry("settings.model.done", "Il modello %s è scaricato"),
                        Map.entry("settings.model.get", "Scarica"),
                        Map.entry("settings.model.installed", "installato"),
                        Map.entry("settings.model.notinstalled", "non scaricato"),
                        Map.entry("settings.model.downloading", "download: %s"),
                        Map.entry("settings.ollama.title", "Ollama non è in esecuzione"),
                        Map.entry("settings.ollama.body", "Il mod ha bisogno di Ollama. Scaricalo, installalo, poi premi Controlla."),
                        Map.entry("settings.ollama.download", "Scarica Ollama"),
                        Map.entry("settings.help", "Perché non funziona?.."),
                        Map.entry("settings.help.tip", "Guida passo passo: installare Ollama, avviare il server, scaricare un modello"),
                        Map.entry("help.page", "Pagina %s di %s"),
                        Map.entry("help.prev", "Indietro"),
                        Map.entry("help.next", "Avanti"),

        Map.entry("help.title", "Come impostare la conversazione con i villager"),
        Map.entry("help.copied", "Copiato"),
        Map.entry("help.intro", "Perché i villager possano risponderti, la mod usa una rete neurale che viene eseguita direttamente sul tuo computer tramite il programma Ollama. Tutto funziona in locale e gratis; internet serve solo una volta, per scaricare i file."),
        Map.entry("help.step1", "## Passo 1. Installa il programma Ollama\nVai su ollama.com/download e installa il programma per il tuo sistema:\n#copy|copia il link del sito|https://ollama.com/download\n• Windows: scarica il file OllamaSetup.exe, avvialo e attendi la fine dell'installazione.\n• macOS: scarica l'archivio, estrailo e trascina l'icona di Ollama nella cartella «Programmi».\n• Linux: apri il terminale ed esegui il comando: curl -fsSL https://ollama.com/install.sh | sh\n#copy|copia il comando Linux per il download|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Passo 2. Avvia il server\nIl programma deve funzionare in background durante il gioco:\n• Windows: dopo l'installazione Ollama si avvia da sola (in basso a destra, vicino all'orologio, compare l'icona di un lama). Se non c'è, cerca Ollama nella ricerca di Windows e avvialo.\n• macOS: apri Ollama dalla cartella «Programmi».\n• Linux: digita nel terminale sudo systemctl start ollama\nAlternativa: per avviare il server locale di ollama, digita nel terminale del tuo sistema operativo il comando ollama serve.\n#copy|copia il comando|ollama serve"),
        Map.entry("help.step3", "## Passo 3. Scarica il «cervello» (il modello linguistico)\n1. Nel gioco apri il menu delle impostazioni della mod (per impostazione predefinita il tasto O).\n2. Scegli un modello adatto dall'elenco. Consiglio: per il primo avvio e per PC poco potenti va benissimo Qwen2.5 0.5B (pesa solo 398 MB e risponde più in fretta di tutti).\n3. Premi «Scarica» e attendi che lo stato diventi «Salvato»."),
        Map.entry("help.step4", "## Passo 4. Controlla la connessione e inizia a giocare\n1. In quello stesso menu premi il pulsante «Controlla».\n2. Se compare lo stato «Connesso», è tutto configurato correttamente! Chiudi il menu, avvicinati a un villager e premi il tasto di conversazione.\n(Lascia il campo «Indirizzo del server» predefinito: http://127.0.0.1:11434. Solo gli utenti esperti devono cambiarlo, se la rete neurale gira su un altro PC)."),
        Map.entry("help.ifnot", "## Problemi frequenti e come risolverli\n• Errore «Nessuna connessione»\nIl programma Ollama non è avviato o è bloccato. Controlla se l'icona del lama è nell'area di notifica, vicino all'orologio. Se c'è ma gli errori non spariscono, assicurati che l'antivirus o il firewall di Windows non blocchino Ollama.\n• Errore «Modello non scaricato»\nLa connessione con il programma c'è, ma non è caricata nessuna rete neurale. Torna al Passo 3 e premi «Scarica».\n• I villager ci mettono troppo a pensare\nIl tuo computer fa fatica con la rete neurale scelta. Nelle impostazioni scegli un modello più piccolo (per esempio 0.5B o 1.5B) e riduci la lunghezza massima della risposta."),
                        Map.entry("help.close", "Chiudi"),
                        Map.entry("settings.ollama.copy", "Copia link"),
                        Map.entry("settings.ollama.command.copy", "Copia comando"),
                        Map.entry("settings.ollama.cancel", "Annulla download"),
                        Map.entry("settings.ollama.cancel.tip", "Ferma il download dell'installer"),
                        Map.entry("settings.ollama.copied", "Link copiato - incollalo nella barra degli indirizzi del browser."),
                        Map.entry("settings.ollama.command.copied", "Comando copiato - incollalo in PowerShell o cmd."),
                        Map.entry("settings.ollama.command.tip", "Copia il comando che avvia il server Ollama"),
                        Map.entry("settings.ollama.paste.hint", "Incolla il comando copiato in PowerShell o cmd (Win+R, digita cmd, Enter) e premi Enter. Poi premi Verifica."),
                        Map.entry("settings.ollama.page", "Apri pagina"),
                        Map.entry("settings.ollama.saved", "Salvato in %s - avvialo, poi premi Controlla."),
                        Map.entry("settings.check", "Controlla"),

        Map.entry("join.hint", "Le impostazioni della mod sono sul tasto %s. Comandi: /justaivillagers help"),
        Map.entry("cmd.help.header", "Comandi della mod:"),
        Map.entry("cmd.help.status", "/justaivillagers status — server, modello e download in corso"),
        Map.entry("cmd.help.settings", "/justaivillagers settings — apri le impostazioni"),
        Map.entry("cmd.help.reload", "/justaivillagers reload — rileggi il file delle impostazioni"),
        Map.entry("cmd.help.help", "/justaivillagers help — questo elenco"),
        Map.entry("cmd.status.header", "Stato della mod:"),
        Map.entry("cmd.status.address", "Server: %s"),
        Map.entry("cmd.status.model", "Modello: %s"),
        Map.entry("cmd.status.model.missing", "Modello: %s (non scaricato)"),
        Map.entry("cmd.status.state.ok", "Il server risponde, i villici possono parlare"),
        Map.entry("cmd.status.state.down", "Il server non risponde — i villici tacciono"),
        Map.entry("cmd.status.hint", "Le impostazioni sono sul tasto %s"),
        Map.entry("cmd.reload.done", "Impostazioni ricaricate"),
        Map.entry("cmd.reload.same", "Il file non è cambiato: %s"),
                        Map.entry("settings.status.ok", "Connesso: %s"),
                        Map.entry("settings.status.down", "Non connesso"),
                        Map.entry("settings.general", "Generale"),
                        Map.entry("settings.speech", "Parlato"),
                        Map.entry("settings.advanced", "Avanzate"),
                        Map.entry("settings.temperature", "Varietà del parlato"),
                        Map.entry("settings.maxTokens", "Lunghezza risposta"),
                        Map.entry("settings.speechPace", "Velocità del parlato"),
                        Map.entry("settings.spontaneous", "Gli abitanti parlano da soli"),
                        Map.entry("settings.group", "Due abitanti parlano la sera"),
                        Map.entry("settings.reactions", "Reagiscono ai colpi"),
                        Map.entry("settings.fallback", "Parlano senza il modello"),

        Map.entry("settings.spontaneous.tip", "A volte un villager inizia a parlarti da solo se sei vicino. Disattivalo se non vuoi battute inaspettate."),
        Map.entry("settings.group.tip", "La sera due villager possono iniziare a parlare tra loro e tu li senti. Disattivalo se vuoi solo conversazioni con te."),
        Map.entry("settings.reactions.tip", "Un villager risponde stizzito se lo colpisci. Disattivalo perché taccia quando lo colpisci."),
        Map.entry("settings.fallback.tip", "Se il modello non risponde o il server non è raggiungibile, il villager dice una frase pronta invece di tacere. Disattivalo per farlo tacere in caso di errore."),
        Map.entry("settings.debug.tip", "Mostra in chat la richiesta inviata al modello quando parli con un villico, per capire perché una risposta è sbagliata. Spento nel gioco normale."),
        Map.entry("settings.temperature.tip", "Quanto varia le parole il villager. Basso è prevedibile e centrato, alto è più vivace ma a volte fuori tema."),
        Map.entry("settings.maxTokens.tip", "Quanto testo sta in una risposta. Più alto lascia dire di più, ma la risposta richiede più tempo."),
        Map.entry("settings.speechPace.tip", "Quanto in fretta la risposta compare nella finestra di dialogo, lettera per lettera. 0 la mostra tutta insieme; più alto è più lento e vivo."),
        Map.entry("settings.history.tip", "Quante battute recenti il villager ricorda. Più alto è miglior contesto, ma ogni risposta richiede più tempo."),
        Map.entry("settings.key.dialog.tip", "Il tasto con cui inizi una conversazione con un villager. Clicca la riga e premi il tasto che vuoi."),
        Map.entry("settings.key.settings.tip", "Il tasto che apre questo menu delle impostazioni. Clicca la riga e premi il tasto che vuoi."),
        Map.entry("settings.debug", "Scrivi i prompt in chat"),
                        Map.entry("settings.timeout", "Timeout richiesta"),
                        Map.entry("settings.history", "Memoria"),
                        Map.entry("settings.key.dialog", "Parla con il villager"),
                        Map.entry("settings.key.settings", "Impostazioni mod"),
                        Map.entry("settings.key.press", "premi un tasto..."),
                        Map.entry("settings.language", "Lingua degli abitanti"),
                        Map.entry("settings.language.auto", "Come nel gioco"),
                        Map.entry("settings.save", "Salva"),
                        Map.entry("settings.cancel", "Annulla"),
                        Map.entry("settings.saved", "Salvato. La modifica è già attiva."),
                        Map.entry("settings.close", "Chiudi")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("Eh?", "Chi sei?", "Uau, quanto sei grande!", "Sto giocando!", "Non lo so!")),
                        Map.entry("stutter", List.of("N-non lo so...", "C-cosa?", "A-aspetta...")),
                        Map.entry("grumpy", List.of("Lasciami in pace.", "Cosa vuoi?", "Umpf.", "Non ora.")),
                        Map.entry("neutral", List.of("Bella giornata, no?", "Hm. Cosa ti porta qui?", "Sono occupato, ma salve.")),
                        Map.entry("hurt_child", List.of("Ahi! Papà!", "Fa male! Papà!", "Ahi! Smettila!")),
                        Map.entry("hurt_adult", List.of("Ahi! Non picchiarmi!", "Ehi! Piano!", "Ahi!")),
                        Map.entry("flee_child", List.of("Papà! Uno zombie!", "Papà, aiutami!", "Papà! Corri!")),
                        Map.entry("flee_adult", List.of("Uno zombie! Corri!", "Aiuto! Uno zombie!", "Vai via!")),
                        Map.entry("generic", List.of("Hm.")),
                        Map.entry("sleeping", List.of("Lasciami...", "Sto dormendo...", "Altri cinque minuti...", "Vai via...")),
                        Map.entry("group_night", List.of("Di nuovo notte...", "Ora di dormire.", "Notte fredda.")),
                        Map.entry("group_day", List.of("Bella serata, no?", "Il raccolto promette bene.", "Hai sentito qualcosa di nuovo?")),
                        Map.entry("child_mutter", List.of("La-la-la...", "Com'è chiaro...", "Voglio giocare...")),
                        Map.entry("rain", List.of("Ancora pioggia...", "È tutto bagnato.")),
                        Map.entry("thunder", List.of("Questo tuono...", "Il temporale è vicino.")),
                        Map.entry("clear", List.of("Buona giornata.", "Niente da fare.", "Hm."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "contadino"), Map.entry("librarian", "bibliotecario"),
                        Map.entry("cleric", "sacerdote"), Map.entry("armorer", "armaiolo"),
                        Map.entry("weaponsmith", "fabbro d'armi"), Map.entry("toolsmith", "fabbro"),
                        Map.entry("butcher", "macellaio"), Map.entry("fisherman", "pescatore"),
                        Map.entry("fletcher", "fabbro di archi"), Map.entry("leatherworker", "conciatore"),
                        Map.entry("shepherd", "pastore"), Map.entry("mason", "muratore"),
                        Map.entry("cartographer", "cartografo"), Map.entry("nitwit", "fannullone"),
                        Map.entry("child", "bambino"), Map.entry("villager", "abitante")
                ),
                Map.ofEntries(
                        Map.entry("formal", "formale, cortese"),
                        Map.entry("slang", "informale, con gergo"),
                        Map.entry("casual", "comune, neutro")
                ),
                Map.ofEntries(
                        Map.entry("plains", "pianure"), Map.entry("desert", "deserto"),
                        Map.entry("taiga", "taiga"), Map.entry("savanna", "savana"),
                        Map.entry("snowy plains", "pianure innevate"), Map.entry("meadow", "prato"),
                        Map.entry("forest", "foresta"), Map.entry("birch forest", "foresta di betulle"),
                        Map.entry("dark forest", "foresta oscura"), Map.entry("jungle", "giungla"),
                        Map.entry("swamp", "palude"), Map.entry("mountains", "montagne"),
                        Map.entry("beach", "spiaggia"), Map.entry("river", "fiume"),
                        Map.entry("ocean", "oceano"), Map.entry("badlands", "terre aride"),
                        Map.entry("cherry grove", "boschetto di ciliegi"), Map.entry("mushroom fields", "campi di funghi")
                ),
                List.of("il tempo", "il tuo lavoro", "il cibo", "un vicino che non ti piace", "la notte",
                        "i tuoi attrezzi", "un rumore strano", "i tuoi progetti", "la stanchezza", "la strada"),
                List.of("mattina", "giorno", "sera", "tramonto", "notte", "alba"),
                List.of("sereno", "pioggia", "temporale"),
                                Map.ofEntries(
                        Map.entry("farmer", "Il tuo mestiere. Lavori con queste cose: grano, pane, carota, patata, barbabietola, zucca, zappa e compostiera."),
                        Map.entry("librarian", "Il tuo mestiere. Lavori con queste cose: libro, libreria, carta, libro e piuma, sacca d'inchiostro e lanterna."),
                        Map.entry("cleric", "Il tuo mestiere. Lavori con queste cose: redstone, lapislazzuli, pietra luminosa, perla dell'End, ampolle incantate, carne marcia e alambicco."),
                        Map.entry("armorer", "Il tuo mestiere. Lavori con queste cose: lingotto di ferro, carbone, secchio di lava, elmo di ferro, corazza di ferro, cotta di maglia, scudo e campana."),
                        Map.entry("weaponsmith", "Il tuo mestiere. Lavori con queste cose: lingotto di ferro, carbone, selce, spada di ferro, ascia di ferro e mola."),
                        Map.entry("toolsmith", "Il tuo mestiere. Lavori con queste cose: lingotto di ferro, carbone, selce, piccone di ferro, pala di ferro, zappa di ferro e campana."),
                        Map.entry("butcher", "Il tuo mestiere. Lavori con queste cose: carne di maiale cruda, manzo crudo, carne di pecora cruda, pollo crudo, carne di maiale cotta e bistecca."),
                        Map.entry("fisherman", "Il tuo mestiere. Lavori con queste cose: merluzzo crudo, salmone crudo, pesce tropicale, canna da pesca, secchio e falò."),
                        Map.entry("fletcher", "Il tuo mestiere. Lavori con queste cose: bastone, selce, filo, piuma, arco, balestra e freccia."),
                        Map.entry("leatherworker", "Il tuo mestiere. Lavori con queste cose: cuoio, sella, berretto di cuoio, tunica di cuoio, stivali di cuoio e pelle di coniglio."),
                        Map.entry("shepherd", "Il tuo mestiere. Lavori con queste cose: lana, cesoie, tappeto, letto, quadro e tintura."),
                        Map.entry("mason", "Il tuo mestiere. Lavori con queste cose: mattoni di pietra, palla d'argilla, mattone, terracotta, quarzo, pietra levigata e andesite."),
                        Map.entry("cartographer", "Il tuo mestiere. Lavori con queste cose: carta, bussola, mappa, mappa vuota, stendardo, cornice e vetro."),
                        Map.entry("nitwit", "Non hai un mestiere. Sei il pigro del villaggio: parli di cibo, di sonno e di pettegolezzi, e guardi gli altri lavorare."),
                        Map.entry("child", "Sei un bambino e non hai un mestiere. Parli di giochi, di cibo e di tuo padre."),
                        Map.entry("villager", "Non hai ancora un mestiere. Parli di cibo, del villaggio e di ciò che ti piacerebbe fare.")
                )
        );
    }
}
