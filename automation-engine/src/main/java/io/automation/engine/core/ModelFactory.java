package io.automation.engine.core;

import io.automation.engine.exceptions.ModelCreationException;

import java.util.*;

public class ModelFactory {
    /**
     * Private constructor to prevent instantiation of the {@code ModelFactory} class.
     */
    private ModelFactory() {

    }

    /**
     * Creates an instance of the specified model class using the provided parameters.
     *
     * @param <T>        the type of the model class, which must extend {@code PageModelBase}.
     * @param modelClass the class of the model to instantiate.
     * @param parameters the parameters to pass to the model's constructor.
     * @return an instance of the specified model class.
     * @throws ModelCreationException if the model instance cannot be created.
     */
    public static <T extends PageModelBase> T newModel(Class<T> modelClass, Object... parameters) {
        try {
            // Determine the parameter types based on the provided parameters.
            Class<?>[] parameterTypes = Arrays
                    .stream(parameters)
                    .map(Object::getClass) // Map each parameter to its runtime class.
                    .toArray(Class[]::new); // Collect the classes into an array.

            // Use reflection to create a new instance of the model class with the specified parameters.
            return modelClass.getDeclaredConstructor(parameterTypes).newInstance(parameters);
        } catch (Exception e) {
            // Wrap any exception in a ModelCreationException and include the original cause.
            throw new ModelCreationException("Failed to create model instance", e.getCause());
        }
    }
}
