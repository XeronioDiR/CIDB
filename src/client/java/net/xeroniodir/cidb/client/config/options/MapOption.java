package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.screens.MapConfigScreen; // Новый экран
import net.xeroniodir.cidb.client.config.widgets.ListButtonWidget; // Используем ту же кнопку
import net.xeroniodir.cidb.client.config.widgets.MapButtonWidget;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class MapOption<K, V> extends Option<Map<K, V>> {

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    public final TriFunction<K, Consumer<K>, Supplier<K>, Option<K>> keyOptionFactory;
    public final TriFunction<V, Consumer<V>, Supplier<V>, Option<V>> valueOptionFactory;

    private final Supplier<K> defaultKeySupplier;
    private final Supplier<V> defaultValueSupplier;

    public MapOption(String title,
                     Map<K, V> defaultValue,
                     Supplier<Map<K, V>> getter,
                     Consumer<Map<K, V>> setter,
                     Supplier<K> defaultKeySupplier,
                     Supplier<V> defaultValueSupplier,
                     TriFunction<K, Consumer<K>, Supplier<K>, Option<K>> keyOptionFactory,
                     TriFunction<V, Consumer<V>, Supplier<V>, Option<V>> valueOptionFactory,
                     String description) {
        super(title, defaultValue, getter, setter, description);
        this.defaultKeySupplier = defaultKeySupplier;
        this.defaultValueSupplier = defaultValueSupplier;
        this.keyOptionFactory = keyOptionFactory;
        this.valueOptionFactory = valueOptionFactory;
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        return new MapButtonWidget(x, y, width, 20, this);
    }

    public Option<K> createKeyOption(K key, Consumer<K> setter, Supplier<K> getter) {
        return keyOptionFactory.apply(key, setter, getter);
    }

    public Option<V> createValueOption(V value, Consumer<V> setter, Supplier<V> getter) {
        return valueOptionFactory.apply(value, setter, getter);
    }

    // Методы для создания новых элементов
    public K createNewKeyDefault() { return defaultKeySupplier.get(); }
    public V createNewValueDefault() { return defaultValueSupplier.get(); }
}