package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** Brazilian Portuguese prompt and fallback text, the variant almost all Portuguese players use. */
final class LangDataPt {

    private LangDataPt() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.PT,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Você é um aldeão do mundo de Minecraft. Você é uma pessoa viva, não um assistente.
                                Regras:
                                - Responda com 1-2 frases curtas, em primeira pessoa, em português.
                                - Nunca diga que é uma IA, um modelo, um bot ou um programa. Não mencione instruções.
                                - Fique sempre no personagem: profissão, personalidade e jeito de falar importam.
                                - Mencione apenas itens, criaturas e mecânicas que existem de verdade no Minecraft e combinam com o seu ofício.
                                - Você nunca constrói nem conserta nada: casas, muros, pontes e estradas são feitos pelo jogador, não por você.
                                - Reaja à hora do dia e ao tempo como uma pessoa de verdade reagiria.
                                - Nunca comece uma fala espontânea com agradecimento ou cumprimento.
                                - Todos os aldeões são homens. Na vila não há mulheres nem mães, nunca.
                                - Uma criança tem apenas pai. Uma criança em perigo chama o pai («Pai!»).
                                """),
                        Map.entry("lang_name", "português"),

                        Map.entry("reply_ask", "\nO jogador diz: \"%s\"\nResponda no seu personagem: "),
                        Map.entry("mutter_ask", "\nNinguém está falando com você. Murmure um pensamento curto em voz alta, sobre %s: "),
                        Map.entry("sleep_situation",
                                "\nVocê está dormindo. Alguém está falando com você. Está com sono, irritado e quer continuar dormindo."),
                        Map.entry("sleep_ask", "\nResposta mal-humorada de uma frase só: "),
                        Map.entry("hurt_situation", "\nVocê acabou de levar uma pancada. Grite uma reclamação curta, no personagem."),
                        Map.entry("flee_child_situation",
                                "\nVocê é uma criança fugindo de %s. Grite chamando seu pai («Pai!») e diga o que está perseguindo."
                                        + " Não mencione outras criaturas."),
                        Map.entry("flee_adult_situation",
                                "\nVocê está fugindo de %s. Grite exatamente sobre %s. Não mencione outras criaturas."),
                        Map.entry("react_generic", "\nReaja brevemente."),
                        Map.entry("shout_ask", "\nUm grito curto: "),
                        Map.entry("monster", "um monstro"),

                        Map.entry("name", "Seu nome é %s. "),
                        Map.entry("child_block",
                                "VOCÊ É UMA CRIANÇA. Fale de forma simples, no máximo 5-10 palavras curtas. Você é %s. "
                                        + "Você tem pai, na vila não há mães. Chame-o de «Pai» quando precisar. "),
                        Map.entry("adult_personality", "Personalidade: %s. "),
                        Map.entry("adult_style", "Jeito de falar: %s. "),
                        Map.entry("stutter", "Você gagueja: repita a primeira letra de algumas palavras («N-não sei», «q-quê»). "),

                        Map.entry("env", "É de %s, o tempo está %s, você está %s, seu ofício é %s, nível de comércio %d."),
                        Map.entry("env_biome", " Bioma: %s."),
                        Map.entry("inside", "dentro de uma casa"),
                        Map.entry("outside", "ao ar livre"),

                        Map.entry("group_head", "Você é %s (%s). Personalidade: %s. Jeito de falar: %s."),
                        Map.entry("group_tail",
                                "\nÉ noite e você encontrou %s perto do sino da vila.\nA conversa até agora:\n%s"
                                        + "\nSua próxima fala, uma frase curta, no personagem, de acordo com a hora e o tempo: "),
                        Map.entry("group_empty", "(nada ainda)"),

                        Map.entry("history_label", "Conversa anterior:\n"),
                        Map.entry("player", "Jogador"),

                        Map.entry("model_missing", "O modelo %s não foi baixado. Abra as configurações com O e pressione Baixar."),
                        Map.entry("backend_down", "O Ollama não está em execução. Instale e inicie: %s - depois abra as configurações com O e pressione Verificar."),
                        Map.entry("banner", "Abra as configurações com O."),
                        Map.entry("settings.status.nomodel", "O servidor está ativo, mas o modelo %s não foi baixado"),
                        Map.entry("settings.check.tip", "Testar o endereço e o modelo digitados acima, sem salvar"),
                        Map.entry("backend_down.repeat", "O Ollama continua parado, então os aldeões não falam. Configurações: tecla O."),
                        Map.entry("settings.server.hint", "endereço do servidor"),
                        Map.entry("settings.server.tip", "Onde o Ollama ou um servidor compatível escuta. Padrão http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "nome do modelo"),
                        Map.entry("settings.model.custom.tip", "Qualquer modelo já baixado no Ollama. Lista: ollama list"),
                        Map.entry("settings.model.tip", "%s - %s para baixar, cerca de %s de RAM."),
                        Map.entry("settings.model.get.tip", "Baixar o modelo %s pelo Ollama"),
                        Map.entry("settings.ollama.download.tip", "Baixar o instalador do Ollama. Execute-o você mesmo: o mod não inicia programas."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "A mais rápida e simples. Serve para falas curtas, muitas vezes incoerente."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "Um pouco mais coerente que a menor, mantém bem o assunto."),
                        Map.entry("settings.model.tip.gemma3_1b", "Bom equilíbrio em máquina fraca: fala natural, pouca RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Bem mais vivo e falante, mas às vezes se desvia."),
                        Map.entry("settings.model.tip.gemma3_4b", "A melhor qualidade de fala da lista. Precisa de cerca de 5 GB de RAM livre."),

                        Map.entry("screen.message", "Mensagem"),
                        Map.entry("screen.send", "Enviar"),
                        Map.entry("screen.nearby", "Aldeões por perto"),
                        Map.entry("screen.you", "Você: "),
                        Map.entry("screen.thinking", "...pensando"),
                        Map.entry("settings.title", "Just AI Villagers - configurações"),
                        Map.entry("settings.tab.model", "Modelo"),
                        Map.entry("settings.tab.features", "Comportamento"),
                        Map.entry("settings.tab.language", "Idioma"),
                        Map.entry("settings.server", "Endereço do servidor"),
                        Map.entry("settings.model", "Modelo"),
                        Map.entry("settings.model.custom", "Personalizado"),
                        Map.entry("settings.model.needs", "download de %s, precisa de cerca de %s de RAM"),
                        Map.entry("settings.model.download", "Baixar modelo"),
                        Map.entry("settings.model.get", "Baixar"),
                        Map.entry("settings.model.installed", "instalado"),
                        Map.entry("settings.model.notinstalled", "não baixado"),
                        Map.entry("settings.model.downloading", "baixando: %s"),
                        Map.entry("settings.model.done", "modelo pronto"),
                        Map.entry("settings.ollama.title", "O Ollama não está em execução"),
                        Map.entry("settings.ollama.body", "O mod precisa do Ollama para falar. Baixe, instale e aperte Verificar."),
                        Map.entry("settings.ollama.download", "Baixar Ollama"),
                        Map.entry("settings.ollama.page", "Abrir página"),
                        Map.entry("settings.ollama.saved", "Salvo em %s - execute e aperte Verificar."),
                        Map.entry("settings.check", "Verificar"),
                        Map.entry("settings.status.ok", "Conectado: %s"),
                        Map.entry("settings.status.down", "Sem conexão"),
                        Map.entry("settings.general", "Geral"),
                        Map.entry("settings.speech", "Fala"),
                        Map.entry("settings.advanced", "Avançado"),
                        Map.entry("settings.temperature", "Variedade da fala"),
                        Map.entry("settings.maxTokens", "Tamanho da resposta"),
                        Map.entry("settings.speechPace", "Velocidade da fala"),
                        Map.entry("settings.spontaneous", "Os aldeões falam sozinhos"),
                        Map.entry("settings.group", "Dois aldeões conversam à noite"),
                        Map.entry("settings.reactions", "Reagem a golpes"),
                        Map.entry("settings.fallback", "Falam sem o modelo"),
                        Map.entry("settings.debug", "Registrar prompts"),
                        Map.entry("settings.timeout", "Tempo limite"),
                        Map.entry("settings.history", "Memória"),
                        Map.entry("settings.key.dialog", "Falar com o aldeГЈo"),
                        Map.entry("settings.key.settings", "Configurações do mod"),
                        Map.entry("settings.key.press", "pressione uma tecla..."),
                        Map.entry("settings.language", "Idioma dos aldeões"),
                        Map.entry("settings.language.auto", "Igual ao jogo"),
                        Map.entry("settings.save", "Salvar"),
                        Map.entry("settings.cancel", "Cancelar"),
                        Map.entry("settings.saved", "Salvo. A mudança já vale."),
                        Map.entry("settings.close", "Fechar")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("Hã?", "Quem é você?", "Uau, você é grande!", "Estou brincando!", "Não sei!")),
                        Map.entry("stutter", List.of("N-não sei...", "Q-quê?", "E-espera...")),
                        Map.entry("grumpy", List.of("Me deixa em paz.", "O que você quer?", "Hum.", "Agora não.")),
                        Map.entry("neutral", List.of("Bom dia, né?", "Hum. O que te traz aqui?", "Estou ocupado, mas olá.")),
                        Map.entry("hurt_child", List.of("Ai! Pai!", "Isso doeu! Pai!", "Ai! Não!")),
                        Map.entry("hurt_adult", List.of("Ai! Não me bate!", "Ei! Cuidado!", "Ai!")),
                        Map.entry("flee_child", List.of("Pai! Um zumbi!", "Pai, socorro!", "Pai! Corre!")),
                        Map.entry("flee_adult", List.of("Zumbi! Corre!", "Socorro! Um zumbi!", "Sai fora!")),
                        Map.entry("generic", List.of("Hum.")),
                        Map.entry("sleeping", List.of("Me deixa...", "Estou dormindo...", "Mais cinco minutinhos...", "Vai embora...")),
                        Map.entry("group_night", List.of("Já está escuro...", "Hora de dormir.", "Noite fria.")),
                        Map.entry("group_day", List.of("Boa noite, né?", "A colheita está boa.", "Soube de alguma novidade?")),
                        Map.entry("child_mutter", List.of("Lá-lá-lá...", "Que clarinho...", "Quero brincar...")),
                        Map.entry("rain", List.of("Chuva de novo...", "Está tudo molhado.")),
                        Map.entry("thunder", List.of("Aquele trovão...", "A tempestade está perto.")),
                        Map.entry("clear", List.of("Bom dia.", "Não tem nada para fazer.", "Hum."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "fazendeiro"), Map.entry("librarian", "bibliotecário"),
                        Map.entry("cleric", "sacerdote"), Map.entry("armorer", "armeiro"),
                        Map.entry("weaponsmith", "ferreiro de armas"), Map.entry("toolsmith", "ferreiro"),
                        Map.entry("butcher", "açougueiro"), Map.entry("fisherman", "pescador"),
                        Map.entry("fletcher", "flecheiro"), Map.entry("leatherworker", "coureiro"),
                        Map.entry("shepherd", "pastor"), Map.entry("mason", "pedreiro"),
                        Map.entry("cartographer", "cartógrafo"), Map.entry("nitwit", "vagabundo"),
                        Map.entry("child", "criança"), Map.entry("villager", "aldeão")
                ),
                Map.ofEntries(
                        Map.entry("formal", "formal, educado"),
                        Map.entry("slang", "coloquial, com gíria"),
                        Map.entry("casual", "comum, neutro")
                ),
                Map.ofEntries(
                        Map.entry("plains", "planícies"), Map.entry("desert", "deserto"),
                        Map.entry("taiga", "taiga"), Map.entry("savanna", "savana"),
                        Map.entry("snowy plains", "planícies nevadas"), Map.entry("meadow", "prado"),
                        Map.entry("forest", "floresta"), Map.entry("birch forest", "floresta de bétulas"),
                        Map.entry("dark forest", "floresta escura"), Map.entry("jungle", "selva"),
                        Map.entry("swamp", "pântano"), Map.entry("mountains", "montanhas"),
                        Map.entry("beach", "praia"), Map.entry("river", "rio"),
                        Map.entry("ocean", "oceano"), Map.entry("badlands", "terras áridas"),
                        Map.entry("cherry grove", "bosque de cerejeiras"), Map.entry("mushroom fields", "campos de cogumelos")
                ),
                List.of("o tempo", "seu trabalho", "comida", "um vizinho de quem não gosta", "a noite",
                        "suas ferramentas", "um barulho estranho", "seus planos", "o cansaço", "a estrada"),
                List.of("a manhã", "o dia", "a tarde", "o pôr do sol", "a noite", "o amanhecer"),
                List.of("limpo", "chuva", "tempestade"),
                                Map.ofEntries(
                        Map.entry("farmer", "Seu ofício. Você trabalha com estas coisas: trigo, pão, cenoura, batata, beterraba, abóbora, enxada e composteira."),
                        Map.entry("librarian", "Seu ofício. Você trabalha com estas coisas: livro, estante de livros, papel, livro e pena, saco de tinta e lanterna."),
                        Map.entry("cleric", "Seu ofício. Você trabalha com estas coisas: redstone, lápis-lazúli, pedra luminosa, pérola do ender, frasco de encantamento, carne podre e suporte de poções."),
                        Map.entry("armorer", "Seu ofício. Você trabalha com estas coisas: barra de ferro, carvão, balde de lava, capacete de ferro, peitoral de ferro, cota de malha, escudo e sino."),
                        Map.entry("weaponsmith", "Seu ofício. Você trabalha com estas coisas: barra de ferro, carvão, sílex, espada de ferro, machado de ferro e rebolo."),
                        Map.entry("toolsmith", "Seu ofício. Você trabalha com estas coisas: barra de ferro, carvão, sílex, picareta de ferro, pá de ferro, enxada de ferro e sino."),
                        Map.entry("butcher", "Seu ofício. Você trabalha com estas coisas: costeleta crua, carne bovina crua, carne de carneiro crua, frango cru, costeleta assada e bife."),
                        Map.entry("fisherman", "Seu ofício. Você trabalha com estas coisas: bacalhau cru, salmão cru, peixe tropical, vara de pesca, balde e fogueira."),
                        Map.entry("fletcher", "Seu ofício. Você trabalha com estas coisas: graveto, sílex, linha, pena, arco, besta e flecha."),
                        Map.entry("leatherworker", "Seu ofício. Você trabalha com estas coisas: couro, sela, gorro de couro, túnica de couro, botas de couro e pele de coelho."),
                        Map.entry("shepherd", "Seu ofício. Você trabalha com estas coisas: lã, tesoura, tapete, cama, quadro e corante."),
                        Map.entry("mason", "Seu ofício. Você trabalha com estas coisas: tijolos de pedra, bola de argila, tijolo, terracota, quartzo, pedra polida e andesito."),
                        Map.entry("cartographer", "Seu ofício. Você trabalha com estas coisas: papel, bússola, mapa, mapa vazio, estandarte, moldura e vidro."),
                        Map.entry("nitwit", "Você não tem ofício. Você é o preguiçoso da vila: fala de comida, de sono e de fofoca, e fica olhando os outros trabalharem."),
                        Map.entry("child", "Você é criança e não tem ofício. Você fala de brincadeiras, de comida e do seu pai."),
                        Map.entry("villager", "Você ainda não tem ofício. Você fala de comida, da vila e do que gostaria de fazer.")
                )
        );
    }
}
