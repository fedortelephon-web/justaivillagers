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
                                - Ты никогда ничего не строишь и не чинишь: дома, стены, мосты и дороги делает игрок, а не ты.
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

        Map.entry("flee_child_line", "Папа! %s!"),
        Map.entry("flee_adult_line", "%s! Беги!"),
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

                        Map.entry("model_missing", "Модель %s не скачана. Откройте настройки клавишей O и нажмите «Скачать»."),
                        Map.entry("backend_down", "Ollama не запущена. Установите и запустите её: %s — затем откройте настройки клавишей O и нажмите «Проверить»."),
                        Map.entry("banner", "Откройте настройки клавишей O."),
                        Map.entry("settings.check.tip", "Проверить адрес и модель, введённые сейчас, не сохраняя настройки"),
                        Map.entry("backend_down.repeat", "Ollama всё ещё не запущена или указан неверный адрес сервера — жители не могут говорить. Настройки: клавиша O."),
                        Map.entry("settings.server.hint", "адрес сервера"),
                        Map.entry("settings.server.tip", "Адрес, где запущена Ollama или совместимый сервер. По умолчанию http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "имя модели"),
                        Map.entry("settings.model.custom.tip", "Любая модель, которая уже скачана в Ollama. Список: ollama list"),
                        Map.entry("settings.model.tip", "%s — скачать %s, нужно около %s ОЗУ."),
                        Map.entry("settings.model.get.tip", "Скачать модель %s через Ollama"),
                        Map.entry("settings.ollama.download.tip", "Скачать установщик Ollama. После скачивания запустите его вручную — мод не запускает программы сам."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "Самая быстрая и самая простая. Годится для коротких реплик, часто путается."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "Немного связнее предыдущей, хорошо держит тему разговора."),
                        Map.entry("settings.model.tip.gemma3_1b", "Хороший баланс для слабого компьютера: живая речь, мало ОЗУ."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Заметно живее и разговорчивее, но иногда уходит в сторону."),
                        Map.entry("settings.model.tip.gemma3_4b", "Лучшее качество речи из списка. Нужно около 5 ГБ свободной ОЗУ."),

                        Map.entry("screen.message", "Сообщение"),
                        Map.entry("screen.send", "Отправить"),
                        Map.entry("screen.nearby", "Жители рядом"),
                        Map.entry("screen.you", "Вы: "),
                        Map.entry("screen.thinking", "...думает"),
                        Map.entry("settings.title", "Just AI Villagers — настройки"),
                        Map.entry("settings.tab.model", "Модель"),
                        Map.entry("settings.tab.features", "Поведение"),
                        Map.entry("settings.tab.language", "Язык"),
                        Map.entry("settings.server", "Адрес сервера"),
                        Map.entry("settings.model", "Модель"),
                        Map.entry("settings.model.custom", "Своя"),
                        Map.entry("settings.model.needs", "загрузка %s, нужно около %s ОЗУ"),
                        Map.entry("settings.model.download", "Скачать модель"),

        Map.entry("settings.status.nomodel", "Сервер работает, %s не скачана"),
        Map.entry("settings.model.installed.tip", "Эта модель уже скачана — её можно выбрать без загрузки"),
        Map.entry("settings.model.failed", "Не удалось скачать %s: %s"),
        Map.entry("settings.model.done", "Модель %s скачана"),
                        Map.entry("settings.model.get", "Скачать"),
                        Map.entry("settings.model.installed", "установлена"),
                        Map.entry("settings.model.notinstalled", "не скачана"),
                        Map.entry("settings.model.downloading", "скачивание: %s"),
                        Map.entry("settings.ollama.title", "Ollama не запущена"),
                        Map.entry("settings.ollama.body", "Моду нужна Ollama. Скачай, установи, потом нажми «Проверить»."),
                        Map.entry("settings.ollama.download", "Скачать Ollama"),
                        Map.entry("settings.help", "Почему не работает?.."),
                        Map.entry("settings.help.tip", "Пошаговая инструкция: установка Ollama, запуск сервера, скачивание модели"),
                        Map.entry("help.page", "Страница %s из %s"),
                        Map.entry("help.prev", "Назад"),
                        Map.entry("help.next", "Далее"),

        Map.entry("help.title", "Как настроить общение с жителями"),
        Map.entry("help.copied", "Скопировано"),
        Map.entry("help.intro", "Чтобы жители могли отвечать вам, мод использует нейросеть, которая запускается прямо на вашем компьютере через программу Ollama. Всё работает локально и бесплатно, интернет нужен только один раз — чтобы скачать файлы."),
        Map.entry("help.step1", "## Шаг 1. Установите программу Ollama\nПерейдите на сайт ollama.com/download и установите программу для вашей системы:\n#copy|скопировать ссылку на сайт|https://ollama.com/download\n• Windows: скачайте файл OllamaSetup.exe, запустите и дождитесь окончания установки.\n• macOS: скачайте архив, распакуйте и перетащите иконку Ollama в папку «Программы».\n• Linux: откройте терминал и выполните команду: curl -fsSL https://ollama.com/install.sh | sh\n#copy|скопировать команду Linux для скачивания|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Шаг 2. Запустите сервер\nПрограмма должна работать в фоне во время игры:\n• Windows: после установки Ollama запускается сама (в правом нижнем углу экрана, возле часов, появится иконка ламы). Если её там нет — найдите Ollama через поиск Windows и запустите.\n• macOS: откройте Ollama из папки «Программы».\n• Linux: введите в терминале sudo systemctl start ollama\nЗапасной вариант: для запуска локального сервера ollama пропишите в терминал вашей операционной системы команду ollama serve.\n#copy|скопировать команду|ollama serve"),
        Map.entry("help.step3", "## Шаг 3. Скачайте «мозги» (языковую модель)\n1. В игре откройте меню настроек мода (по умолчанию — клавиша O).\n2. Выберите подходящую модель из списка. Совет: для первого запуска и слабых ПК лучше всего подойдёт Qwen2.5 0.5B (весит всего 398 МБ и отвечает быстрее всех).\n3. Нажмите «Скачать» и дождитесь, пока статус изменится на «Сохранено»."),
        Map.entry("help.step4", "## Шаг 4. Проверьте связь и начинайте игру\n1. В том же меню настроек нажмите кнопку «Проверить».\n2. Если появился статус «Подключено» — всё настроено верно! Закройте меню, подойдите к любому жителю и нажмите клавишу разговора.\n(Поле «Адрес сервера» оставьте по умолчанию: http://127.0.0.1:11434. Менять его нужно только продвинутым пользователям, если нейросеть запущена на другом ПК)."),
        Map.entry("help.ifnot", "## Частые проблемы и их решение\n• Ошибка «Нет подключения»\nПрограмма Ollama не запущена или заблокирована. Проверьте, висит ли иконка ламы в трее (возле часов). Если да, но ошибки не исчезают — убедитесь, что антивирус или брандмауэр Windows не блокируют Ollama.\n• Ошибка «Модель не скачана»\nСвязь с программой есть, но не загружена нейросеть. Вернитесь к Шагу 3 и нажмите «Скачать».\n• Жители думают слишком долго\nВашему компьютеру тяжело обрабатывать выбранную нейросеть. Выберите в настройках модель меньшего размера (например, 0.5B или 1.5B) и уменьшите максимальную длину ответа."),
                        Map.entry("help.close", "Закрыть"),
                        Map.entry("settings.ollama.copy", "Копировать ссылку"),
                        Map.entry("settings.ollama.command.copy", "Копировать команду"),
                        Map.entry("settings.ollama.cancel", "Отменить скачивание"),
                        Map.entry("settings.ollama.cancel.tip", "Остановить скачивание установщика"),
                        Map.entry("settings.ollama.copied", "Ссылка скопирована — вставьте её в адресную строку браузера."),
                        Map.entry("settings.ollama.command.copied", "Команда скопирована — вставьте её в PowerShell или cmd."),
                        Map.entry("settings.ollama.command.tip", "Копирует команду, которая запускает сервер Ollama"),
                        Map.entry("settings.ollama.paste.hint", "Скопированную команду вставьте в PowerShell или cmd (клавиши Win+R, введите cmd, Enter) и нажмите Enter. После этого нажмите «Проверить»."),
                        Map.entry("settings.ollama.page", "Открыть страницу"),
                        Map.entry("settings.ollama.saved", "Сохранено в %s — запусти и нажми «Проверить»."),
                        Map.entry("settings.check", "Проверить"),

        Map.entry("join.hint", "Настройки мода — клавиша %s. Команды: /justaivillagers help"),
        Map.entry("cmd.help.header", "Команды мода:"),
        Map.entry("cmd.help.status", "/justaivillagers status — сервер, модель и текущая загрузка"),
        Map.entry("cmd.help.settings", "/justaivillagers settings — открыть настройки"),
        Map.entry("cmd.help.reload", "/justaivillagers reload — перечитать файл настроек"),
        Map.entry("cmd.help.help", "/justaivillagers help — этот список"),
        Map.entry("cmd.status.header", "Состояние мода:"),
        Map.entry("cmd.status.address", "Сервер: %s"),
        Map.entry("cmd.status.model", "Модель: %s"),
        Map.entry("cmd.status.model.missing", "Модель: %s (не скачана)"),
        Map.entry("cmd.status.state.ok", "Сервер отвечает, можно разговаривать с жителями"),
        Map.entry("cmd.status.state.down", "Сервер не отвечает — жители молчат"),
        Map.entry("cmd.status.hint", "Настройки — клавиша %s"),
        Map.entry("cmd.reload.done", "Настройки перечитаны"),
        Map.entry("cmd.reload.same", "Файл не менялся: %s"),
                        Map.entry("settings.status.ok", "Подключено: %s"),
                        Map.entry("settings.status.down", "Нет подключения"),
                        Map.entry("settings.general", "Общее"),
                        Map.entry("settings.speech", "Речь"),
                        Map.entry("settings.advanced", "Дополнительно"),
                        Map.entry("settings.temperature", "Разнообразие речи"),
                        Map.entry("settings.maxTokens", "Длина ответа"),
                        Map.entry("settings.speechPace", "Скорость речи"),
                        Map.entry("settings.spontaneous", "Жители говорят сами"),
                        Map.entry("settings.group", "Двое жителей говорят вечером"),
                        Map.entry("settings.reactions", "Реагируют на удар"),
                        Map.entry("settings.fallback", "Говорят без модели"),

        Map.entry("settings.spontaneous.tip", "Житель время от времени сам обращается к вам, если вы стоите рядом. Выключите, если не хотите неожиданных реплик."),
        Map.entry("settings.group.tip", "Вечером двое жителей могут завести разговор друг с другом, а вы его услышите. Выключите, если нужны только разговоры с вами."),
        Map.entry("settings.reactions.tip", "Житель коротко огрызается, если вы его ударите. Выключите, чтобы на удар он молчал."),
        Map.entry("settings.fallback.tip", "Если модель не ответила или сервер недоступен, житель скажет готовую фразу вместо молчания. Выключите, чтобы он молчал при ошибке."),
        Map.entry("settings.debug.tip", "Показывает в чате запрос, который уходит модели, когда вы говорите с жителем. Нужно, чтобы понять, почему житель отвечает не то. В обычной игре выключено."),
        Map.entry("settings.temperature.tip", "Насколько разнообразно житель подбирает слова. Низко — отвечает предсказуемо и по делу, высоко — живее, но иногда невпопад."),
        Map.entry("settings.maxTokens.tip", "Сколько слов помещается в один ответ. Больше — житель может сказать больше, но ответ придётся ждать дольше."),
        Map.entry("settings.speechPace.tip", "С какой скоростью ответ появляется в окне разговора — по одной букве. 0 — сразу весь ответ, больше — медленнее и «живее»."),
        Map.entry("settings.history.tip", "Сколько последних реплик житель помнит в разговоре. Больше — лучше помнит контекст, но каждый ответ считается дольше."),
        Map.entry("settings.key.dialog.tip", "Клавиша, которой вы начинаете разговор с жителем. Нажмите на строку и нажмите нужную клавишу."),
        Map.entry("settings.key.settings.tip", "Клавиша, которой открывается это меню настроек. Нажмите на строку и нажмите нужную клавишу."),
        Map.entry("settings.debug", "Писать промпты в чат"),
                        Map.entry("settings.timeout", "Таймаут запроса"),
                        Map.entry("settings.history", "Длина памяти"),
                        Map.entry("settings.key.dialog", "Разговор с жителем"),
                        Map.entry("settings.key.settings", "Настройки мода"),
                        Map.entry("settings.key.press", "нажмите клавишу..."),
                        Map.entry("settings.language", "Язык жителей"),
                        Map.entry("settings.language.auto", "Как в игре"),
                        Map.entry("settings.save", "Сохранить"),
                        Map.entry("settings.cancel", "Отмена"),
                        Map.entry("settings.saved", "Сохранено. Изменения уже действуют."),
                        Map.entry("settings.close", "Закрыть")
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
                List.of("ясно", "дождь", "гроза"),
                                Map.ofEntries(
                        Map.entry("farmer", "Твоё ремесло. Ты работаешь вот с этим: пшеница, хлеб, морковь, картофель, свёкла, тыква, тяпка и компостер."),
                        Map.entry("librarian", "Твоё ремесло. Ты работаешь вот с этим: книга, книжная полка, бумага, книга с пером, чернильный мешок и фонарь."),
                        Map.entry("cleric", "Твоё ремесло. Ты работаешь вот с этим: редстоун, лазурит, светокамень, эндер-жемчуг, бутылочка опыта, гнилая плоть и варочная стойка."),
                        Map.entry("armorer", "Твоё ремесло. Ты работаешь вот с этим: железный слиток, уголь, ведро лавы, железный шлем, железный нагрудник, кольчуга, щит и колокол."),
                        Map.entry("weaponsmith", "Твоё ремесло. Ты работаешь вот с этим: железный слиток, уголь, кремень, железный меч, железный топор и точильный камень."),
                        Map.entry("toolsmith", "Твоё ремесло. Ты работаешь вот с этим: железный слиток, уголь, кремень, железная кирка, железная лопата, железная тяпка и колокол."),
                        Map.entry("butcher", "Твоё ремесло. Ты работаешь вот с этим: сырая свинина, сырая говядина, сырая баранина, сырая курица, жареная свинина и стейк."),
                        Map.entry("fisherman", "Твоё ремесло. Ты работаешь вот с этим: сырая треска, сырой лосось, тропическая рыба, удочка, ведро и костёр."),
                        Map.entry("fletcher", "Твоё ремесло. Ты работаешь вот с этим: палка, кремень, нить, перо, лук, арбалет и стрела."),
                        Map.entry("leatherworker", "Твоё ремесло. Ты работаешь вот с этим: кожа, седло, кожаная шапка, кожаная куртка, кожаные ботинки и кроличья шкурка."),
                        Map.entry("shepherd", "Твоё ремесло. Ты работаешь вот с этим: шерсть, ножницы, ковёр, кровать, картина и краситель."),
                        Map.entry("mason", "Твоё ремесло. Ты работаешь вот с этим: каменный кирпич, глиняный шарик, кирпич, терракота, кварц, полированный камень и андезит."),
                        Map.entry("cartographer", "Твоё ремесло. Ты работаешь вот с этим: бумага, компас, карта, пустая карта, флаг, рамка и стеклянная панель."),
                        Map.entry("nitwit", "У тебя нет ремесла. Ты деревенский бездельник: говоришь о еде, сне и сплетнях, а работают другие."),
                        Map.entry("child", "Ты ребёнок, у тебя нет ремесла. Ты говоришь об играх, еде и об отце."),
                        Map.entry("villager", "У тебя пока нет ремесла. Ты говоришь о еде, деревне и о том, чем хотел бы заняться.")
                )
        );
    }
}
