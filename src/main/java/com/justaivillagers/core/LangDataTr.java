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

        Map.entry("flee_child_line", "Baba! %s!"),
        Map.entry("flee_adult_line", "%s! Koş!"),
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

                        Map.entry("model_missing", "%s modeli indirilmemiş. O ile ayarları açıp İndir'e basın."),
                        Map.entry("backend_down", "Ollama çalışmıyor. Kurun ve başlatın: %s - ardından O ile ayarları açıp Kontrol et'e basın."),
                        Map.entry("banner", "Ayarları O tuşuyla açın."),
                        Map.entry("settings.check.tip", "Yukarıdaki adresi ve modeli kaydetmeden sına"),
                        Map.entry("backend_down.repeat", "Ollama hâlâ çalışmıyor, bu yüzden köylüler konuşamıyor. Ayarlar: O tuşu."),
                        Map.entry("settings.server.hint", "sunucu adresi"),
                        Map.entry("settings.server.tip", "Ollama veya uyumlu sunucunun adresi. Varsayılan http://127.0.0.1:11434"),
                        Map.entry("settings.model.custom.hint", "model adı"),
                        Map.entry("settings.model.custom.tip", "Ollama'da indirilmiş herhangi bir model. Liste: ollama list"),
                        Map.entry("settings.model.tip", "%s - %s indirme, yaklaşık %s RAM."),
                        Map.entry("settings.model.get.tip", "%s modelini Ollama ile indir"),
                        Map.entry("settings.ollama.download.tip", "Ollama kurulum dosyasını indir. Sonra kendin çalıştır - mod program başlatmaz."),
                        Map.entry("settings.model.tip.qwen2_5_0_5b", "En hızlı ve en basit. Kısa replikler için uygun, sık sık tutarsız."),
                        Map.entry("settings.model.tip.qwen3_0_6b", "En küçüğe göre biraz daha tutarlı, konuyu iyi korur."),
                        Map.entry("settings.model.tip.gemma3_1b", "Zayıf bilgisayarlar için iyi denge: doğal konuşma, az RAM."),
                        Map.entry("settings.model.tip.llama3_2_1b", "Belirgin şekilde daha canlı ve konuşkan, ama bazen konudan sapar."),
                        Map.entry("settings.model.tip.gemma3_4b", "Listedeki en iyi konuşma kalitesi. Yaklaşık 5 GB boş RAM gerekir."),

                        Map.entry("screen.message", "Mesaj"),
                        Map.entry("screen.send", "Gönder"),
                        Map.entry("screen.nearby", "Yakındaki köylüler"),
                        Map.entry("screen.you", "Sen: "),
                        Map.entry("screen.thinking", "...düşünüyor"),
                        Map.entry("settings.title", "Just AI Villagers - ayarlar"),
                        Map.entry("settings.tab.model", "Model"),
                        Map.entry("settings.tab.features", "Davranış"),
                        Map.entry("settings.tab.language", "Dil"),
                        Map.entry("settings.server", "Sunucu adresi"),
                        Map.entry("settings.model", "Model"),
                        Map.entry("settings.model.custom", "Özel"),
                        Map.entry("settings.model.needs", "indirme %s, yaklaşık %s RAM gerekir"),
                        Map.entry("settings.model.download", "Modeli indir"),

        Map.entry("settings.status.nomodel", "Sunucu çalışıyor, %s indirilmemiş"),
        Map.entry("settings.model.installed.tip", "Bu model zaten indirilmiş, indirme yapmadan seçebilirsiniz"),
        Map.entry("settings.model.failed", "%s indirilemedi: %s"),
        Map.entry("settings.model.done", "%s modeli indirildi"),
                        Map.entry("settings.model.get", "Д°ndir"),
                        Map.entry("settings.model.installed", "kurulu"),
                        Map.entry("settings.model.notinstalled", "indirilmedi"),
                        Map.entry("settings.model.downloading", "indiriliyor: %s"),
                        Map.entry("settings.ollama.title", "Ollama çalışmıyor"),
                        Map.entry("settings.ollama.body", "Mod konuşmak için Ollama'ya ihtiyaç duyar. İndir, kur, sonra Tekrar dene."),
                        Map.entry("settings.ollama.download", "Ollama indir"),
                        Map.entry("settings.help", "Neden çalışmıyor?.."),
                        Map.entry("settings.help.tip", "Adım adım kılavuz: Ollama kurulumu, sunucuyu başlatma, model indirme"),
                        Map.entry("help.page", "Sayfa %s / %s"),
                        Map.entry("help.prev", "Geri"),
                        Map.entry("help.next", "İleri"),

        Map.entry("help.title", "Köylülerle konuşma nasıl ayarlanır"),
        Map.entry("help.copied", "Kopyalandı"),
        Map.entry("help.intro", "Köylülerin size cevap verebilmesi için mod, Ollama adlı program üzerinden doğrudan bilgisayarınızda çalışan bir sinir ağı kullanır. Her şey yerel olarak ve ücretsiz çalışır; internete yalnızca bir kez, dosyaları indirmek için ihtiyaç vardır."),
        Map.entry("help.step1", "## Adım 1. Ollama programını kurun\nollama.com/download adresine gidin ve sisteminiz için programı kurun:\n#copy|site bağlantısını kopyala|https://ollama.com/download\n• Windows: OllamaSetup.exe dosyasını indirin, çalıştırın ve kurulumun bitmesini bekleyin.\n• macOS: arşivi indirin, açın ve Ollama simgesini «Programlar» klasörüne sürükleyin.\n• Linux: terminali açın ve şu komutu çalıştırın: curl -fsSL https://ollama.com/install.sh | sh\n#copy|Linux indirme komutunu kopyala|curl -fsSL https://ollama.com/install.sh | sh"),
        Map.entry("help.step2", "## Adım 2. Sunucuyu başlatın\nProgram oyun sırasında arka planda çalışmalıdır:\n• Windows: kurulumdan sonra Ollama kendiliğinden başlar (ekranın sağ alt köşesinde, saatin yanında bir lama simgesi belirir). Orada değilse Windows aramasında Ollama'yı bulup başlatın.\n• macOS: Ollama'yı «Programlar» klasöründen açın.\n• Linux: terminale sudo systemctl start ollama yazın\nYedek çözüm: yerel ollama sunucusunu başlatmak için işletim sisteminizin terminaline ollama serve komutunu yazın.\n#copy|komutu kopyala|ollama serve"),
        Map.entry("help.step3", "## Adım 3. «Beynini» indirin (dil modelini)\n1. Oyunda modun ayarlar menüsünü açın (varsayılan olarak O tuşu).\n2. Listeden uygun bir model seçin. İpucu: ilk kullanım ve zayıf bilgisayarlar için en iyisi Qwen2.5 0.5B'dir (yalnızca 398 MB ve en hızlı cevap veren).\n3. «İndir» düğmesine basın ve durum «Kaydedildi» olana kadar bekleyin."),
        Map.entry("help.step4", "## Adım 4. Bağlantıyı kontrol edin ve oyuna başlayın\n1. Aynı ayarlar menüsünde «Kontrol et» düğmesine basın.\n2. Durum «Bağlandı» olarak görünüyorsa her şey doğru ayarlanmış! Menüyü kapatın, herhangi bir köylüye yaklaşın ve konuşma tuşuna basın.\n(«Sunucu adresi» alanını varsayılan olarak bırakın: http://127.0.0.1:11434. Yalnızca sinir ağı başka bir bilgisayarda çalışıyorsa ileri düzey kullanıcıların değiştirmesi gerekir)."),
        Map.entry("help.ifnot", "## Sık karşılaşılan sorunlar ve çözümleri\n• «Bağlantı yok» hatası\nOllama programı çalışmıyor veya engellenmiş. Lama simgesinin tepsi alanında, saatin yanında olup olmadığını kontrol edin. Varsa ama hatalar kaybolmuyorsa antivirüsün veya Windows güvenlik duvarının Ollama'yı engellemediğinden emin olun.\n• «Model indirilmedi» hatası\nProgramla bağlantı var ama hiçbir sinir ağı yüklenmemiş. Adım 3'e dönün ve «İndir» düğmesine basın.\n• Köylüler çok uzun süre düşünüyor\nBilgisayarınız seçilen sinir ağını zor işliyor. Ayarlardan daha küçük bir model seçin (örneğin 0.5B veya 1.5B) ve en fazla cevap uzunluğunu azaltın."),
                        Map.entry("help.close", "Kapat"),
                        Map.entry("settings.ollama.copy", "Bağlantıyı kopyala"),
                        Map.entry("settings.ollama.command.copy", "Komutu kopyala"),
                        Map.entry("settings.ollama.cancel", "İndirmeyi iptal et"),
                        Map.entry("settings.ollama.cancel.tip", "Yükleyici indirmesini durdur"),
                        Map.entry("settings.ollama.copied", "Bağlantı kopyalandı - tarayıcının adres çubuğuna yapıştır."),
                        Map.entry("settings.ollama.command.copied", "Komut kopyalandı - PowerShell veya cmd'ye yapıştır."),
                        Map.entry("settings.ollama.command.tip", "Ollama sunucusunu başlatan komutu kopyalar"),
                        Map.entry("settings.ollama.paste.hint", "Kopyalanan komutu PowerShell veya cmd'ye yapıştır (Win+R, cmd yaz, Enter) ve Enter'a bas. Sonra Sına'ya bas."),
                        Map.entry("settings.ollama.page", "Sayfayı aç"),
                        Map.entry("settings.ollama.saved", "%s konumuna kaydedildi - çalıştır, sonra Tekrar dene."),
                        Map.entry("settings.check", "Tekrar dene"),

        Map.entry("join.hint", "Mod ayarları %s tuşunda. Komutlar: /justaivillagers help"),
        Map.entry("cmd.help.header", "Mod komutları:"),
        Map.entry("cmd.help.status", "/justaivillagers status — sunucu, model ve süren indirme"),
        Map.entry("cmd.help.settings", "/justaivillagers settings — ayarları aç"),
        Map.entry("cmd.help.reload", "/justaivillagers reload — ayar dosyasını yeniden oku"),
        Map.entry("cmd.help.help", "/justaivillagers help — bu liste"),
        Map.entry("cmd.status.header", "Mod durumu:"),
        Map.entry("cmd.status.address", "Sunucu: %s"),
        Map.entry("cmd.status.model", "Model: %s"),
        Map.entry("cmd.status.model.missing", "Model: %s (indirilmemiş)"),
        Map.entry("cmd.status.state.ok", "Sunucu yanıt veriyor, köylüler konuşabilir"),
        Map.entry("cmd.status.state.down", "Sunucu yanıt vermiyor — köylüler susuyor"),
        Map.entry("cmd.status.hint", "Ayarlar %s tuşunda"),
        Map.entry("cmd.reload.done", "Ayarlar yeniden yüklendi"),
        Map.entry("cmd.reload.same", "Dosya değişmemiş: %s"),
                        Map.entry("settings.status.ok", "Bağlandı: %s"),
                        Map.entry("settings.status.down", "Bağlanmadı"),
                        Map.entry("settings.general", "Genel"),
                        Map.entry("settings.speech", "Konuşma"),
                        Map.entry("settings.advanced", "Gelişmiş"),
                        Map.entry("settings.temperature", "Konuşma çeşitliliği"),
                        Map.entry("settings.maxTokens", "Cevap uzunluğu"),
                        Map.entry("settings.speechPace", "Konuşma hızı"),
                        Map.entry("settings.spontaneous", "Köylüler kendi kendine konuşur"),
                        Map.entry("settings.group", "İki köylü akşam konuşur"),
                        Map.entry("settings.reactions", "Vurulmaya tepki verir"),
                        Map.entry("settings.fallback", "Model olmadan konuşur"),

        Map.entry("settings.spontaneous.tip", "Bir köylü yakınındayken bazen kendiliğinden konuşmaya başlar. Beklenmedik sözler istemiyorsan kapat."),
        Map.entry("settings.group.tip", "Akşamları iki köylü kendi aralarında konuşmaya başlayabilir ve siz duyarsınız. Sadece kendinizle konuşulsun istiyorsanız kapatın."),
        Map.entry("settings.reactions.tip", "Bir köylü vurduğunuzda kısa bir karşılık verir. Vurulduğunda sussun istiyorsanız kapatın."),
        Map.entry("settings.fallback.tip", "Model yanıt vermezse veya sunucuya ulaşılamazsa köylü susmak yerine hazır bir cümle söyler. Hata durumunda sussun istiyorsanız kapatın."),
        Map.entry("settings.debug.tip", "Bir köylüyle konuşurken modele giden isteği sohbette gösterir; yanıtın neden yanlış geldiğini görmek için. Normal oyunda kapalı."),
        Map.entry("settings.temperature.tip", "Köylünün kelimeleri ne kadar çeşitlendirdiği. Düşük tahmin edilebilir ve konuya uygun, yüksek daha canlı ama bazen konu dışı."),
        Map.entry("settings.maxTokens.tip", "Bir yanıta ne kadar metin sığdığı. Yüksek olursa köylü daha çok şey söyler ama yanıt daha uzun sürer."),
        Map.entry("settings.speechPace.tip", "Yanıtın konuşma penceresinde harf harf ne kadar hızlı göründüğü. 0 hepsini birden gösterir; yüksek daha yavaş ve canlıdır."),
        Map.entry("settings.history.tip", "Köylünün bir konuşmada kaç son cümleyi hatırladığı. Fazlası daha iyi bağlam demek, ama her yanıt daha uzun sürer."),
        Map.entry("settings.key.dialog.tip", "Bir köylüyle konuşmayı başlatan tuş. Satıra tıklayın ve istediğiniz tuşa basın."),
        Map.entry("settings.key.settings.tip", "Bu ayarlar menüsünü açan tuş. Satıra tıklayın ve istediğiniz tuşa basın."),
        Map.entry("settings.debug", "İstemleri sohbete yaz"),
                        Map.entry("settings.timeout", "İstek zaman aşımı"),
                        Map.entry("settings.history", "Hafıza"),
                        Map.entry("settings.key.dialog", "KГ¶ylГјyle konuЕџ"),
                        Map.entry("settings.key.settings", "Mod ayarlarД±"),
                        Map.entry("settings.key.press", "bir tuЕџa basД±n..."),
                        Map.entry("settings.language", "Köylülerin dili"),
                        Map.entry("settings.language.auto", "Oyunla aynı"),
                        Map.entry("settings.save", "Kaydet"),
                        Map.entry("settings.cancel", "İptal"),
                        Map.entry("settings.saved", "Kaydedildi. Değişiklik hemen geçerli."),
                        Map.entry("settings.close", "Kapat")
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
                List.of("açık", "yağmur", "fırtına"),
                                Map.ofEntries(
                        Map.entry("farmer", "Mesleğin. Şu şeylerle çalışırsın: buğday, ekmek, havuç, patates, pancar, balkabağı, çapa ve kompost."),
                        Map.entry("librarian", "Mesleğin. Şu şeylerle çalışırsın: kitap, kitaplık, kâğıt, kitap ve tüy, mürekkep kesesi ve fener."),
                        Map.entry("cleric", "Mesleğin. Şu şeylerle çalışırsın: redstone, lapis lazuli, parlayan taş, ender incisi, büyü şişesi, çürük et ve mayalama standı."),
                        Map.entry("armorer", "Mesleğin. Şu şeylerle çalışırsın: demir külçe, kömür, lav kovası, demir miğfer, demir göğüslük, zincir zırh, kalkan ve çan."),
                        Map.entry("weaponsmith", "Mesleğin. Şu şeylerle çalışırsın: demir külçe, kömür, çakmak taşı, demir kılıç, demir balta ve bileme taşı."),
                        Map.entry("toolsmith", "Mesleğin. Şu şeylerle çalışırsın: demir külçe, kömür, çakmak taşı, demir kazma, demir kürek, demir çapa ve çan."),
                        Map.entry("butcher", "Mesleğin. Şu şeylerle çalışırsın: çiğ domuz eti, çiğ sığır eti, çiğ koyun eti, çiğ tavuk, pişmiş domuz eti ve biftek."),
                        Map.entry("fisherman", "Mesleğin. Şu şeylerle çalışırsın: çiğ morina, çiğ somon, tropikal balık, olta, kova ve kamp ateşi."),
                        Map.entry("fletcher", "Mesleğin. Şu şeylerle çalışırsın: sopa, çakmak taşı, ip, tüy, yay, arbalet ve ok."),
                        Map.entry("leatherworker", "Mesleğin. Şu şeylerle çalışırsın: deri, eyer, deri başlık, deri yelek, deri bot ve tavşan derisi."),
                        Map.entry("shepherd", "Mesleğin. Şu şeylerle çalışırsın: yün, makas, halı, yatak, tablo ve boya."),
                        Map.entry("mason", "Mesleğin. Şu şeylerle çalışırsın: taş tuğla, kil topu, tuğla, pişmiş toprak, kuvars, cilalı taş ve andezit."),
                        Map.entry("cartographer", "Mesleğin. Şu şeylerle çalışırsın: kâğıt, pusula, harita, boş harita, flama, eşya çerçevesi ve cam panel."),
                        Map.entry("nitwit", "Bir mesleğin yok. Köyün tembelisin: yemek, uyku ve dedikodu hakkında konuşursun, çalışanlara bakarsın."),
                        Map.entry("child", "Sen bir çocuksun ve mesleğin yok. Oyun, yemek ve baban hakkında konuşursun."),
                        Map.entry("villager", "Henüz bir mesleğin yok. Yemek, köy ve ne yapmak istediğin hakkında konuşursun.")
                )
        );
    }
}
