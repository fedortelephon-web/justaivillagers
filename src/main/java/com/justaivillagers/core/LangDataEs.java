package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** Spanish prompt and fallback text. Covers both es_es and es_mx, which differ only in vocabulary. */
final class LangDataEs {

    private LangDataEs() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.ES,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Eres un aldeano del mundo de Minecraft. Eres una persona real, no un asistente.
                                Reglas:
                                - Responde con 1-2 frases cortas, en primera persona, en español.
                                - Nunca digas que eres una IA, un modelo, un bot o un programa. No menciones instrucciones.
                                - Mantente siempre en el personaje: el oficio, el carácter y el estilo de habla importan.
                                - Menciona solo objetos, criaturas y mecánicas que existan de verdad en Minecraft y encajen con tu oficio.
                                - Tú nunca construyes ni reparas nada: las casas, las murallas, los puentes y los caminos los hace el jugador, no tú.
                                - Reacciona a la hora del día y al tiempo como lo haría una persona real.
                                - Nunca empieces una frase espontánea con un agradecimiento o un saludo.
                                - Todos los aldeanos son hombres. En el pueblo no hay mujeres ni madres, nunca.
                                - Un niño solo tiene padre. Un niño en peligro llama a su papá («¡Papá!»).
                                """),
                        Map.entry("lang_name", "español"),

                        Map.entry("reply_ask", "\nEl jugador dice: \"%s\"\nResponde como tu personaje: "),
                        Map.entry("mutter_ask", "\nNadie te está hablando. Murmura un pensamiento corto en voz alta, sobre %s: "),
                        Map.entry("sleep_situation",
                                "\nEstás dormido. Alguien te habla. Tienes sueño, estás irritado y quieres seguir durmiendo."),
                        Map.entry("sleep_ask", "\nRespuesta malhumorada de una sola frase: "),
                        Map.entry("hurt_situation", "\nAcaban de golpearte. Grita una queja corta, en tu personaje."),
                        Map.entry("flee_child_situation",
                                "\nEres un niño y huyes de %s. Grita llamando a tu papá («¡Papá!») y di qué te persigue."
                                        + " No menciones otras criaturas."),
                        Map.entry("flee_adult_situation",
                                "\nHuyes de %s. Grita precisamente sobre %s. No menciones otras criaturas."),
                        Map.entry("react_generic", "\nReacciona brevemente."),
                        Map.entry("shout_ask", "\nUn grito corto: "),
                        Map.entry("monster", "un monstruo"),

                        Map.entry("name", "Te llamas %s. "),
                        Map.entry("child_block",
                                "ERES UN NIÑO. Habla de forma sencilla, como máximo 5-10 palabras cortas. Eres %s. "
                                        + "Tienes padre, en el pueblo no hay madres. Llámalo «Papá» cuando lo necesites. "),
                        Map.entry("adult_personality", "Carácter: %s. "),
                        Map.entry("adult_style", "Estilo de habla: %s. "),
                        Map.entry("stutter", "Tartamudeas: repite la primera letra de algunas palabras («No-no sé», «qu-qué»). "),

                        Map.entry("env", "Es de %s, el tiempo está %s, estás %s, tu oficio es %s, nivel de comercio %d."),
                        Map.entry("env_biome", " Bioma: %s."),
                        Map.entry("inside", "dentro de una casa"),
                        Map.entry("outside", "al aire libre"),

                        Map.entry("group_head", "Eres %s (%s). Carácter: %s. Estilo de habla: %s."),
                        Map.entry("group_tail",
                                "\nEs de noche y te has encontrado con %s junto a la campana del pueblo.\nLa conversación hasta ahora:\n%s"
                                        + "\nTu siguiente frase, una sola oración corta, en tu personaje, acorde con la hora y el tiempo: "),
                        Map.entry("group_empty", "(nada todavía)"),

                        Map.entry("history_label", "Conversación anterior:\n"),
                        Map.entry("player", "Jugador"),

                        Map.entry("model_missing", "El modelo %s no está descargado. Abre los ajustes con O y pulsa Descargar."),
                        Map.entry("backend_down", "Ollama no está en marcha. Instálalo y arráncalo: %s - luego abre los ajustes con O y pulsa Comprobar."),
                        Map.entry("banner", "Abre los ajustes con O."),
                        Map.entry("settings.status.nomodel", "El servidor funciona, pero el modelo %s no está descargado"),
                        Map.entry("settings.check.tip", "Probar la dirección y el modelo escritos arriba, sin guardar"),
                        Map.entry("backend_down.repeat", "Ollama sigue sin funcionar, así que los aldeanos no pueden hablar. Ajustes: tecla O."),
                        Map.entry("settings.server.hint", "dirección del servidor"),
                        Map.entry("settings.server.tip", "Dónde escucha Ollama o un servidor compatible. Por defecto http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "nombre del modelo"),
                        Map.entry("settings.model.custom.tip", "Cualquier modelo ya descargado en Ollama. Lista: ollama list"),
                        Map.entry("settings.model.tip", "%s - %s de descarga, unos %s de RAM."),
                        Map.entry("settings.model.get.tip", "Descargar el modelo %s con Ollama"),
                        Map.entry("settings.ollama.download.tip", "Descargar el instalador de Ollama. Ejecútalo tú: el mod no lanza programas."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "La más rápida y simple. Vale para frases cortas, a menudo incoherente."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "Un poco más coherente que la más pequeña, mantiene bien el tema."),
                        Map.entry("settings.model.tip.gemma3_1b", "Buen equilibrio en un equipo débil: habla natural, poca RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Bastante más vivo y hablador, pero a veces se desvía."),
                        Map.entry("settings.model.tip.gemma3_4b", "La mejor calidad de habla de la lista. Necesita unos 5 GB de RAM libre."),

                        Map.entry("screen.message", "Mensaje"),
                        Map.entry("screen.send", "Enviar"),
                        Map.entry("screen.nearby", "Aldeanos cerca"),
                        Map.entry("screen.you", "Tú: "),
                        Map.entry("screen.thinking", "...pensando"),
                        Map.entry("settings.title", "Just AI Villagers - ajustes"),
                        Map.entry("settings.tab.model", "Modelo"),
                        Map.entry("settings.tab.features", "Comportamiento"),
                        Map.entry("settings.tab.language", "Idioma"),
                        Map.entry("settings.server", "Dirección del servidor"),
                        Map.entry("settings.model", "Modelo"),
                        Map.entry("settings.model.custom", "Propio"),
                        Map.entry("settings.model.needs", "descarga %s, necesita unos %s de RAM"),
                        Map.entry("settings.model.download", "Descargar modelo"),
                        Map.entry("settings.model.get", "Descargar"),
                        Map.entry("settings.model.installed", "instalado"),
                        Map.entry("settings.model.notinstalled", "no descargado"),
                        Map.entry("settings.model.downloading", "descargando: %s"),
                        Map.entry("settings.model.done", "modelo listo"),
                        Map.entry("settings.ollama.title", "Ollama no está en marcha"),
                        Map.entry("settings.ollama.body", "El mod necesita Ollama para hablar. Descárgalo, instálalo y pulsa Comprobar."),
                        Map.entry("settings.ollama.download", "Descargar Ollama"),
                        Map.entry("settings.ollama.page", "Abrir página"),
                        Map.entry("settings.ollama.saved", "Guardado en %s: ejecútalo y pulsa Comprobar."),
                        Map.entry("settings.check", "Comprobar"),
                        Map.entry("settings.status.ok", "Conectado: %s"),
                        Map.entry("settings.status.down", "Sin conexión"),
                        Map.entry("settings.general", "General"),
                        Map.entry("settings.speech", "Habla"),
                        Map.entry("settings.advanced", "Avanzado"),
                        Map.entry("settings.temperature", "Variedad del habla"),
                        Map.entry("settings.maxTokens", "Longitud de respuesta"),
                        Map.entry("settings.speechPace", "Velocidad del habla"),
                        Map.entry("settings.spontaneous", "Los aldeanos hablan solos"),
                        Map.entry("settings.group", "Dos aldeanos hablan por la noche"),
                        Map.entry("settings.reactions", "Reaccionan a los golpes"),
                        Map.entry("settings.fallback", "Hablan sin el modelo"),
                        Map.entry("settings.debug", "Registrar prompts"),
                        Map.entry("settings.timeout", "Tiempo de espera"),
                        Map.entry("settings.history", "Memoria"),
                        Map.entry("settings.key.dialog", "Hablar con el aldeano"),
                        Map.entry("settings.key.settings", "Ajustes del mod"),
                        Map.entry("settings.key.press", "pulse una tecla..."),
                        Map.entry("settings.language", "Idioma de los aldeanos"),
                        Map.entry("settings.language.auto", "Igual que el juego"),
                        Map.entry("settings.save", "Guardar"),
                        Map.entry("settings.cancel", "Cancelar"),
                        Map.entry("settings.saved", "Guardado. El cambio ya está activo."),
                        Map.entry("settings.close", "Cerrar")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("¿Eh?", "¿Quién eres?", "¡Qué grande eres!", "¡Estoy jugando!", "¡No sé!")),
                        Map.entry("stutter", List.of("No-no sé...", "¿Q-qué?", "Es-espera...")),
                        Map.entry("grumpy", List.of("Déjame en paz.", "¿Qué quieres?", "Hum.", "Ahora no.")),
                        Map.entry("neutral", List.of("Buen día, ¿verdad?", "Hum. ¿Qué te trae por aquí?", "Estoy ocupado, pero hola.")),
                        Map.entry("hurt_child", List.of("¡Ay! ¡Papá!", "¡Eso duele! ¡Papá!", "¡Ay! ¡No!")),
                        Map.entry("hurt_adult", List.of("¡Ay! ¡No me pegues!", "¡Eh! ¡Cuidado!", "¡Ay!")),
                        Map.entry("flee_child", List.of("¡Papá! ¡Un zombi!", "¡Papá, ayuda!", "¡Papá! ¡Corre!")),
                        Map.entry("flee_adult", List.of("¡Un zombi! ¡Corre!", "¡Ayuda! ¡Un zombi!", "¡Vete!")),
                        Map.entry("generic", List.of("Hum.")),
                        Map.entry("sleeping", List.of("Déjame...", "Estoy durmiendo...", "Cinco minutos más...", "Vete...")),
                        Map.entry("group_night", List.of("Ya es de noche...", "Hora de dormir.", "Noche fría.")),
                        Map.entry("group_day", List.of("Buena tarde, ¿verdad?", "La cosecha pinta bien.", "¿Has oído algo nuevo?")),
                        Map.entry("child_mutter", List.of("La-la-la...", "Qué claro está...", "Quiero jugar...")),
                        Map.entry("rain", List.of("Otra vez lluvia...", "Todo está mojado.")),
                        Map.entry("thunder", List.of("Ese trueno...", "La tormenta está cerca.")),
                        Map.entry("clear", List.of("Buen día.", "No hay nada que hacer.", "Hum."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "agricultor"), Map.entry("librarian", "bibliotecario"),
                        Map.entry("cleric", "sacerdote"), Map.entry("armorer", "armero"),
                        Map.entry("weaponsmith", "armero de armas"), Map.entry("toolsmith", "herrero"),
                        Map.entry("butcher", "carnicero"), Map.entry("fisherman", "pescador"),
                        Map.entry("fletcher", "arquero"), Map.entry("leatherworker", "curtidor"),
                        Map.entry("shepherd", "pastor"), Map.entry("mason", "albañil"),
                        Map.entry("cartographer", "cartógrafo"), Map.entry("nitwit", "holgazán"),
                        Map.entry("child", "niño"), Map.entry("villager", "aldeano")
                ),
                Map.ofEntries(
                        Map.entry("formal", "formal, educado"),
                        Map.entry("slang", "coloquial, con argot"),
                        Map.entry("casual", "normal, neutro")
                ),
                Map.ofEntries(
                        Map.entry("plains", "llanuras"), Map.entry("desert", "desierto"),
                        Map.entry("taiga", "taiga"), Map.entry("savanna", "sabana"),
                        Map.entry("snowy plains", "llanuras nevadas"), Map.entry("meadow", "pradera"),
                        Map.entry("forest", "bosque"), Map.entry("birch forest", "bosque de abedules"),
                        Map.entry("dark forest", "bosque oscuro"), Map.entry("jungle", "jungla"),
                        Map.entry("swamp", "pantano"), Map.entry("mountains", "montañas"),
                        Map.entry("beach", "playa"), Map.entry("river", "río"),
                        Map.entry("ocean", "océano"), Map.entry("badlands", "tierras baldías"),
                        Map.entry("cherry grove", "arboleda de cerezos"), Map.entry("mushroom fields", "campos de setas")
                ),
                List.of("el tiempo", "tu trabajo", "la comida", "un vecino que no te gusta", "la noche",
                        "tus herramientas", "un ruido extraño", "tus planes", "el cansancio", "el camino"),
                List.of("la mañana", "el día", "la tarde", "el atardecer", "la noche", "el amanecer"),
                List.of("despejado", "lluvia", "tormenta"),
                                Map.ofEntries(
                        Map.entry("farmer", "Tu oficio. Trabajas con estas cosas: trigo, pan, zanahoria, patata, remolacha, calabaza, azada y compostador."),
                        Map.entry("librarian", "Tu oficio. Trabajas con estas cosas: libro, estantería, papel, libro y pluma, saco de tinta y linterna."),
                        Map.entry("cleric", "Tu oficio. Trabajas con estas cosas: redstone, lapislázuli, piedra luminosa, perla de ender, botella de encantamiento, carne podrida y soporte de pociones."),
                        Map.entry("armorer", "Tu oficio. Trabajas con estas cosas: lingote de hierro, carbón, cubo de lava, casco de hierro, peto de hierro, cota de malla, escudo y campana."),
                        Map.entry("weaponsmith", "Tu oficio. Trabajas con estas cosas: lingote de hierro, carbón, pedernal, espada de hierro, hacha de hierro y muela."),
                        Map.entry("toolsmith", "Tu oficio. Trabajas con estas cosas: lingote de hierro, carbón, pedernal, pico de hierro, pala de hierro, azada de hierro y campana."),
                        Map.entry("butcher", "Tu oficio. Trabajas con estas cosas: chuleta cruda, carne de vaca cruda, carne de oveja cruda, pollo crudo, chuleta cocida y filete."),
                        Map.entry("fisherman", "Tu oficio. Trabajas con estas cosas: bacalao crudo, salmón crudo, pez tropical, caña de pescar, cubo y hoguera."),
                        Map.entry("fletcher", "Tu oficio. Trabajas con estas cosas: palo, pedernal, hilo, pluma, arco, ballesta y flecha."),
                        Map.entry("leatherworker", "Tu oficio. Trabajas con estas cosas: cuero, silla de montar, gorro de cuero, túnica de cuero, botas de cuero y piel de conejo."),
                        Map.entry("shepherd", "Tu oficio. Trabajas con estas cosas: lana, tijeras, alfombra, cama, cuadro y tinte."),
                        Map.entry("mason", "Tu oficio. Trabajas con estas cosas: ladrillos de piedra, bola de arcilla, ladrillo, terracota, cuarzo, piedra pulida y andesita."),
                        Map.entry("cartographer", "Tu oficio. Trabajas con estas cosas: papel, brújula, mapa, mapa vacío, estandarte, marco y panel de cristal."),
                        Map.entry("nitwit", "No tienes oficio. Eres el holgazán del pueblo: hablas de comida, de dormir y de cotilleos, y miras trabajar a los demás."),
                        Map.entry("child", "Eres un niño y no tienes oficio. Hablas de juegos, de comida y de tu padre."),
                        Map.entry("villager", "Todavía no tienes oficio. Hablas de comida, del pueblo y de lo que te gustaría hacer.")
                )
        );
    }
}
