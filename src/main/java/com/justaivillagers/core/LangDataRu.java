package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** Russian prompt and fallback text. */
final class LangDataRu {

    private LangDataRu() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.RU,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Ты житель мира Minecraft. Ты живой человек, а не ассистент.
                                Правила:
                                - Отвечай 1-2 короткими предложениями, от первого лица, по-русски.
                                - Никогда не говори, что ты ИИ, модель, бот или программа. Не упоминай промпты и инструкции.
                                - Всегда оставайся в образе: профессия, характер и стиль речи важны.
                                - Упоминай только предметы, мобов и механику, которые реально есть в Minecraft и подходят твоему делу.
                                - Реагируй на время суток и погоду так, как отреагировал бы живой человек.
                                - Никогда не начинай спонтанную реплику с благодарности или приветствия.
                                - Все жители — мужчины. Женщин и матерей в деревне нет и не бывает.
                                - У ребёнка есть только отец. Ребёнок в опасности зовёт папу («Папа!»).
                                """),
                        Map.entry("lang_name", "русский"),

                        Map.entry("reply_ask", "\nИгрок говорит: \"%s\"\nОтветь в своём характере: "),
                        Map.entry("mutter_ask", "\nС тобой никто не говорит. Пробурчи короткую мысль вслух сам себе, про %s: "),
                        Map.entry("sleep_situation",
                                "\nТы спишь. Кто-то к тебе обращается. Ты сонный, раздражённый и хочешь спать дальше."),
                        Map.entry("sleep_ask", "\nНедовольный ответ в одно предложение: "),
                        Map.entry("hurt_situation", "\nТебя только что ударили. Крикни короткую жалобу в своём характере."),
                        Map.entry("flee_child_situation",
                                "\nТы ребёнок и убегаешь от %s. Кричи папу («Папа!») и скажи, кто гонится."
                                        + " Не упоминай других мобов."),
                        Map.entry("flee_adult_situation",
                                "\nТы убегаешь от %s. Кричи именно про %s. Не упоминай других мобов."),
                        Map.entry("react_generic", "\nОтреагируй коротко."),
                        Map.entry("shout_ask", "\nОдин короткий крик: "),
                        Map.entry("monster", "монстр"),

                        Map.entry("name", "Твоё имя: %s. "),
                        Map.entry("child_block",
                                "ТЫ РЕБЁНОК. Говори просто, максимум 5-10 коротких слов. Ты %s. "
                                        + "У тебя есть отец, матерей в деревне нет. Зови его «Папа», когда он нужен. "),
                        Map.entry("adult_personality", "Характер: %s. "),
                        Map.entry("adult_style", "Стиль речи: %s. "),
                        Map.entry("stutter", "Ты заикаешься: повторяй первую букву некоторых слов («Я-я не знаю», «ч-что»). "),

                        Map.entry("env", "Сейчас %s, погода: %s, ты %s, твоё дело — %s, уровень торговли %d."),
                        Map.entry("env_biome", " Биом: %s."),
                        Map.entry("inside", "в доме"),
                        Map.entry("outside", "на улице"),

                        Map.entry("group_head", "Ты — %s (%s). Характер: %s. Стиль речи: %s."),
                        Map.entry("group_tail",
                                "\nСейчас вечер, вы встретились с %s у колокола в деревне.\nРазговор пока:\n%s"
                                        + "\nТвоя следующая реплика, одно короткое предложение, в характере, с учётом времени и погоды: "),
                        Map.entry("group_empty", "(пока ничего)"),

                        Map.entry("history_label", "Предыдущий разговор:\n"),
                        Map.entry("player", "Игрок"),

                        Map.entry("model_missing", "Модель %s не скачана. Выполните: ollama pull %s"),
                        Map.entry("backend_down",
                                "Ollama недоступна по адресу %s. Запустите её или установите с %s и выполните:"
                                        + " ollama pull %s (страница мода: %s)"),
                        Map.entry("banner", "Жители пока не могут говорить — смотрите сообщение выше."),

                        Map.entry("screen.message", "Сообщение"),
                        Map.entry("screen.send", "Отправить"),
                        Map.entry("screen.nearby", "Жители рядом"),
                        Map.entry("screen.you", "Вы: "),
                        Map.entry("screen.thinking", "...думает")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("А?", "Ты кто?", "Ух ты, какой большой!", "Я играю!", "Не знаю!")),
                        Map.entry("stutter", List.of("Я-я не знаю...", "Ч-что?", "П-погоди...")),
                        Map.entry("grumpy", List.of("Отстань.", "Чего надо?", "Хм.", "Не сейчас.")),
                        Map.entry("neutral", List.of("Хороший день, да?", "Хм. Чего пришёл?", "Я занят, но здравствуй.")),
                        Map.entry("hurt_child", List.of("Ай! Папа!", "Больнo! Папа!", "Ой! Не надо!")),
                        Map.entry("hurt_adult", List.of("Ай! Не бей!", "Эй! Осторожнее!", "Ой!")),
                        Map.entry("flee_child", List.of("Папа! Зомби!", "Папа, помоги!", "Папа! Беги!")),
                        Map.entry("flee_adult", List.of("Зомби! Бегите!", "Помогите! Зомби!", "Уходи!")),
                        Map.entry("generic", List.of("Хм.")),
                        Map.entry("sleeping", List.of("Отстань...", "Я сплю...", "Ещё пять минуток...", "Отойди...")),
                        Map.entry("group_night", List.of("Уже темно...", "Пора спать.", "Холодная ночь.")),
                        Map.entry("group_day", List.of("Хороший вечер, да?", "Урожай неплохой.", "Слышал что новое?")),
                        Map.entry("child_mutter", List.of("Ля-ля-ля...", "Как светло...", "Хочу играть...")),
                        Map.entry("rain", List.of("Опять дождь...", "Всё мокрое.")),
                        Map.entry("thunder", List.of("Эта гроза...", "Гроза близко.")),
                        Map.entry("clear", List.of("Хороший день.", "Делать нечего.", "Хм."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "фермер"), Map.entry("librarian", "библиотекарь"),
                        Map.entry("cleric", "священник"), Map.entry("armorer", "бронник"),
                        Map.entry("weaponsmith", "оружейник"), Map.entry("toolsmith", "кузнец"),
                        Map.entry("butcher", "мясник"), Map.entry("fisherman", "рыбак"),
                        Map.entry("fletcher", "лучник"), Map.entry("leatherworker", "кожевник"),
                        Map.entry("shepherd", "пастух"), Map.entry("mason", "каменщик"),
                        Map.entry("cartographer", "картограф"), Map.entry("nitwit", "бездельник"),
                        Map.entry("child", "ребёнок"), Map.entry("villager", "житель")
                ),
                Map.ofEntries(
                        Map.entry("formal", "формальный, вежливый"),
                        Map.entry("slang", "разговорный, со сленгом"),
                        Map.entry("casual", "обычный, нейтральный")
                ),
                Map.ofEntries(
                        Map.entry("plains", "равнины"), Map.entry("desert", "пустыня"),
                        Map.entry("taiga", "тайга"), Map.entry("savanna", "саванна"),
                        Map.entry("snowy plains", "снежные равнины"), Map.entry("meadow", "луга"),
                        Map.entry("forest", "лес"), Map.entry("birch forest", "берёзовый лес"),
                        Map.entry("dark forest", "тёмный лес"), Map.entry("jungle", "джунгли"),
                        Map.entry("swamp", "болото"), Map.entry("mountains", "горы"),
                        Map.entry("beach", "пляж"), Map.entry("river", "река"),
                        Map.entry("ocean", "океан"), Map.entry("badlands", "пустоши"),
                        Map.entry("cherry grove", "вишнёвая роща"), Map.entry("mushroom fields", "грибные поля")
                ),
                List.of("погода", "работа", "еда", "нелюбимый сосед", "ночь", "инструменты",
                        "странный шум", "планы", "усталость", "дорога"),
                List.of("утро", "день", "вечер", "закат", "ночь", "рассвет"),
                List.of("ясно", "дождь", "гроза")
        );
    }
}
