package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.widgets.ListButtonWidget;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class ListOption<T> extends Option<List<T>> {

    public final TriFunction<T, Consumer<T>, Supplier<T>, Option<T>> elementOptionFactory; // <-- TriFunction

    private final Supplier<T> defaultElementSupplier;

    public ListOption(String title,
                      List<T> defaultValue,
                      Supplier<List<T>> getter,
                      Consumer<List<T>> setter,
                      Supplier<T> defaultElementSupplier,
                      TriFunction<T, Consumer<T>, Supplier<T>, Option<T>> elementOptionFactory,
                      String description) {
        super(title, defaultValue, getter, setter, description);
        this.defaultElementSupplier = defaultElementSupplier;
        this.elementOptionFactory = elementOptionFactory;
    }

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    public Option<T> createOptionForElement(T value, Consumer<T> setter, Supplier<T> getter) {
        return elementOptionFactory.apply(value, setter, getter);
    }

    public T createNewItemDefault() {
        return defaultElementSupplier.get();
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        return new ListButtonWidget(x, y, width, 20, (ListOption<Object>) this);
    }
}