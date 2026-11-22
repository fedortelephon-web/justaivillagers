package com.justaivillagers.core;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stable, lazily generated personality for each villager: name, character traits, speech style and
 * stutter flag. Deterministic per UUID so a villager keeps its identity across sessions without
 * persisting anything.
 */
public final class VillagerProfile {

    public final UUID id;
    public final String name;
    public final String personality;
    public final SpeechStyle style;
    public final boolean stutters;
    public final boolean child;
    public final ChildKind childKind;

    public enum SpeechStyle {
        FORMAL,
        CASUAL,
        SLANG
    }

    public enum ChildKind {
        NONE,
        CURIOUS,
        CAUTIOUS
    }

    public VillagerProfile(UUID id, String name, String personality, SpeechStyle style, boolean stutters,
                           boolean child, ChildKind childKind) {
        this.id = id;
        this.name = name;
        this.personality = personality;
        this.style = style;
        this.stutters = stutters;
        this.child = child;
        this.childKind = childKind;
    }

    public String styleKey() {
        return style.name().toLowerCase(Locale.ROOT);
    }

    /**
     * The same person, now a child or an adult. Name, character and speech style are kept; only the
     * age-dependent bits change, so growing up does not turn the villager into a stranger.
     */
    public VillagerProfile withChild(boolean nowChild) {
        if (nowChild == child) {
            return this;
        }
        ChildKind kind = nowChild
                ? (childKind == ChildKind.NONE ? ChildKind.CURIOUS : childKind)
                : ChildKind.NONE;
        return new VillagerProfile(id, name, personality, style, stutters, nowChild, kind);
    }

    public String childKindKey() {
        return childKind == ChildKind.NONE ? "" : childKind.name().toLowerCase(Locale.ROOT);
    }

    /** Whether the trait is one of the unpleasant ones, used to colour fallback lines. */
    public boolean negative() {
        return FallbackLines.isNegative(personality);
    }

    /** Cache with a bounded size: original leaked an entry per villager ever seen in the world. */
    public static final class Cache {

        private static final int MAX_ENTRIES = 512;

        // Keyed by language as well as UUID: the name and the character come from per-language word
        // pools, so caching by UUID alone left a villager called "Леонид" and described as
        // "подозрительный" sitting in an English client after the language was switched. The UUID
        // still decides who the villager is, so the same person is drawn again in the new language.
        private final ConcurrentHashMap<String, VillagerProfile> profiles = new ConcurrentHashMap<>();

        private static String key(UUID id, String lang) {
            return Lang.normalise(lang) + "/" + id;
        }

        public VillagerProfile get(UUID id, boolean child, String lang) {
            String key = key(id, lang);
            VillagerProfile existing = profiles.get(key);
            if (existing == null) {
                VillagerProfile created = create(id, child, lang);
                if (profiles.size() >= MAX_ENTRIES) {
                    profiles.clear();
                }
                profiles.put(key, created);
                return created;
            }
            if (existing.child == child) {
                return existing;
            }
            // The villager grew up (or was somehow marked as a baby again). Identity is permanent: a
            // UUID that changes name at adulthood would look like a stranger, and its history would
            // be signed with the wrong name in every later prompt.
            VillagerProfile aged = existing.withChild(child);
            profiles.put(key, aged);
            return aged;
        }

        public void clear() {
            profiles.clear();
        }

        public int size() {
            return profiles.size();
        }

        private VillagerProfile create(UUID id, boolean child, String lang) {
            // Derive everything from the UUID so the same villager is the same person after a restart.
            // Not influenced by load order or by which other villagers are cached: an
            // earlier attempt to avoid duplicates by consulting the cache renamed villagers between
            // sessions, because the cache is empty on startup and fills in a different order.
            long seed = id.getMostSignificantBits() ^ id.getLeastSignificantBits();
            Random rng = new Random(seed);
            List<String> namePool = Names.namePool(lang);
            List<String> goodPool = Names.personalityPool(lang, true);
            List<String> badPool = Names.personalityPool(lang, false);
            boolean nice = rng.nextBoolean();
            List<String> pool = nice ? goodPool : badPool;
            String givenName = namePool.get(rng.nextInt(namePool.size()));
            String name = givenName;
            List<String> surnames = Names.surnamePool(lang);
            // A surname is drawn for a minority of villagers only: a village where everyone has two
            // names reads like a legal document. That still multiplies the distinct-name space by
            // roughly the size of the surname pool, which is what kills the duplicate problem.
            if (surnames != null && !surnames.isEmpty() && rng.nextInt(4) == 0) {
                name = givenName + " " + surnames.get(rng.nextInt(surnames.size()));
            }
            String personality = pool.get(rng.nextInt(pool.size()));
            SpeechStyle style = SpeechStyle.values()[rng.nextInt(SpeechStyle.values().length)];
            boolean stutters = !child && rng.nextInt(100) < 12;
            ChildKind kind = !child ? ChildKind.NONE
                    : (rng.nextBoolean() ? ChildKind.CURIOUS : ChildKind.CAUTIOUS);
            return new VillagerProfile(id, name, personality, style, stutters, child, kind);
        }
    }
}
