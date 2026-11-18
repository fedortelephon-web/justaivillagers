package com.justaivillagers.core;

import java.util.LinkedHashMap;
import java.util.Map;

/** Registry of the language packs, keyed by {@link Lang} code. */
public final class LangData {

    private static final Map<String, LangPack> PACKS = build();

    private LangData() {
    }

    private static Map<String, LangPack> build() {
        Map<String, LangPack> map = new LinkedHashMap<>();
        for (LangPack pack : new LangPack[]{
                LangDataEn.pack(), LangDataTr.pack(), LangDataEs.pack(), LangDataPt.pack(),
                LangDataDe.pack(), LangDataPl.pack(), LangDataFr.pack(), LangDataIt.pack(),
                LangDataRu.pack()}) {
            map.put(pack.code, pack);
        }
        return Map.copyOf(map);
    }

    /** The pack for a supported language code; unknown codes get English rather than a crash. */
    public static LangPack of(String code) {
        LangPack pack = code == null ? null : PACKS.get(code);
        return pack != null ? pack : PACKS.get(Lang.EN);
    }

    public static boolean has(String code) {
        return code != null && PACKS.containsKey(code);
    }

    public static int count() {
        return PACKS.size();
    }
}
