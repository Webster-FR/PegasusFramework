package fr.pegasus.papermc.tools.dispatcher;

import fr.pegasus.papermc.tools.PegasusRandom;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher {
    private final DispatcherAlgorithm dispatchAlgorithm;

    /**
     * Create a new Dispatcher
     * @param dispatchAlgorithm The dispatch algorithm to use
     */
    public Dispatcher(@NotNull DispatcherAlgorithm dispatchAlgorithm){
        this.dispatchAlgorithm = dispatchAlgorithm;
    }

    /**
     * Dispatch the values to the keys
     * @param keys The keys
     * @param values The values
     * @return The final map
     * @param <A> The type of the keys
     * @param <B> The type of the values
     */
    public <A, B> Map<A, B> dispatch(@NotNull List<A> keys, @NotNull List<B> values){
        Map<A, B> finalMap = new HashMap<>();
        switch (dispatchAlgorithm){
            case ROUND_ROBIN -> {
                for(int i = 0; i < keys.size(); i++)
                    finalMap.put(keys.get(i), values.get(i % (values.size())));
            }
            case REVERSE_ROUND_ROBIN -> {
                for(int i = keys.size() - 1; i >= 0; i--)
                    finalMap.put(keys.get(i), values.get(i % (values.size())));
            }
            case RANDOM -> {
                for(A key: keys)
                    finalMap.put(key, values.get(new PegasusRandom().nextInt(values.size())));
            }
            case RANDOM_UNIQUE -> {
                if(keys.size() > values.size())
                    throw new RuntimeException("The number of keys must be less than or equal to the number of values (%d keys provided, %d values provided)"
                            .formatted(keys.size(), values.size()));
                List<B> valuesCopy = new ArrayList<>(values);
                int random;
                for(A key: keys){
                    random = new PegasusRandom().nextInt(valuesCopy.size());
                    finalMap.put(key, valuesCopy.get(random));
                    valuesCopy.remove(random);
                }
            }
            default -> throw new RuntimeException("Unknown dispatch algorithm");
        }
        return finalMap;
    }
}
