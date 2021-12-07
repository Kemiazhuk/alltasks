package com.efimchick.ifmo.streams.countwords;

import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.regex.Matcher;

final class MatchItr extends Spliterators.AbstractSpliterator<String> {
    private final Matcher matcher;

    MatchItr(Matcher m) {
        super(m.regionEnd() - m.regionStart(), ORDERED | NONNULL);
        matcher = m;
    }

    private boolean advance(Consumer<? super String> action) {
        action.accept(matcher.group());
        return true;
    }

    public boolean tryAdvance(Consumer<? super String> action) {
        return matcher.find() && advance(action);
    }
}
