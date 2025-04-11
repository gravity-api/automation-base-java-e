package io.automation.engine.core;

/**
 * Provides methods for switching between different models in a fluent API manner.
 */
public interface FluentModel {
    /**
     * Switches to a new instance of the specified model type.
     *
     * @param <T> the type of the model to switch to
     * @return a new instance of the specified model type
     */
    <T extends ModelBase> T switchModel();

    /**
     * Switches to a new instance of the specified model type with the given type name.
     *
     * @param <T>      the type of the model to switch to
     * @param typeName the name of the model type to switch to
     * @return a new instance of the specified model type
     */
    <T extends ModelBase> T switchModel(String typeName);

    /**
     * Switches to a new instance of the specified model type with the given type name and setup model.
     *
     * @param <T>        the type of the model to switch to
     * @param typeName   the name of the model type to switch to
     * @param setupModel the setup model containing configuration and WebDriver information
     * @return a new instance of the specified model type
     */
    <T extends ModelBase> T switchModel(String typeName, TestContext setupModel);

    /**
     * Switches to a new instance of the specified model type with the given type name,
     * setup model, and additional arguments.
     *
     * @param <T>        the type of the model to switch to
     * @param typeName   the name of the model type to switch to
     * @param setupModel the setup model containing configuration and WebDriver information
     * @param arguments  additional arguments to pass to the model's constructor
     * @return a new instance of the specified model type
     */
    <T extends ModelBase> T switchModel(String typeName, TestContext setupModel, Object... arguments);

    /**
     * Switches to a new instance of the specified model type.
     *
     * @param <T>  the type of the model to switch to
     * @param type the {@code Class} of the model to switch to
     * @return a new instance of the specified model type
     */
    <T extends ModelBase> T switchModel(Class<T> type);

    /**
     * Switches to a new instance of the specified model type with the given setup model.
     *
     * @param <T>        the type of the model to switch to
     * @param type       the {@code Class} of the model to switch to
     * @param setupModel the setup model containing configuration and WebDriver information
     * @return a new instance of the specified model type
     */
    <T extends ModelBase> T switchModel(Class<T> type, TestContext setupModel);

    /**
     * Switches to a new instance of the specified model type with additional arguments.
     *
     * @param <T>        the type of the model to switch to
     * @param type       the {@code Class} of the model to switch to
     * @param setupModel the setup model containing configuration and WebDriver information
     * @param arguments  additional arguments to pass to the model's constructor
     * @return a new instance of the specified model type
     */
    <T extends ModelBase> T switchModel(Class<T> type, TestContext setupModel, Object... arguments);
}
