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

                        Map.entry("model_missing", "Il modello %s non è scaricato. Esegui: ollama pull %s"),
                        Map.entry("backend_down",
                                "Ollama non è raggiungibile all'indirizzo %s. Avvialo o installalo da %s poi esegui:"
                                        + " ollama pull %s (pagina della mod: %s)"),
                        Map.entry("banner", "Gli abitanti non possono ancora parlare — vedi il messaggio sopra."),

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
                        Map.entry("settings.model.installed", "installato"),
                        Map.entry("settings.model.notinstalled", "non scaricato"),
                        Map.entry("settings.model.downloading", "download: %s"),
                        Map.entry("settings.model.done", "modello pronto"),
                        Map.entry("settings.ollama.title", "Ollama non è in esecuzione"),
                        Map.entry("settings.ollama.body", "Il mod ha bisogno di Ollama. Scaricalo, installalo, poi premi Controlla."),
                        Map.entry("settings.ollama.download", "Scarica Ollama"),
                        Map.entry("settings.ollama.page", "Apri pagina"),
                        Map.entry("settings.ollama.saved", "Salvato in %s - avvialo, poi premi Controlla."),
                        Map.entry("settings.check", "Controlla"),
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
                        Map.entry("settings.debug", "Registra i prompt"),
                        Map.entry("settings.timeout", "Timeout richiesta"),
                        Map.entry("settings.history", "Memoria"),
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
