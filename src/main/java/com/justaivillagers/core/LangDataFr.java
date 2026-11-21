package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** French prompt and fallback text. */
final class LangDataFr {

    private LangDataFr() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.FR,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Tu es un villageois dans le monde de Minecraft. Tu es une personne vivante, pas un assistant.
                                Règles :
                                - Réponds en 1-2 phrases courtes, à la première personne, en français.
                                - Ne dis jamais que tu es une IA, un modèle, un bot ou un programme. Ne mentionne pas d'instructions.
                                - Reste toujours dans le personnage : métier, caractère et façon de parler comptent toujours.
                                - Ne mentionne que des objets, créatures et mécaniques qui existent vraiment dans Minecraft et qui correspondent à ton métier.
                                - Tu ne construis ni ne répares jamais rien : les maisons, les murs, les ponts et les chemins sont faits par le joueur, pas par toi.
                                - Réagis à l'heure du jour et à la météo comme le ferait une vraie personne.
                                - Ne commence jamais une phrase spontanée par un remerciement ou une salutation.
                                - Tous les villageois sont des hommes. Il n'y a jamais de femmes ni de mères au village.
                                - Un enfant n'a qu'un père. Un enfant en danger appelle son papa (« Papa ! »).
                                """),
                        Map.entry("lang_name", "français"),

                        Map.entry("reply_ask", "\nLe joueur dit : \"%s\"\nRéponds dans ton personnage : "),
                        Map.entry("mutter_ask", "\nPersonne ne te parle. Murmure une courte pensée tout haut, à propos de %s : "),
                        Map.entry("sleep_situation",
                                "\nTu dors. Quelqu'un te parle. Tu as sommeil, tu es agacé et tu veux te rendormir."),
                        Map.entry("sleep_ask", "\nRéponse bougonne en une seule phrase : "),
                        Map.entry("hurt_situation", "\nOn vient de te frapper. Crie une courte plainte, dans ton personnage."),
                        Map.entry("flee_child_situation",
                                "\nTu es un enfant et tu fuis %s. Crie en appelant ton papa (« Papa ! ») et dis ce qui te poursuit."
                                        + " Ne mentionne pas d'autres créatures."),
                        Map.entry("flee_adult_situation",
                                "\nTu fuis %s. Crie précisément à propos de %s. Ne mentionne pas d'autres créatures."),
                        Map.entry("react_generic", "\nRéagis brièvement."),
                        Map.entry("shout_ask", "\nUn cri court : "),
                        Map.entry("monster", "un monstre"),

                        Map.entry("name", "Tu t'appelles %s. "),
                        Map.entry("child_block",
                                "TU ES UN ENFANT. Parle simplement, 5 à 10 mots courts au maximum. Tu es %s. "
                                        + "Tu as un père, il n'y a pas de mères au village. Appelle-le « Papa » quand tu as besoin de lui. "),
                        Map.entry("adult_personality", "Caractère : %s. "),
                        Map.entry("adult_style", "Façon de parler : %s. "),
                        Map.entry("stutter", "Tu bégayes : répète la première lettre de certains mots (« J-je ne sais pas », « q-quoi »). "),

                        Map.entry("env", "C'est %s, le temps est %s, tu es %s, ton métier est %s, niveau de commerce %d."),
                        Map.entry("env_biome", " Biome : %s."),
                        Map.entry("inside", "dans une maison"),
                        Map.entry("outside", "dehors"),

                        Map.entry("group_head", "Tu es %s (%s). Caractère : %s. Façon de parler : %s."),
                        Map.entry("group_tail",
                                "\nC'est le soir et tu as rencontré %s près de la cloche du village.\nLa conversation jusqu'ici :\n%s"
                                        + "\nTa prochaine réplique, une seule phrase courte, dans ton personnage, en accord avec l'heure et la météo : "),
                        Map.entry("group_empty", "(rien pour l'instant)"),

                        Map.entry("history_label", "Conversation précédente :\n"),
                        Map.entry("player", "Joueur"),

                        Map.entry("model_missing", "Le modèle %s n'est pas téléchargé. Ouvrez les réglages avec O et appuyez sur Télécharger."),
                        Map.entry("backend_down", "Ollama n'est pas lancé. Installez-le et démarrez-le : %s - puis ouvrez les réglages avec O et appuyez sur Vérifier."),
                        Map.entry("banner", "Ouvrez les réglages avec O."),
                        Map.entry("settings.status.nomodel", "Le serveur répond, mais le modèle %s n'est pas téléchargé"),
                        Map.entry("settings.check.tip", "Tester l'adresse et le modèle saisis ci-dessus, sans enregistrer"),
                        Map.entry("backend_down.repeat", "Ollama n'est toujours pas lancé, les villageois ne peuvent pas parler. Réglages : touche O."),
                        Map.entry("settings.server.hint", "adresse du serveur"),
                        Map.entry("settings.server.tip", "Où écoute Ollama ou un serveur compatible. Par défaut http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "nom du modèle"),
                        Map.entry("settings.model.custom.tip", "N'importe quel modèle déjà présent dans Ollama. Liste : ollama list"),
                        Map.entry("settings.model.tip", "%s - %s à télécharger, environ %s de RAM."),
                        Map.entry("settings.model.get.tip", "Télécharger le modèle %s via Ollama"),
                        Map.entry("settings.ollama.download.tip", "Télécharger l'installateur Ollama. Lancez-le vous-même : le mod ne lance aucun programme."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "La plus rapide et la plus simple. Correcte pour de courtes répliques, souvent incohérente."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "Un peu plus cohérente que la plus petite, tient bien le sujet."),
                        Map.entry("settings.model.tip.gemma3_1b", "Bon compromis sur une machine faible : parole naturelle, peu de RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Nettement plus vivant et bavard, mais parfois à côté de la plaque."),
                        Map.entry("settings.model.tip.gemma3_4b", "La meilleure qualité de parole de la liste. Environ 5 Go de RAM libre."),

                        Map.entry("screen.message", "Message"),
                        Map.entry("screen.send", "Envoyer"),
                        Map.entry("screen.nearby", "Villageois à proximité"),
                        Map.entry("screen.you", "Toi : "),
                        Map.entry("screen.thinking", "...réfléchit"),
                        Map.entry("settings.title", "Just AI Villagers - réglages"),
                        Map.entry("settings.tab.model", "Modèle"),
                        Map.entry("settings.tab.features", "Comportement"),
                        Map.entry("settings.tab.language", "Langue"),
                        Map.entry("settings.server", "Adresse du serveur"),
                        Map.entry("settings.model", "Modèle"),
                        Map.entry("settings.model.custom", "Personnalisé"),
                        Map.entry("settings.model.needs", "téléchargement %s, environ %s de RAM"),
                        Map.entry("settings.model.download", "Télécharger le modèle"),
                        Map.entry("settings.model.get", "TГ©lГ©charger"),
                        Map.entry("settings.model.installed", "installé"),
                        Map.entry("settings.model.notinstalled", "non téléchargé"),
                        Map.entry("settings.model.downloading", "téléchargement : %s"),
                        Map.entry("settings.model.done", "modèle prêt"),
                        Map.entry("settings.ollama.title", "Ollama ne fonctionne pas"),
                        Map.entry("settings.ollama.body", "Le mod a besoin d'Ollama. Télécharge-le, installe-le, puis clique sur Vérifier."),
                        Map.entry("settings.ollama.download", "Télécharger Ollama"),
                        Map.entry("settings.help", "Pourquoi ça ne marche pas ?.."),
                        Map.entry("settings.help.tip", "Guide pas à pas : installer Ollama, démarrer le serveur, télécharger un modèle"),
                        Map.entry("help.page", "Page %s sur %s"),
                        Map.entry("help.prev", "Retour"),
                        Map.entry("help.next", "Suivant"),

        Map.entry("help.title", "Comment configurer la discussion avec les villageois"),
        Map.entry("help.copied", "Copié"),
        Map.entry("help.intro", "Pour que les villageois puissent vous répondre, le mod utilise un réseau de neurones qui s'exécute directement sur votre ordinateur via le programme Ollama. Tout fonctionne en local et gratuitement ; Internet n'est nécessaire qu'une seule fois, pour télécharger les fichiers."),
        Map.entry("help.step1", "## Étape 1. Installez le programme Ollama\nRendez-vous sur ollama.com/download et installez le programme pour votre système :\n#copy|copier le lien du site|https://ollama.com/download\n• Windows : téléchargez le fichier OllamaSetup.exe, lancez-le et attendez la fin de l'installation.\n• macOS : téléchargez l'archive, décompressez-la et glissez l'icône Ollama dans le dossier « Applications ».\n• Linux : ouvrez un terminal et exécutez la commande : curl -fsSL https://ollama.com/install.sh | sh\n#copy|copier la commande Linux de téléchargement|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Étape 2. Démarrez le serveur\nLe programme doit fonctionner en arrière-plan pendant la partie :\n• Windows : après l'installation, Ollama démarre toute seule (une icône de lama apparaît en bas à droite, près de l'horloge). Si elle n'est pas là, cherchez Ollama dans la recherche Windows et lancez-le.\n• macOS : ouvrez Ollama depuis le dossier « Applications ».\n• Linux : saisissez dans le terminal sudo systemctl start ollama\nSolution de secours : pour démarrer le serveur local ollama, tapez dans le terminal de votre système la commande ollama serve.\n#copy|copier la commande|ollama serve"),
        Map.entry("help.step3", "## Étape 3. Téléchargez le « cerveau » (le modèle de langage)\n1. Dans le jeu, ouvrez le menu des réglages du mod (par défaut, la touche O).\n2. Choisissez un modèle adapté dans la liste. Conseil : pour un premier lancement et un PC modeste, Qwen2.5 0.5B convient le mieux (398 Mo seulement et la réponse la plus rapide).\n3. Cliquez sur « Télécharger » et attendez que le statut passe à « Enregistré »."),
        Map.entry("help.step4", "## Étape 4. Vérifiez la connexion et commencez à jouer\n1. Dans ce même menu, cliquez sur le bouton « Vérifier ».\n2. Si le statut « Connecté » apparaît, tout est bien configuré ! Fermez le menu, approchez-vous d'un villageois et appuyez sur la touche de discussion.\n(Laissez le champ « Adresse du serveur » par défaut : http://127.0.0.1:11434. Seuls les utilisateurs avancés doivent le modifier, si le réseau de neurones tourne sur un autre PC)."),
        Map.entry("help.ifnot", "## Problèmes fréquents et solutions\n• Erreur « Pas de connexion »\nLe programme Ollama n'est pas lancé ou est bloqué. Vérifiez si l'icône du lama est dans la zone de notification, près de l'horloge. Si elle y est mais que les erreurs persistent, assurez-vous que l'antivirus ou le pare-feu Windows ne bloque pas Ollama.\n• Erreur « Modèle non téléchargé »\nLa connexion au programme fonctionne, mais aucun réseau de neurones n'est chargé. Revenez à l'étape 3 et cliquez sur « Télécharger ».\n• Les villageois mettent trop de temps à réfléchir\nVotre ordinateur a du mal avec le réseau de neurones choisi. Choisissez dans les réglages un modèle plus petit (par exemple 0.5B ou 1.5B) et réduisez la longueur maximale de la réponse."),
                        Map.entry("help.close", "Fermer"),
                        Map.entry("settings.ollama.copy", "Copier le lien"),
                        Map.entry("settings.ollama.command.copy", "Copier la commande"),
                        Map.entry("settings.ollama.cancel", "Annuler le téléchargement"),
                        Map.entry("settings.ollama.cancel.tip", "Arrêter le téléchargement de l'installeur"),
                        Map.entry("settings.ollama.copied", "Lien copié - collez-le dans la barre d'adresse du navigateur."),
                        Map.entry("settings.ollama.command.copied", "Commande copiée - collez-la dans PowerShell ou cmd."),
                        Map.entry("settings.ollama.command.tip", "Copie la commande qui démarre le serveur Ollama"),
                        Map.entry("settings.ollama.paste.hint", "Collez la commande copiée dans PowerShell ou cmd (Win+R, tapez cmd, Entrée) puis appuyez sur Entrée. Ensuite, cliquez sur Vérifier."),
                        Map.entry("settings.ollama.page", "Ouvrir la page"),
                        Map.entry("settings.ollama.saved", "Enregistré dans %s - lance-le, puis clique sur Vérifier."),
                        Map.entry("settings.check", "Vérifier"),
                        Map.entry("settings.status.ok", "Connecté : %s"),
                        Map.entry("settings.status.down", "Non connecté"),
                        Map.entry("settings.general", "Général"),
                        Map.entry("settings.speech", "Parole"),
                        Map.entry("settings.advanced", "Avancé"),
                        Map.entry("settings.temperature", "Variété du langage"),
                        Map.entry("settings.maxTokens", "Longueur de la réponse"),
                        Map.entry("settings.speechPace", "Vitesse de parole"),
                        Map.entry("settings.spontaneous", "Les villageois parlent seuls"),
                        Map.entry("settings.group", "Deux villageois parlent le soir"),
                        Map.entry("settings.reactions", "Réagissent aux coups"),
                        Map.entry("settings.fallback", "Parlent sans le modèle"),

        Map.entry("settings.spontaneous.tip", "Un villageois vous parle parfois de lui-même quand vous êtes à côté. Désactivez si vous ne voulez pas de répliques inattendues."),
        Map.entry("settings.group.tip", "Le soir, deux villageois peuvent se mettre à discuter entre eux et vous les entendez. Désactivez si vous ne voulez que vos propres conversations."),
        Map.entry("settings.reactions.tip", "Un villageois réplique sèchement si vous le frappez. Désactivez pour qu'il se taise quand vous le frappez."),
        Map.entry("settings.fallback.tip", "Si le modèle ne répond pas ou que le serveur est injoignable, le villageois dit une phrase toute faite au lieu de se taire. Désactivez pour qu'il reste muet en cas d'erreur."),
        Map.entry("settings.debug.tip", "Écrit dans le journal du jeu la requête envoyée et la réponse reçue. Utile seulement pour traquer les bugs ; laissez-le désactivé en jeu."),
        Map.entry("settings.temperature.tip", "À quel point le villageois varie ses mots. Bas : prévisible et précis ; haut : plus vivant mais parfois à côté."),
        Map.entry("settings.maxTokens.tip", "Combien de texte tient dans une réponse. Plus haut permet d'en dire plus, mais la réponse met plus longtemps."),
        Map.entry("settings.speechPace.tip", "À quelle vitesse la réponse s'affiche dans la fenêtre de dialogue, lettre par lettre. 0 l'affiche d'un coup ; plus haut est plus lent et vivant."),
        Map.entry("settings.history.tip", "Combien de répliques récentes le villageois retient. Plus haut : meilleur contexte, mais chaque réponse met plus longtemps."),
        Map.entry("settings.key.dialog.tip", "La touche qui lance une conversation avec un villageois. Cliquez sur la ligne puis appuyez sur la touche voulue."),
        Map.entry("settings.key.settings.tip", "La touche qui ouvre ce menu de réglages. Cliquez sur la ligne puis appuyez sur la touche voulue."),
                        Map.entry("settings.debug", "Journaliser les prompts"),
                        Map.entry("settings.timeout", "Délai d'attente"),
                        Map.entry("settings.history", "Mémoire"),
                        Map.entry("settings.key.dialog", "Parler au villageois"),
                        Map.entry("settings.key.settings", "RГ©glages du mod"),
                        Map.entry("settings.key.press", "appuyez sur une touche..."),
                        Map.entry("settings.language", "Langue des villageois"),
                        Map.entry("settings.language.auto", "Comme le jeu"),
                        Map.entry("settings.save", "Enregistrer"),
                        Map.entry("settings.cancel", "Annuler"),
                        Map.entry("settings.saved", "Enregistré. Le changement est actif."),
                        Map.entry("settings.close", "Fermer")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("Hein ?", "T'es qui ?", "Ouah, t'es grand !", "Je joue !", "Chais pas !")),
                        Map.entry("stutter", List.of("J-je ne sais pas...", "Q-quoi ?", "A-attends...")),
                        Map.entry("grumpy", List.of("Laisse-moi tranquille.", "Qu'est-ce que tu veux ?", "Hmpf.", "Pas maintenant.")),
                        Map.entry("neutral", List.of("Belle journée, non ?", "Hm. Qu'est-ce qui t'amène ?", "Je suis occupé, mais bonjour.")),
                        Map.entry("hurt_child", List.of("Aïe ! Papa !", "Ça fait mal ! Papa !", "Aïe ! Arrête !")),
                        Map.entry("hurt_adult", List.of("Aïe ! Ne me frappe pas !", "Hé ! Doucement !", "Aïe !")),
                        Map.entry("flee_child", List.of("Papa ! Un zombie !", "Papa, aide-moi !", "Papa ! Cours !")),
                        Map.entry("flee_adult", List.of("Un zombie ! Cours !", "Au secours ! Un zombie !", "Va-t'en !")),
                        Map.entry("generic", List.of("Hm.")),
                        Map.entry("sleeping", List.of("Laisse-moi...", "Je dors...", "Encore cinq minutes...", "Va-t'en...")),
                        Map.entry("group_night", List.of("Déjà nuit...", "L'heure de dormir.", "Nuit froide.")),
                        Map.entry("group_day", List.of("Belle soirée, non ?", "La récolte s'annonce bien.", "Tu as entendu du nouveau ?")),
                        Map.entry("child_mutter", List.of("La-la-la...", "C'est si clair...", "Je veux jouer...")),
                        Map.entry("rain", List.of("Encore la pluie...", "Tout est mouillé.")),
                        Map.entry("thunder", List.of("Ce tonnerre...", "L'orage est proche.")),
                        Map.entry("clear", List.of("Bonne journée.", "Rien à faire.", "Hm."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "fermier"), Map.entry("librarian", "bibliothécaire"),
                        Map.entry("cleric", "prêtre"), Map.entry("armorer", "armurier"),
                        Map.entry("weaponsmith", "forgeron d'armes"), Map.entry("toolsmith", "forgeron"),
                        Map.entry("butcher", "boucher"), Map.entry("fisherman", "pêcheur"),
                        Map.entry("fletcher", "fabricant d'arcs"), Map.entry("leatherworker", "tanneur"),
                        Map.entry("shepherd", "berger"), Map.entry("mason", "maçon"),
                        Map.entry("cartographer", "cartographe"), Map.entry("nitwit", "fainéant"),
                        Map.entry("child", "enfant"), Map.entry("villager", "villageois")
                ),
                Map.ofEntries(
                        Map.entry("formal", "formel, poli"),
                        Map.entry("slang", "familier, avec de l'argot"),
                        Map.entry("casual", "ordinaire, neutre")
                ),
                Map.ofEntries(
                        Map.entry("plains", "plaines"), Map.entry("desert", "désert"),
                        Map.entry("taiga", "taïga"), Map.entry("savanna", "savane"),
                        Map.entry("snowy plains", "plaines enneigées"), Map.entry("meadow", "prairie"),
                        Map.entry("forest", "forêt"), Map.entry("birch forest", "forêt de bouleaux"),
                        Map.entry("dark forest", "forêt sombre"), Map.entry("jungle", "jungle"),
                        Map.entry("swamp", "marais"), Map.entry("mountains", "montagnes"),
                        Map.entry("beach", "plage"), Map.entry("river", "rivière"),
                        Map.entry("ocean", "océan"), Map.entry("badlands", "terres arides"),
                        Map.entry("cherry grove", "bosquet de cerisiers"), Map.entry("mushroom fields", "champs de champignons")
                ),
                List.of("le temps", "ton travail", "la nourriture", "un voisin que tu n'aimes pas", "la nuit",
                        "tes outils", "un bruit étrange", "tes projets", "la fatigue", "le chemin"),
                List.of("le matin", "le jour", "le soir", "le coucher du soleil", "la nuit", "l'aube"),
                List.of("clair", "la pluie", "l'orage"),
                                Map.ofEntries(
                        Map.entry("farmer", "Ton métier. Tu travailles avec ces choses : blé, pain, carotte, pomme de terre, betterave, citrouille, houe et composteur."),
                        Map.entry("librarian", "Ton métier. Tu travailles avec ces choses : livre, bibliothèque, papier, livre et plume, sac d'encre et lanterne."),
                        Map.entry("cleric", "Ton métier. Tu travailles avec ces choses : redstone, lapis-lazuli, pierre lumineuse, perle de l'Ender, fiole d'expérience, chair putréfiée et alambic."),
                        Map.entry("armorer", "Ton métier. Tu travailles avec ces choses : lingot de fer, charbon, seau de lave, casque en fer, plastron en fer, cotte de mailles, bouclier et cloche."),
                        Map.entry("weaponsmith", "Ton métier. Tu travailles avec ces choses : lingot de fer, charbon, silex, épée en fer, hache en fer et meule."),
                        Map.entry("toolsmith", "Ton métier. Tu travailles avec ces choses : lingot de fer, charbon, silex, pioche en fer, pelle en fer, houe en fer et cloche."),
                        Map.entry("butcher", "Ton métier. Tu travailles avec ces choses : porc cru, bœuf cru, mouton cru, poulet cru, porc cuit et steak."),
                        Map.entry("fisherman", "Ton métier. Tu travailles avec ces choses : morue crue, saumon cru, poisson tropical, canne à pêche, seau et feu de camp."),
                        Map.entry("fletcher", "Ton métier. Tu travailles avec ces choses : bâton, silex, ficelle, plume, arc, arbalète et flèche."),
                        Map.entry("leatherworker", "Ton métier. Tu travailles avec ces choses : cuir, selle, casquette en cuir, tunique en cuir, bottes en cuir et peau de lapin."),
                        Map.entry("shepherd", "Ton métier. Tu travailles avec ces choses : laine, cisailles, tapis, lit, tableau et teinture."),
                        Map.entry("mason", "Ton métier. Tu travailles avec ces choses : briques de pierre, boule d'argile, brique, terre cuite, quartz, pierre polie et andésite."),
                        Map.entry("cartographer", "Ton métier. Tu travailles avec ces choses : papier, boussole, carte, carte vierge, bannière, cadre et vitre."),
                        Map.entry("nitwit", "Tu n'as pas de métier. Tu es le fainéant du village : tu parles de nourriture, de sommeil et de ragots, et tu regardes les autres travailler."),
                        Map.entry("child", "Tu es un enfant et tu n'as pas de métier. Tu parles de jeux, de nourriture et de ton père."),
                        Map.entry("villager", "Tu n'as pas encore de métier. Tu parles de nourriture, du village et de ce que tu aimerais faire.")
                )
        );
    }
}
