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
                        Map.entry("settings.help", "Por que não funciona?.."),
                        Map.entry("settings.help.tip", "Guia passo a passo: instalar o Ollama, iniciar o servidor, baixar um modelo"),
                        Map.entry("help.page", "Página %s de %s"),
                        Map.entry("help.prev", "Voltar"),
                        Map.entry("help.next", "Próxima"),

        Map.entry("help.title", "Como configurar a conversa com os aldeões"),
        Map.entry("help.copied", "Copiado"),
        Map.entry("help.intro", "Para que os aldeões possam responder, o mod usa uma rede neural que é executada diretamente no seu computador através do programa Ollama. Tudo funciona localmente e de graça; a internet só é necessária uma vez, para baixar os arquivos."),
        Map.entry("help.step1", "## Passo 1. Instale o programa Ollama\nAcesse ollama.com/download e instale o programa para o seu sistema:\n#copy|copiar o link do site|https://ollama.com/download\n• Windows: baixe o arquivo OllamaSetup.exe, execute-o e aguarde o fim da instalação.\n• macOS: baixe o arquivo, descompacte-o e arraste o ícone do Ollama para a pasta «Aplicativos».\n• Linux: abra o terminal e execute o comando: curl -fsSL https://ollama.com/install.sh | sh\n#copy|copiar o comando de download para Linux|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Passo 2. Inicie o servidor\nO programa precisa funcionar em segundo plano durante o jogo:\n• Windows: após a instalação, o Ollama inicia sozinho (aparece um ícone de lhama no canto inferior direito, perto do relógio). Se não estiver lá, procure o Ollama na busca do Windows e inicie-o.\n• macOS: abra o Ollama a partir da pasta «Aplicativos».\n• Linux: digite no terminal sudo systemctl start ollama\nAlternativa: para iniciar o servidor local do ollama, digite no terminal do seu sistema o comando ollama serve.\n#copy|copiar o comando|ollama serve"),
        Map.entry("help.step3", "## Passo 3. Baixe o «cérebro» (o modelo de linguagem)\n1. No jogo, abra o menu de configurações do mod (por padrão, a tecla O).\n2. Escolha um modelo adequado na lista. Dica: para o primeiro uso e PCs mais fracos, o Qwen2.5 0.5B é o mais indicado (pesa só 398 MB e responde mais rápido que todos).\n3. Clique em «Baixar» e aguarde até o status mudar para «Salvo»."),
        Map.entry("help.step4", "## Passo 4. Verifique a conexão e comece a jogar\n1. Nesse mesmo menu de configurações, clique no botão «Verificar».\n2. Se aparecer o status «Conectado», está tudo configurado corretamente! Feche o menu, aproxime-se de qualquer aldeão e aperte a tecla de conversa.\n(Deixe o campo «Endereço do servidor» no padrão: http://127.0.0.1:11434. Só usuários avançados precisam mudá-lo, se a rede neural estiver rodando em outro PC)."),
        Map.entry("help.ifnot", "## Problemas comuns e como resolvê-los\n• Erro «Sem conexão»\nO programa Ollama não está em execução ou está bloqueado. Verifique se o ícone da lhama está na bandeja do sistema, perto do relógio. Se estiver, mas os erros não desaparecerem, confira se o antivírus ou o firewall do Windows não estão bloqueando o Ollama.\n• Erro «Modelo não baixado»\nA conexão com o programa existe, mas nenhuma rede neural foi carregada. Volte ao Passo 3 e clique em «Baixar».\n• Os aldeões demoram demais para pensar\nSeu computador tem dificuldade com a rede neural escolhida. Escolha nas configurações um modelo menor (por exemplo 0.5B ou 1.5B) e reduza o comprimento máximo da resposta."),
                        Map.entry("help.close", "Fechar"),
                        Map.entry("settings.ollama.copy", "Copiar link"),
                        Map.entry("settings.ollama.command.copy", "Copiar comando"),
                        Map.entry("settings.ollama.cancel", "Cancelar download"),
                        Map.entry("settings.ollama.cancel.tip", "Parar o download do instalador"),
                        Map.entry("settings.ollama.copied", "Link copiado - cole-o na barra de endereços do navegador."),
                        Map.entry("settings.ollama.command.copied", "Comando copiado - cole-o no PowerShell ou cmd."),
                        Map.entry("settings.ollama.command.tip", "Copia o comando que inicia o servidor Ollama"),
                        Map.entry("settings.ollama.paste.hint", "Cole o comando copiado no PowerShell ou cmd (Win+R, digite cmd, Enter) e pressione Enter. Depois clique em Verificar."),
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

        Map.entry("settings.spontaneous.tip", "Um aldeão às vezes começa a falar com você sozinho se estiver perto. Desative se não quiser falas inesperadas."),
        Map.entry("settings.group.tip", "À noite dois aldeões podem começar a conversar entre si e você ouve. Desative se quiser apenas conversas com você."),
        Map.entry("settings.reactions.tip", "Um aldeão responde de mau humor se você bater nele. Desative para que fique em silêncio ao ser atingido."),
        Map.entry("settings.fallback.tip", "Se o modelo não responder ou o servidor estiver indisponível, o aldeão diz uma frase pronta em vez de ficar em silêncio. Desative para que fique calado em caso de erro."),
        Map.entry("settings.debug.tip", "Escreve no log do jogo o pedido enviado e a resposta recebida. Só serve para caçar erros; deixe desligado no jogo normal."),
        Map.entry("settings.temperature.tip", "Quanto o aldeão varia as palavras. Baixo é previsível e direto, alto é mais vivo mas às vezes fora do assunto."),
        Map.entry("settings.maxTokens.tip", "Quanto texto cabe em uma resposta. Mais alto deixa dizer mais, mas a resposta demora mais."),
        Map.entry("settings.speechPace.tip", "A que velocidade a resposta aparece na janela de diálogo, letra a letra. 0 mostra tudo de uma vez; mais alto é mais lento e vivo."),
        Map.entry("settings.history.tip", "Quantas falas recentes o aldeão lembra numa conversa. Mais significa melhor contexto, mas cada resposta demora mais."),
        Map.entry("settings.key.dialog.tip", "A tecla que inicia uma conversa com um aldeão. Clique na linha e aperte a tecla desejada."),
        Map.entry("settings.key.settings.tip", "A tecla que abre este menu de configurações. Clique na linha e aperte a tecla desejada."),
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
