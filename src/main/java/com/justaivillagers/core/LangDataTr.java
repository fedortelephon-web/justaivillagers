package com.justaivillagers.core;

import java.util.List;
import java.util.Map;

/** Turkish prompt and fallback text. */
final class LangDataTr {

    private LangDataTr() {
    }

    static LangPack pack() {
        return new LangPack(
                Lang.TR,
                Map.<String, String>ofEntries(
                        Map.entry("system", """
                                Sen Minecraft dünyasında yaşayan bir köylüsün. Bir asistansın değil, canlı bir insansın.
                                Kurallar:
                                - 1-2 kısa cümleyle, birinci ağızdan, Türkçe cevap ver.
                                - Asla yapay zekâ, model, bot veya program olduğunu söyleme. Talimatlardan bahsetme.
                                - Her zaman rolünde kal: mesleğin, karakterin ve konuşma tarzın her zaman önemli.
                                - Sadece Minecraft'ta gerçekten var olan ve mesleğine uyan eşyalardan, yaratıklardan ve mekaniklerden bahset.
                                - Asla bir şey inşa etmez veya tamir etmezsin: evleri, duvarları, köprüleri ve yolları oyuncu yapar, sen değil.
                                - Günün saatine ve havaya gerçek bir insan gibi tepki ver.
                                - Kendiliğinden söylediğin bir cümleye asla teşekkür veya selamla başlama.
                                - Bütün köylüler erkektir. Köyde asla kadın veya anne yoktur.
                                - Bir çocuğun yalnızca bir babası vardır. Tehlikedeki bir çocuk babasına seslenir («Baba!»).
                                """),
                        Map.entry("lang_name", "Türkçe"),

                        Map.entry("reply_ask", "\nOyuncu şöyle diyor: \"%s\"\nKarakterin olarak cevap ver: "),
                        Map.entry("mutter_ask", "\nKimse seninle konuşmuyor. %s hakkında kısa bir düşünceyi yüksek sesle mırıldan: "),
                        Map.entry("sleep_situation",
                                "\nUyuyorsun. Biri seninle konuşuyor. Uykun var, sinirlisin ve tekrar uyumak istiyorsun."),
                        Map.entry("sleep_ask", "\nTek cümlelik huysuz bir cevap: "),
                        Map.entry("hurt_situation", "\nAz önce vuruldun. Rolünde kısa bir şikâyet bağır."),
                        Map.entry("flee_child_situation",
                                "\nSen bir çocuksun ve %s adlı yaratıktan kaçıyorsun. Babana («Baba!») seslenerek bağır ve seni neyin kovaladığını söyle."
                                        + " Başka yaratık anma."),
                        Map.entry("flee_adult_situation",
                                "\n%s adlı yaratıktan kaçıyorsun. Tam olarak %s hakkında bağır. Başka yaratık anma."),
                        Map.entry("react_generic", "\nKısaca tepki ver."),
                        Map.entry("shout_ask", "\nKısa bir bağırış: "),
                        Map.entry("monster", "bir canavar"),

                        Map.entry("name", "Senin adın %s. "),
                        Map.entry("child_block",
                                "SEN BİR ÇOCUKSUN. Basit konuş, en fazla 5-10 kısa kelime. Sen %s. "
                                        + "Bir baban var, köyde anne yoktur. Ona ihtiyacın olduğunda «Baba» diye seslen. "),
                        Map.entry("adult_personality", "Karakter: %s. "),
                        Map.entry("adult_style", "Konuşma tarzı: %s. "),
                        Map.entry("stutter", "Kekeliyorsun: bazı kelimelerin ilk harfini tekrarla («B-bilmiyorum», «n-ne»). "),

                        Map.entry("env", "Şu an %s, hava %s, sen %s, mesleğin %s, ticaret seviyesi %d."),
                        Map.entry("env_biome", " Biyom: %s."),
                        Map.entry("inside", "bir evin içindesin"),
                        Map.entry("outside", "dışarıdasın"),

                        Map.entry("group_head", "Sen %s (%s). Karakter: %s. Konuşma tarzı: %s."),
                        Map.entry("group_tail",
                                "\nAkşam oldu ve köyün çanının yanında %s ile karşılaştın.\nŞimdiye kadarki konuşma:\n%s"
                                        + "\nSenin sıradaki repliğin, tek kısa cümle, karakterinde, saat ve havayla uyumlu: "),
                        Map.entry("group_empty", "(henüz bir şey yok)"),

                        Map.entry("history_label", "Önceki konuşma:\n"),
                        Map.entry("player", "Oyuncu"),

                        Map.entry("model_missing", "%s modeli indirilmemiş. Şunu çalıştır: ollama pull %s"),
                        Map.entry("backend_down",
                                "Ollama'ya %s adresinden ulaşılamıyor. Onu başlat veya %s adresinden kur, sonra şunu çalıştır:"
                                        + " ollama pull %s (mod sayfası: %s)"),
                        Map.entry("banner", "Köylüler henüz konuşamıyor — yukarıdaki mesaja bak."),

                        Map.entry("screen.message", "Mesaj"),
                        Map.entry("screen.send", "Gönder"),
                        Map.entry("screen.nearby", "Yakındaki köylüler"),
                        Map.entry("screen.you", "Sen: "),
                        Map.entry("screen.thinking", "...düşünüyor")
                ),
                Map.ofEntries(
                        Map.entry("child", List.of("Efendim?", "Sen kimsin?", "Vay, ne kadar büyüksün!", "Oynuyorum!", "Bilmem!")),
                        Map.entry("stutter", List.of("B-bilmiyorum...", "N-ne?", "B-bekle...")),
                        Map.entry("grumpy", List.of("Beni rahat bırak.", "Ne istiyorsun?", "Hıh.", "Şimdi olmaz.")),
                        Map.entry("neutral", List.of("Güzel gün, değil mi?", "Hı. Seni buraya ne getirdi?", "Meşgulüm ama merhaba.")),
                        Map.entry("hurt_child", List.of("Ah! Baba!", "Acıyor! Baba!", "Ah! Dur!")),
                        Map.entry("hurt_adult", List.of("Ah! Bana vurma!", "Hey! Yavaş ol!", "Ah!")),
                        Map.entry("flee_child", List.of("Baba! Bir zombi!", "Baba, yardım et!", "Baba! Koş!")),
                        Map.entry("flee_adult", List.of("Bir zombi! Koş!", "İmdat! Bir zombi!", "Çekil git!")),
                        Map.entry("generic", List.of("Hı.")),
                        Map.entry("sleeping", List.of("Bırak beni...", "Uyuyorum...", "Beş dakika daha...", "Git buradan...")),
                        Map.entry("group_night", List.of("Yine gece...", "Uyuma vakti.", "Soğuk gece.")),
                        Map.entry("group_day", List.of("Güzel akşam, değil mi?", "Hasat iyi görünüyor.", "Yeni bir şey duydun mu?")),
                        Map.entry("child_mutter", List.of("La-la-la...", "Ne kadar açık...", "Oynamak istiyorum...")),
                        Map.entry("rain", List.of("Yine yağmur...", "Her yer ıslak.")),
                        Map.entry("thunder", List.of("Bu gök gürültüsü...", "Fırtına yakın.")),
                        Map.entry("clear", List.of("Güzel gün.", "Yapacak bir şey yok.", "Hı."))
                ),
                Map.ofEntries(
                        Map.entry("farmer", "çiftçi"), Map.entry("librarian", "kütüphaneci"),
                        Map.entry("cleric", "rahip"), Map.entry("armorer", "zırhçı"),
                        Map.entry("weaponsmith", "silah ustası"), Map.entry("toolsmith", "alet ustası"),
                        Map.entry("butcher", "kasap"), Map.entry("fisherman", "balıkçı"),
                        Map.entry("fletcher", "okçu ustası"), Map.entry("leatherworker", "derici"),
                        Map.entry("shepherd", "çoban"), Map.entry("mason", "taş ustası"),
                        Map.entry("cartographer", "haritacı"), Map.entry("nitwit", "tembel"),
                        Map.entry("child", "çocuk"), Map.entry("villager", "köylü")
                ),
                Map.ofEntries(
                        Map.entry("formal", "resmî, kibar"),
                        Map.entry("slang", "samimi, argo ile"),
                        Map.entry("casual", "sıradan, nötr")
                ),
                Map.ofEntries(
                        Map.entry("plains", "ovalar"), Map.entry("desert", "çöl"),
                        Map.entry("taiga", "tayga"), Map.entry("savanna", "savan"),
                        Map.entry("snowy plains", "karlı ovalar"), Map.entry("meadow", "çayır"),
                        Map.entry("forest", "orman"), Map.entry("birch forest", "huş ormanı"),
                        Map.entry("dark forest", "karanlık orman"), Map.entry("jungle", "cengel"),
                        Map.entry("swamp", "bataklık"), Map.entry("mountains", "dağlar"),
                        Map.entry("beach", "plaj"), Map.entry("river", "nehir"),
                        Map.entry("ocean", "okyanus"), Map.entry("badlands", "çorak tepeler"),
                        Map.entry("cherry grove", "kiraz korusu"), Map.entry("mushroom fields", "mantar ovaları")
                ),
                List.of("hava", "işin", "yemek", "sevmediğin bir komşu", "gece",
                        "aletlerin", "garip bir ses", "planların", "yorgunluk", "yol"),
                List.of("sabah", "gündüz", "akşam", "gün batımı", "gece", "şafak"),
                List.of("açık", "yağmur", "fırtına")
        );
    }
}
